import com.dataartisans.data.KeyedDataPoint;
import com.dataartisans.sinks.InfluxDBSink;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer08;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;
import pojo.CAdvisor;

import java.util.Properties;

public class Main {
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
        props.setProperty("zookeeper.connect", "10.0.7.11:2181");     // Zookeeper default host:port
        props.setProperty("bootstrap.servers", "10.0.7.11:9092");     // Broker default host:port
//        props.setProperty("zookeeper.connect", "10.48.98.232:2181");    // Zookeeper default host:port
//        props.setProperty("bootstrap.servers", "10.48.98.232:9092");    // Broker default host:port
        props.setProperty("group.id", "myGroup");                       // Consumer group ID
        props.setProperty("topic", "container_stats");
        //props.setProperty("log.retention.hours", "2");                  // How long to retain the logs before deleting them
//        props.setProperty("auto.offset.reset", "earliest");           // Always read topic from start

        // Open Kafka Stream
        FlinkKafkaConsumer08<CAdvisor> consumer = new FlinkKafkaConsumer08<CAdvisor>(
                parameterTool.getRequired("topic"), // Get topic name
                new CAdvisorDeserializer(),
                props
        );

        // Save data to influxdb
        DataStream<KeyedDataPoint<String>> dataStream = env.addSource(consumer);
        dataStream.addSink(new InfluxDBSink<>("http://10.0.7.11:8086", "root", "root", "cadvisor", "cadvisor"));

        env.execute("Replace CAdvisor messages in InfluxDB");
    }
}
