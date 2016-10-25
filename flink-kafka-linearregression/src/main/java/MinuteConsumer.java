import com.google.gson.Gson;
import com.twitter.chill.Tuple4Serializer;
import org.apache.flink.api.common.functions.*;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.api.windowing.windows.Window;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer08;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer082;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer08;
import org.apache.flink.streaming.util.serialization.SerializationSchema;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;
import org.apache.flink.util.Collector;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.w3c.dom.TypeInfo;
import pojo.CAdvisor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class MinuteConsumer {
    public static void main(String[] args) throws Exception {
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
        props.setProperty("zookeeper.connect", "10.0.7.13:2181"); // Zookeeper default host:port
        props.setProperty("bootstrap.servers", "10.0.7.13:9092"); // Broker default host:port
        props.setProperty("group.id", "myGroup");                 // Consumer group ID
//        props.setProperty("auto.offset.reset", "earliest");       // Always read topic from start

        // Open Kafka Stream
        FlinkKafkaConsumer08<CAdvisor> consumer = new FlinkKafkaConsumer08<>(
                parameterTool.getRequired("topic"), // Get topic name
                new CAdvisorDeserializer(),
                props
        );

        // Assign a timestamp extractor to the consumer
        //consumer.assignTimestampsAndWatermarks(new CAdvisorTimestampExtractor());

        DataStream<CAdvisor> dataStream = env.addSource(consumer);

        // Train Model Stream
        DataStream<Tuple4<String, String, Long, Long>> trainModelStream = dataStream
            .rebalance()
            .assignTimestampsAndWatermarks(new CAdvisorTimestampExtractor())
            .keyBy(CAdvisor.MACHINE_NAME_ID, CAdvisor.CONTAINER_NAME_ID) // Group by machine name and container name
            //.timeWindow(Time.seconds(60))
            .flatMap(new PredictionModel()); // Predict and refine model per machine and container

        // Predict Model Stream

        // Write the training model result (machineName, containerName, timestamp, ramUsage) to a new kafka stream
        trainModelStream.addSink(new FlinkKafkaProducer08<>("ram-usage-data", new Tuple4Serialization(), props));

        // write kafka stream to standard out.
        trainModelStream.print();

        System.out.println(env.getExecutionPlan());

        env.execute("Read from Kafka example");
    }

    public static class Tuple4Serialization implements SerializationSchema<Tuple4<String, String, Long, Long>> {

        @Override
        public byte[] serialize(Tuple4<String, String, Long, Long> element) {
            JSONObject jo = new JSONObject();

            try {
                jo.put("machine_name", element.f0);
                jo.put("container_name", element.f1);
                jo.put("x", element.f2); // timestamp
                jo.put("y", element.f3); // ram
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
    public static class PredictionModel extends RichFlatMapFunction<CAdvisor, Tuple4<String, String, Long, Long>> {
        private transient ValueState<RAMUsagePredictionModel> modelState;

        @Override
        public void flatMap(CAdvisor cAdvisor, Collector<Tuple4<String, String, Long, Long>> out) throws Exception {
            // Fetch operator state
            RAMUsagePredictionModel model = modelState.value();

            // Java does not support time granularity above milliseconds in its date patterns
            // Which is a problem, since Go returns nanoseconds in time.Time
            // https://docs.oracle.com/javase/tutorial/datetime/iso/instant.html
            Instant instant = Instant.parse(cAdvisor.getTimestamp());
            Long epoch = instant.getEpochSecond();

            // Train the model
            model.refineModel(epoch, cAdvisor.getContainerStats().getMemory().getUsage());

            // Update operator state
            modelState.update(model);

            // Emit the used values to train the model
            //Long y = Double.valueOf(((double)cAdvisor.getContainerStats().getCpu().getUsage().getSystem() / cAdvisor.getContainerStats().getCpu().getUsage().getTotal()) * 100).longValue(); // CPU
            //Long y = cAdvisor.getContainerStats().getNetwork().getTxBytes();
            Long y = cAdvisor.getContainerStats().getMemory().getUsage();

            out.collect(new Tuple4<>(cAdvisor.getMachineName(), cAdvisor.getContainerName(), epoch, y));


//
//            // TODO: This stream should only update the model, another stream should be used for predictions
//            // This code is based on: http://dataartisans.github.io/flink-training/exercises/timePrediction.html
//            if (((int)(Math.random() * 10)) == 2) {
//                System.out.printf("Predicting");
//                // Predict next RAM Usage value
//                Long predictedRamUsage = model.predictRamUsage(parsedTime.getTime());
//
//                // Emit prediction
//                out.collect(new Tuple3<>(cAdvisor.getMachineName(), cAdvisor.getContainerName(), predictedRamUsage));
//            } else {
//                // Train the model
//                model.refineModel(parsedTime.getTime(), cAdvisor.getContainerStats().getMemory().getUsage());
//
//                // Update operator state
//                modelState.update(model);
//            }
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
