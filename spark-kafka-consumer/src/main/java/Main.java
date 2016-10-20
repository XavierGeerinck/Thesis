import com.google.gson.Gson;
import kafka.Kafka;
import kafka.serializer.StringDecoder;
import org.apache.kafka.common.serialization.StringDeserializer;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.*;
import org.apache.spark.mllib.feature.Normalizer;
import org.apache.spark.mllib.linalg.*;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.regression.StreamingLinearRegressionWithSGD;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.Time;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.Durations;
import org.codehaus.janino.Java;
import pojo.CAdvisor;
import scala.Tuple2;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        // Init Spark
        SparkConf sparkConf = new SparkConf().setAppName("JavaKafkaConnector").setMaster("local");
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(10));

        // Configure Kafka Connection
        Map<String, String> kafkaParams = new HashMap<>();
        kafkaParams.put("metadata.broker.list", "10.48.98.232:9092");
//        kafkaParams.put("key.deserializer", StringDeserializer.class);
//        kafkaParams.put("value.deserializer", StringDeserializer.class);
//        kafkaParams.put("group.id", "exampleGroup");
//        kafkaParams.put("auto.offset.reset", "latest");
//        kafkaParams.put("enable.auto.commit", false);

        Set<String> topics = new HashSet<>();
        topics.add("container_stats");
        JavaPairInputDStream<String, String> messages = KafkaUtils.createDirectStream(
            jssc,
            String.class,
            String.class,
            StringDecoder.class,
            StringDecoder.class,
            kafkaParams,
            topics
        );

        final Long timeStarted = new Date().getTime();

        // Parse the incoming messages to the <getTime(), CAdvisor> mapping
        JavaDStream<LabeledPoint> parsedStream = messages.map(new Function<Tuple2<String,String>, CAdvisor>() {
            @Override
            public CAdvisor call(Tuple2<String, String> stringStringTuple2) throws Exception {
                Gson g = new Gson();
                CAdvisor cAdvisor = g.fromJson(stringStringTuple2._2(), CAdvisor.class);

                return cAdvisor;
            }
        })
        // Filter out the results for the kafka container
        .filter(new Function<CAdvisor, Boolean>() {
            @Override
            public Boolean call(CAdvisor cAdvisor) throws Exception {
                return cAdvisor.getContainerName().equals("kafka.1.d13y4q7ol39r7vk4qftotpsre");
            }
        })
        // Parse to <Memory, [ Time ]> where time is a feature
        .map(new Function<CAdvisor, LabeledPoint>() {
            @Override
            public LabeledPoint call(CAdvisor cAdvisor) throws Exception {
                // Parse time
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                Date parsedTime =  df.parse(cAdvisor.getTimestamp());

                return new LabeledPoint(cAdvisor.getContainerStats().getMemory().getUsage(), Vectors.dense(parsedTime.getTime()));
            }
        });


//        JavaDStream<LabeledPoint> normalizedStream = parsedStream.map(new Function<LabeledPoint, LabeledPoint>() {
//            @Override
//            public LabeledPoint call(LabeledPoint labeledPoint) throws Exception {
//                Normalizer normalizer = new Normalizer();
//                return new LabeledPoint(labeledPoint.label(), normalizer.transform(labeledPoint.features()));
//            }
//        });

        parsedStream.print();
//        normalizedStream.print();

        // Create ML
        int numFeatures = 1; // This is the amount of values in the Vector.dense
        StreamingLinearRegressionWithSGD model = new StreamingLinearRegressionWithSGD()
                .setInitialWeights(Vectors.zeros(numFeatures))
                .setNumIterations(1) // online learning
                .setMiniBatchFraction(1.0); // Use the whole batch

        model.trainOn(parsedStream);

        JavaDStream<Double> predictions = model.predictOn(parsedStream.filter((Function<LabeledPoint, Boolean>) labeledPoint -> {
            int rand = (int)(Math.random() * 10); // Number from 1 - 10
            return rand == 1;
        }).map((Function<LabeledPoint, Vector>) labeledPoint -> labeledPoint.features()));


        predictions.foreachRDD(new VoidFunction2<JavaRDD<Double>, Time>() {
            @Override
            public void call(JavaRDD<Double> result, Time time) throws Exception {
                System.out.println();
            }
        });

        jssc.start();
        jssc.awaitTermination();
    }
}
