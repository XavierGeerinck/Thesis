import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.ml.common.LabeledVector;
import org.apache.flink.ml.math.DenseVector;
import org.apache.flink.ml.regression.MultipleLinearRegression;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final String HOST = "10.48.98.232";
    private static final String DB_NAME = "cadvisor";
    private static final String DB_PASS = "root";
    private static final String DB_USER = "root";

    public static void main(String[] args) throws Exception {
        // parse input arguments
        final ParameterTool parameterTool = ParameterTool.fromArgs(args);
//
//        if(parameterTool.getNumberOfParameters() < 4) {
//            System.out.println("Missing parameters!\nUsage: Kafka --topic <topic> " +
//                    "--bootstrap.servers <kafka brokers> --zookeeper.connect <zk quorum> --group.id <some id>");
//            return;
//        }

        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().setGlobalJobParameters(parameterTool); // make parameters available in the web interface
        env.getConfig().setAutoWatermarkInterval(1000); // Sets the interval of the automatic watermark emission.
        // try to restart 60 times with 10 seconds delay (10 Minutes)
//        env.setParallelism(40);

        // configure Kafka consumer
        Properties props = new Properties();
//        props.setProperty("zookeeper.connect", "10.0.7.11:2181");     // Zookeeper default host:port
//        props.setProperty("bootstrap.servers", "10.0.7.11:9092");     // Broker default host:port
        props.setProperty("zookeeper.connect", HOST + ":2181");    // Zookeeper default host:port
        props.setProperty("bootstrap.servers", HOST + ":9092");    // Broker default host:port
        props.setProperty("group.id", "myGroup");                       // Consumer group ID
        props.setProperty("topic", "container_stats");
        //props.setProperty("log.retention.hours", "2");                  // How long to retain the logs before deleting them
        props.setProperty("auto.offset.reset", "largest");           // Always read topic from end

        InfluxDB influxDB = InfluxDBFactory.connect("http://" + HOST + ":8086", DB_USER, DB_PASS);
        influxDB.createDatabase(DB_NAME);

        QueryResult result = influxDB.query(new Query("SELECT * FROM memory_usage ORDER BY time DESC LIMIT 50", DB_NAME));

        MultipleLinearRegression mlr = new MultipleLinearRegression()
                .setIterations(10)
                .setStepsize(0.5)
                .setConvergenceThreshold(0.001);

        // [time, container_name, machine_name, node_id, service_name, value]

        QueryResult.Series s = result.getResults().get(0).getSeries().get(0);
        System.out.println(s.getValues().get(s.getColumns().indexOf("value")));

        //System.out.println(result.getResults().get(0).getSeries().get(0).getColumns());
        // LabeledVector: <predictedValue, [ feature1, feature2, ... ]>
        int valueColumnIndex = s.getColumns().indexOf("value");
        int timeColumnIndex = s.getColumns().indexOf("time");

        List<LabeledVector> trainingSetList = s.getValues().stream().map(r -> new LabeledVector(
                Double.parseDouble(r.get(valueColumnIndex).toString()),
                new DenseVector(new double[] { (double)Instant.parse(r.get(timeColumnIndex).toString()).getEpochSecond() })
        ))
        .collect(Collectors.toList());

        DataSet<LabeledVector> trainingSet = env.fromCollection(trainingSetList);
        mlr.fit(env.fromCollection(trainingSetList));
        //DataSet<Prediction> predictions = mlr.predict()




//        // Create multiple linear regression learner
//        val mlr = MultipleLinearRegression()
//                .setIterations(10)
//                .setStepsize(0.5)
//                .setConvergenceThreshold(0.001)
//
//// Obtain training and testing data set
//        val trainingDS: DataSet[LabeledVector] = ...
//        val testingDS: DataSet[Vector] = ...
//
//// Fit the linear model to the provided data
//        mlr.fit(trainingDS)
//
//// Calculate the predictions for the test data
//        val predictions = mlr.predict(testingDS)


        env.execute("Replace CAdvisor messages in InfluxDBConnector");
    }
}
