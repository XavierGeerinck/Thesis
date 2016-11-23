import com.dataartisans.data.Measurement;
import com.dataartisans.sinks.InfluxDBSink;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer08;
import pojo.CAdvisor;

import java.time.Instant;
import java.util.HashMap;
import java.util.Properties;

public class Main {
    private static final String HOST = "10.48.98.232";

    public static void main(String[] args) throws Exception {
        // parse input arguments
        final ParameterTool parameterTool = ParameterTool.fromArgs(args);
//
//        if(parameterTool.getNumberOfParameters() < 4) {
//            System.out.println("Missing parameters!\nUsage: Kafka --topic <topic> " +
//                    "--bootstrap.servers <kafka brokers> --zookeeper.connect <zk quorum> --group.id <some id>");
//            return;
//        }

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.getConfig().setGlobalJobParameters(parameterTool); // make parameters available in the web interface
        env.getConfig().setAutoWatermarkInterval(1000); // Sets the interval of the automatic watermark emission.
        env.enableCheckpointing(5000); // Create a checkpoint every 5 seconds
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

        // Open Kafka Stream
        FlinkKafkaConsumer08<CAdvisor> consumer = new FlinkKafkaConsumer08<CAdvisor>(
                parameterTool.getRequired("topic"), // Get topic name
                new CAdvisorDeserializer(),
                props
        );

        // Save data to influxdb
        /*
         * Interesting datapoints to be saved:
         * cpu_usage_per_cpu
         * cpu_usage_system
         * cpu_usage_total
         * cpu_usage_user
         * fs_limit
         * fs_usage
         * load_average
         * memory_usage
         * memory_working_set
         * rx_bytes
         * rx_errors
         * tx_bytes
         * tx_errors
         */
        DataStream<Measurement> dataStream = env.addSource(consumer)
        .rebalance()
        .assignTimestampsAndWatermarks(new CAdvisorTimestampExtractor())
        .filter((FilterFunction<CAdvisor>) value -> !value.getContainerName().startsWith("/")) // Filter out stuff that starts with a / these are not services and we don't want them
        .map((MapFunction<CAdvisor, Measurement>) cAdvisor -> {
            // Define our fields (can be related to columns)
            HashMap<String, Object> columns = new HashMap<>();

            Instant instant = Instant.parse(cAdvisor.getTimestamp());
            columns.put("time", instant.getEpochSecond());
            columns.put("machine_name", cAdvisor.getMachineName());
            columns.put("service_name", cAdvisor.getContainerLabels().getComDockerSwarmServiceName());
            columns.put("node_id", cAdvisor.getContainerLabels().getComDockerSwarmNodeId());
            columns.put("value", cAdvisor.getContainerStats().getMemory().getUsage());

            // Define our tags (these get indexed and can be used in group_by, ...)
            HashMap<String, String> tags = new HashMap<>();
            tags.put("container_name", cAdvisor.getContainerName());

            return new Measurement("memory_usage", columns, tags);
        });

        dataStream.addSink(new InfluxDBSink("http://" + HOST + ":8086", "root", "root", "cadvisor"));

        env.execute("Replace CAdvisor messages in InfluxDB");
    }
}
