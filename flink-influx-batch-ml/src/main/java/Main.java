import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.IterativeDataSet;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.fs.FileSystem;
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

/**
 * Solves the y = theta0 + theta1 * x problem
 * ==> hypothesis h(theta) = theta0 + theta1 * x
 *
 * This uses the cost function: minimize (theta0 and theta1)
 *      for: 1/(2m) sum(h(x^{(i)}) - y^{(i)})^2 (for the sum from i = 1 until m, with m the amount of training samples)
 *      See: https://www.coursera.org/learn/machine-learning/lecture/rkTp3/cost-function
 */
public class Main {
    private static final String HOST = "10.0.7.11";
//    private static final String HOST = "10.48.98.232";
    private static final String DB_NAME = "cadvisor";
    private static final String DB_PASS = "root";
    private static final String DB_USER = "root";
    private static final int ITERATIONS = 1000;

    public static void main(String[] args) throws Exception {
        // parse input arguments
        final ParameterTool params = ParameterTool.fromArgs(args);

        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().setGlobalJobParameters(params); // make parameters available in the web interface
        env.getConfig().setAutoWatermarkInterval(1000); // Sets the interval of the automatic watermark emission.
        // try to restart 60 times with 10 seconds delay (10 Minutes)

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

        // [time, container_name, machine_name, node_id, service_name, value]
        QueryResult queryResult = influxDB.query(new Query("SELECT * FROM memory_usage GROUP BY container_name ORDER BY time ASC", DB_NAME));

        for (QueryResult.Series s : queryResult.getResults().get(0).getSeries()) {
            String containerName = s.getTags().get("container_name");
            System.out.println(containerName);

            runPredictor(env, params, containerName, s);
        }
    }

    public static void runPredictor(ExecutionEnvironment env, ParameterTool params, String containerName, QueryResult.Series s) throws Exception {
        // As stated in the paper below, it is important to normalize the data
        // Note that this is a very important step, as prior to this,
        // the polynomial regression methods would return questionable
        // results since it is documented that the scikit learn’s
        // non-linear regression models assume normally distributed
        // data as the input for feature matrices.
        // http://lnunno.github.io/assets/docs/ml_paper.pdf
        //
        // Also in below link, the extra tip says to normalize the input vector
        // http://blog.datumbox.com/tuning-the-learning-rate-in-gradient-descent/
        //
        // This algorithm is called "Feature Scaling" https://en.wikipedia.org/wiki/Normalization_(statistics)
        // x' = (x - min(x)) / (max(x) - min(x))
        Instant containerStartTime = Instant.parse("2016-11-26T08:39:47.133954993Z");

        // LabeledVector: <predictedValue, [ feature1, feature2, ... ]>
        int valueColumnIndex = s.getColumns().indexOf("value");
        int timeColumnIndex = s.getColumns().indexOf("time");

        List<Data> trainingSetList = s.getValues().stream().map(r -> new Data(
                ((double)Instant.parse(r.get(timeColumnIndex).toString()).getEpochSecond() - containerStartTime.getEpochSecond()),
                Double.parseDouble(r.get(valueColumnIndex).toString()) / 1024 / 1024
        ))
                .collect(Collectors.toList());

        // Normalize data
        double minX = getLowestXValue(trainingSetList);
        double maxX = getHighestXValue(trainingSetList);
        double minY = getLowestYValue(trainingSetList);
        double maxY = getHighestYValue(trainingSetList);

        List<Data> trainingSetListNormalized = trainingSetList.stream().map(i -> new Data(
                (i.x - minX) / (maxX - minX),
                (i.y - minY) / (maxY - minY)
        )).collect(Collectors.toList());

        // Convert to dataset
        DataSet<Data> data = env.fromCollection(trainingSetListNormalized);

        // Perform Batch Gradient Descent (https://github.com/apache/flink/blob/master/flink-examples/flink-examples-batch/src/main/java/org/apache/flink/examples/java/ml/LinearRegression.java)
        // get the parameters from elements
        DataSet<Params> parameters = LinearRegressionData.readParamsDataSetFromFile("models/" + containerName + ".txt", env);

        IterativeDataSet<Params> loop = parameters.iterate(ITERATIONS);

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
        if (params.has("output")) {
            result.writeAsText(params.get("output"));

            // execute program
            env.execute("Linear Regression example");
        } else {
            System.out.println("Printing result to stdout. Use --output to specify output path.");

            // The result is a DataSet<Params> so we can save this to update our model later
            result.print();

            // Save to its own file (parallelism = 1 writes it to 1 file)
            result.writeAsText("models/" + containerName + ".txt", FileSystem.WriteMode.OVERWRITE).setParallelism(1);
        }
    }

    public static Double getHighestXValue(List<Data> list) {
        Optional<Data> maxX = list.stream().max((o1, o2) -> {
            if (o1.x > o2.x) {
                return 1;
            } else if (o1.x == o2.x) {
                return 0;
            } else {
                return -1;
            }
        });

        return maxX.get().x;
    }

    public static Double getLowestXValue(List<Data> list) {
        Optional<Data> maxX = list.stream().max((o1, o2) -> {
            if (o1.x > o2.x) {
                return -1;
            } else if (o1.x == o2.x) {
                return 0;
            } else {
                return 1;
            }
        });

        return maxX.get().x;
    }

    public static Double getLowestYValue(List<Data> list) {
        Optional<Data> maxY = list.stream().max((o1, o2) -> {
            if (o1.y > o2.y) {
                return -1;
            } else if (o1.y == o2.y) {
                return 0;
            } else {
                return 1;
            }
        });

        return maxY.get().y;
    }

    public static Double getHighestYValue(List<Data> list) {
        Optional<Data> maxY = list.stream().max((o1, o2) -> {
            if (o1.y > o2.y) {
                return 1;
            } else if (o1.y == o2.y) {
                return 0;
            } else {
                return -1;
            }
        });

        return maxY.get().y;
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

            for (Params p : parameters){
                this.parameter = p;
            }

            double theta_0 = parameter.getTheta0() - 0.01*((parameter.getTheta0() + (parameter.getTheta1()*in.x)) - in.y);
            double theta_1 = parameter.getTheta1() - 0.01*(((parameter.getTheta0() + (parameter.getTheta1()*in.x)) - in.y) * in.x);

            return new Tuple2<>(new Params(theta_0, theta_1), count);
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
            return new Tuple2<>(result, val1.f1 + val2.f1);

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
