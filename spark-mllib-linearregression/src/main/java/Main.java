import com.google.gson.Gson;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaDoubleRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;
import org.apache.spark.mllib.regression.LinearRegressionModel;
import org.apache.spark.mllib.regression.LinearRegressionWithSGD;
import pojo.CAdvisor;
import scala.Tuple1;
import scala.Tuple2;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Example for LinearRegressionWithSGD.
 */
public class Main {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("JavaLinearRegressionWithSGDExample").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        // $example on$
        // Load and parse the data
//        String path = "src/main/resources/test.txt";
        String path = "src/main/resources/data_kafka_container.txt";
        JavaRDD<String> data = sc.textFile(path);
        JavaRDD<CAdvisor> parsedData = data.map(
            new Function<String, CAdvisor>() {
                public CAdvisor call(String line) throws ParseException {
                    // Parse the JSON to our POJO
                    Gson g = new Gson();
                    CAdvisor cAdvisor = g.fromJson(line, CAdvisor.class);

                    return cAdvisor;

//                    // Example Code
//                    String[] parts = line.split(",");
//                    String[] features = parts[1].split(" ");
//                    double[] v = new double[features.length];
//                    for (int i = 0; i < features.length - 1; i++) {
//                        v[i] = Double.parseDouble(features[i]);
//                    }
//                    return new LabeledPoint(Double.parseDouble(parts[0]), Vectors.dense(v));
                }
            }
        );




        CAdvisor max = parsedData.max(new CAdvisorTimeComparator());

        System.out.println("Max time: " + max.getTimestamp());
                //System.out.println("Min time: " + max.getMin().toString());

        JavaRDD<LabeledPoint> parsedData2 = parsedData.map(new Function<CAdvisor, LabeledPoint>() {
            @Override
            public LabeledPoint call(CAdvisor cAdvisor) throws Exception {
                // Parse the date to a double
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                Date parsedTime =  df.parse(cAdvisor.getTimestamp());

                return new LabeledPoint(cAdvisor.getContainerStats().getMemory().getUsage(), Vectors.dense(parsedTime.getTime()));
            }
        })

        parsedData.cache();

        // Building the model
        int numIterations = 100;
//        double stepSize = 0.00000001;
        double stepSize = 5;
        final LinearRegressionModel model =
                LinearRegressionWithSGD.train(JavaRDD.toRDD(parsedData), numIterations, stepSize);

        // Evaluate model on training examples and compute training error
        JavaRDD<Tuple2<Double, Double>> valuesAndPreds = parsedData.map(
                new Function<LabeledPoint, Tuple2<Double, Double>>() {
                    public Tuple2<Double, Double> call(LabeledPoint point) {
                        double prediction = model.predict(point.features());
                        return new Tuple2<>(prediction, point.label());
                    }
                }
        );
        double MSE = new JavaDoubleRDD(valuesAndPreds.map(
                new Function<Tuple2<Double, Double>, Object>() {
                    public Object call(Tuple2<Double, Double> pair) {
                        return Math.pow(pair._1() - pair._2(), 2.0);
                    }
                }
        ).rdd()).mean();
        System.out.println("training Mean Squared Error = " + MSE);

        // Save and load model
        model.save(sc.sc(), "target/tmp/javaLinearRegressionWithSGDModel");
        LinearRegressionModel sameModel = LinearRegressionModel.load(sc.sc(),
                "target/tmp/javaLinearRegressionWithSGDModel");
        // $example off$

        sc.stop();
    }

    public static class LabeledPointCompare implements Serializable, Comparator<LabeledPoint> {
        @Override
        public int compare(LabeledPoint o1, LabeledPoint o2) {
            if (o1.features().toArray()[0] > o2.features().toArray()[0]) {
                return 1;
            } else if (o1.features().toArray()[0] == o2.features().toArray()[0]) {
                return 0;
            } else {
                return -1;
            }
        }
    }
}