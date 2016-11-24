import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple7;
import org.apache.flink.api.java.tuple.Tuple8;
import org.apache.flink.api.java.tuple.Tuple9;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer08;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer08;
import org.apache.flink.streaming.util.serialization.SerializationSchema;
import org.apache.flink.util.Collector;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import pojo.CAdvisor;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Properties;

public class MinuteConsumer {
    public static void main(String[] args) throws Exception {
        final Instant now = Instant.now();

        // parse input arguments
        final ParameterTool parameterTool = ParameterTool.fromArgs(args);

        if(parameterTool.getNumberOfParameters() < 4) {
            System.out.println("Missing parameters!\nUsage: Kafka --topic <topic> " +
                    "--bootstrap.servers <kafka brokers> --zookeeper.connect <zk quorum> --group.id <some id>");
            return;
        }

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.getConfig().setGlobalJobParameters(parameterTool); // make parameters available in the web interface
        env.getConfig().setAutoWatermarkInterval(1000); // Sets the interval of the automatic watermark emission.
        env.enableCheckpointing(5000); // Create a checkpoint every 5 seconds
        // try to restart 60 times with 10 seconds delay (10 Minutes)
//        env.setParallelism(40);

        // configure Kafka consumer
        Properties props = new Properties();
//        props.setProperty("zookeeper.connect", "10.0.7.13:2181");     // Zookeeper default host:port
//        props.setProperty("bootstrap.servers", "10.0.7.13:9092");     // Broker default host:port
        props.setProperty("zookeeper.connect", "10.48.98.232:2181");    // Zookeeper default host:port
        props.setProperty("bootstrap.servers", "10.48.98.232:9092");    // Broker default host:port
        props.setProperty("group.id", "myGroup");                       // Consumer group ID
        //props.setProperty("log.retention.hours", "2");                  // How long to retain the logs before deleting them
        props.setProperty("auto.offset.reset", "largest");           // Always read topic from end

        // Open Kafka Stream
        FlinkKafkaConsumer08<CAdvisor> consumer = new FlinkKafkaConsumer08<>(
                parameterTool.getRequired("topic"), // Get topic name
                new CAdvisorDeserializer(),
                props
        );

        // Assign a timestamp extractor to the consumer
        //consumer.assignTimestampsAndWatermarks(new CAdvisorTimestampExtractor());

        DataStream<CAdvisor> dataStream = env.addSource(consumer);

        PredictionModel pm = new PredictionModel();

        // Train Model Stream
        DataStream<Tuple9<String, String, Double, Double, Double, Long, Long, Long, Long>> trainModelStream = dataStream
            .rebalance()
            .assignTimestampsAndWatermarks(new CAdvisorTimestampExtractor())
            .keyBy(CAdvisor.MACHINE_NAME_ID, CAdvisor.CONTAINER_NAME_ID) // Group by machine name and container name
            //.timeWindow(Time.seconds(60))
            .flatMap(new PredictionModel()); // Predict and refine model per machine and container

        DataStream<Tuple9<String, String, Double, Double, Double, Long, Long, Long, Long>> newStream = trainModelStream
                .keyBy(0, 1) // Key on machine name and container name
                //.timeWindow(Time.seconds(10))
                .countWindow(10) // Count 10 items and return the result
                .maxBy(5); // And return the maximum value of our x value (latest training model)

        newStream.addSink(new FlinkKafkaProducer08<>("models-bundled", new Tuple9Serialization(), props));

        // Write the training model result (machineName, containerName, timestamp, ramUsage) to a new kafka stream
        //trainModelStream.addSink(new FlinkKafkaProducer08<>("ram-usage-data", new Tuple7Serialization(), props));

        // Print the processed stream that gets sent to the frontend on stdout
        //trainModelStream.print();
        newStream.print();

        System.out.println(env.getExecutionPlan());

        env.execute("Read from Kafka example");
    }

    public static class Tuple9Serialization implements SerializationSchema<Tuple9<String, String, Double, Double, Double, Long, Long, Long, Long>> {

        @Override
        public byte[] serialize(Tuple9<String, String, Double, Double, Double, Long, Long, Long, Long> element) {
            JSONObject jo = new JSONObject();

            try {
                jo.put("machine_name", element.f0);
                jo.put("container_name", element.f1);
                jo.put("model_intercept", element.f2);
                jo.put("model_slope", element.f3);
                jo.put("model_slope_err", element.f4);
                jo.put("x", element.f5); // timestamp
                jo.put("y", element.f6); // ram
                jo.put("script_start_time", element.f7);
                jo.put("script_current_time", element.f8);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jo.toString().getBytes();
        }
    }

    /**
     * Predicts the new RAM Usage for a CAdvisor Machine/container event based on previous values.
     * Incrementally trains a regression model using cAdvisor and memoryUsage
     *
     * Returns (MachineName, ContainerName, PredictedRAM)
     */
    public static class PredictionModel extends RichFlatMapFunction<CAdvisor, Tuple9<String, String, Double, Double, Double, Long, Long, Long, Long>> {
        // <MachineName <ContainerName <TrainModel>>>
        private transient ValueState<RAMUsagePredictionModel> modelState;
        private final Instant now = Instant.now();

        public ValueState<RAMUsagePredictionModel> getModelState() {
            return modelState;
        }

        @Override
        public void flatMap(CAdvisor cAdvisor, Collector<Tuple9<String, String, Double, Double, Double, Long, Long, Long, Long>> out) throws Exception {
            // Fetch operator state
            RAMUsagePredictionModel model = modelState.value();

            // Java does not support time granularity above milliseconds in its date patterns
            // Which is a problem, since Go returns nanoseconds in time.Time
            // https://docs.oracle.com/javase/tutorial/datetime/iso/instant.html
            //TemporalAccessor creationAccessor = DateTimeFormatter.ISO_DATE_TIME.parse( cAdvisor.getTimestamp() );
            try {
                // TODO: Currently we get the timestamp as the script processing timestamp
                // This however is not correct, since we need the cAdvisor.getTimestamp() which is when the metric was measured
                // But if we use that, we also need the container starttime, which is not provided by cadvisor
                // So the choice was made to use the script processing time for now
                // Instant instant = Instant.parse(cAdvisor.getTimestamp());
                // Long epoch = instant.getEpochSecond();
                // epoch = epoch - now.getEpochSecond(); // Normalise to the start of the script (else we calculate 40 years in unneeded data)
                Long epoch = Instant.now().getEpochSecond() - now.getEpochSecond();

                // Emit the used values to train the model
                //Long y = Double.valueOf(((double)cAdvisor.getContainerStats().getCpu().getUsage().getSystem() / cAdvisor.getContainerStats().getCpu().getUsage().getTotal()) * 100).longValue(); // CPU
                //Long y = cAdvisor.getContainerStats().getNetwork().getTxBytes();

                // Train the model (in memory RAM usage, in Mb)
                Long y = cAdvisor.getContainerStats().getMemory().getUsage() / 1024 / 1024;
                model.refineModel(cAdvisor.getMachineName(), cAdvisor.getContainerName(), epoch, y);

                // Update operator state
                modelState.update(model);

                SimpleRegression simpleRegression = model.getModel(cAdvisor.getMachineName(), cAdvisor.getContainerName());

                out.collect(new Tuple9<>(cAdvisor.getMachineName(), // string
                        cAdvisor.getContainerName(),        // string
                        simpleRegression.getIntercept(),    // double
                        simpleRegression.getSlope(),        // double
                        simpleRegression.getSlopeStdErr(),  // double
                        epoch,                              // long (difference between end and start)
                        y,                                  // long
                        now.getEpochSecond(),               // long
                        Instant.now().getEpochSecond()));   // long
            } catch (DateTimeParseException ex) {

            }
        }

        @Override
        public void open(Configuration config) throws Exception {
            // Obtain key-value state for prediction model
            ValueStateDescriptor<RAMUsagePredictionModel> descriptor = new ValueStateDescriptor<> (
                    "regressionModel", // state name
                    TypeInformation.of(new TypeHint<RAMUsagePredictionModel>() {}),// Type information of state
                    new RAMUsagePredictionModel() // Default value of state
            );

            modelState = getRuntimeContext().getState(descriptor);
        }
    }
}
