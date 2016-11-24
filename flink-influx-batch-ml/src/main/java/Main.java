import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.IterativeDataSet;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.ml.common.LabeledVector;
import org.apache.flink.ml.math.DenseVector;
import org.apache.flink.ml.regression.MultipleLinearRegression;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import util.Data;
import util.LinearRegressionData;
import util.Params;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final String HOST = "10.0.7.11";
//    private static final String HOST = "10.48.98.232";
    private static final String DB_NAME = "cadvisor";
    private static final String DB_PASS = "root";
    private static final String DB_USER = "root";

    public static void main(String[] args) throws Exception {
        // parse input arguments
        final ParameterTool params = ParameterTool.fromArgs(args);

        final int iterations = params.getInt("iterations", 10);

        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().setGlobalJobParameters(params); // make parameters available in the web interface
        env.getConfig().setAutoWatermarkInterval(1000); // Sets the interval of the automatic watermark emission.
        // try to restart 60 times with 10 seconds delay (10 Minutes)
//        env.setParallelism(40);

        // configure Kafka consumer
        Properties props = new Properties();
        props.setProperty("zookeeper.connect", HOST + ":2181");    // Zookeeper default host:port
        props.setProperty("bootstrap.servers", HOST + ":9092");    // Broker default host:port
        props.setProperty("group.id", "myGroup");                       // Consumer group ID
        props.setProperty("topic", "container_stats");
        //props.setProperty("log.retention.hours", "2");                  // How long to retain the logs before deleting them
        props.setProperty("auto.offset.reset", "largest");           // Always read topic from end

        InfluxDB influxDB = InfluxDBFactory.connect("http://" + HOST + ":8086", DB_USER, DB_PASS);
        influxDB.createDatabase(DB_NAME);

        QueryResult queryResult = influxDB.query(new Query("SELECT * FROM memory_usage ORDER BY time ASC", DB_NAME));

        // [time, container_name, machine_name, node_id, service_name, value]

        QueryResult.Series s = queryResult.getResults().get(0).getSeries().get(0);
        System.out.println(s.getValues().get(s.getColumns().indexOf("value")));

        //System.out.println(result.getResults().get(0).getSeries().get(0).getColumns());
        // LabeledVector: <predictedValue, [ feature1, feature2, ... ]>
        int valueColumnIndex = s.getColumns().indexOf("value");
        int timeColumnIndex = s.getColumns().indexOf("time");

        long scriptStartTime = Instant.now().getEpochSecond();

        List<Data> trainingSetList = s.getValues().stream().map(r -> new Data(
                (double)Instant.parse(r.get(timeColumnIndex).toString()).getEpochSecond() - scriptStartTime,
                Double.parseDouble(r.get(valueColumnIndex).toString()) / 1024 / 1024
        ))
        .collect(Collectors.toList());

        System.out.println("PRINT");
        System.out.println(trainingSetList);

        DataSet<Data> data = env.fromCollection(trainingSetList);

        // Perform Batch Gradient Descent (https://github.com/apache/flink/blob/master/flink-examples/flink-examples-batch/src/main/java/org/apache/flink/examples/java/ml/LinearRegression.java)
        // get the parameters from elements
        DataSet<Params> parameters = LinearRegressionData.getDefaultParamsDataSet(env);

        // set number of bulk iterations for SGD linear Regression
        IterativeDataSet<Params> loop = parameters.iterate(iterations);

        DataSet<Params> new_parameters = data
                // compute a single step using every sample
                .map(new SubUpdate()).withBroadcastSet(loop, "parameters")
                // sum up all the steps
                .reduce(new UpdateAccumulator())
                // average the steps and update all parameters
                .map(new Update());

        // feed new parameters back into next iteration
        DataSet<Params> result = loop.closeWith(new_parameters);

        // emit result
        if(params.has("output")) {
            result.writeAsText(params.get("output"));
            // execute program
            env.execute("Linear Regression example");
        } else {
            System.out.println("Printing result to stdout. Use --output to specify output path.");
            result.print();
        }

        //env.execute("Replace CAdvisor messages in InfluxDBConnector");
    }

    // *************************************************************************
    //     USER FUNCTIONS
    // *************************************************************************

    /**
     * Compute a single BGD type update for every parameters.
     */
    public static class SubUpdate extends RichMapFunction<Data,Tuple2<Params,Integer>> {

        private Collection<Params> parameters;

        private Params parameter;

        private int count = 1;

        /** Reads the parameters from a broadcast variable into a collection. */
        @Override
        public void open(Configuration parameters) throws Exception {
            this.parameters = getRuntimeContext().getBroadcastVariable("parameters");
        }

        @Override
        public Tuple2<Params, Integer> map(Data in) throws Exception {

            for(Params p : parameters){
                this.parameter = p;
            }

            double theta_0 = parameter.getTheta0() - 0.01*((parameter.getTheta0() + (parameter.getTheta1()*in.x)) - in.y);
            double theta_1 = parameter.getTheta1() - 0.01*(((parameter.getTheta0() + (parameter.getTheta1()*in.x)) - in.y) * in.x);

            return new Tuple2<Params,Integer>(new Params(theta_0,theta_1),count);
        }
    }

    /**
     * Accumulator all the update.
     * */
    public static class UpdateAccumulator implements ReduceFunction<Tuple2<Params, Integer>> {

        @Override
        public Tuple2<Params, Integer> reduce(Tuple2<Params, Integer> val1, Tuple2<Params, Integer> val2) {

            double new_theta0 = val1.f0.getTheta0() + val2.f0.getTheta0();
            double new_theta1 = val1.f0.getTheta1() + val2.f0.getTheta1();
            Params result = new Params(new_theta0,new_theta1);
            return new Tuple2<Params, Integer>( result, val1.f1 + val2.f1);

        }
    }

    /**
     * Compute the final update by average them.
     */
    public static class Update implements MapFunction<Tuple2<Params, Integer>,Params> {

        @Override
        public Params map(Tuple2<Params, Integer> arg0) throws Exception {
            return arg0.f0.div(arg0.f1);
        }
    }
}
