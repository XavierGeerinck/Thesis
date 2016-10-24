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
import scala.Tuple2;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nutzer on 10/23/2016.
 */
public class BayesNetwork {
    public static void main(String[] args) {
        System.setProperty("hadoop.home.dir", "C:\\hadoop"); // comment out on mac
        SparkConf conf = new SparkConf().setAppName("JavaLinearRegressionWithSGDExample").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(conf);

        // $example on$
        // Load and parse the data
//        String path = "src/main/resources/test.txt";
        String path = "src/main/resources/data_kafka_container.txt";
        JavaRDD<String> data = sc.textFile(path);
        JavaRDD<LabeledPoint> parsedData = data.map(
                new Function<String, LabeledPoint>() {
                    public LabeledPoint call(String line) throws ParseException {
                        // Parse the JSON to our POJO
                        Gson g = new Gson();
                        CAdvisor cAdvisor = g.fromJson(line, CAdvisor.class);

                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                        Date parsedTime =  df.parse(cAdvisor.getTimestamp());

//                    return new Tuple2<>(parsedTime.getTime(), cAdvisor);
                        //System.out.println(parsedTime.getTime() + "," + cAdvisor.getContainerStats().getMemory().getUsage());
                        return new LabeledPoint(cAdvisor.getContainerStats().getMemory().getUsage(), Vectors.dense(parsedTime.getTime()));
                    }
                }
        );

        // Split the data in a training set and a testing set (supervised learning)
        JavaRDD<LabeledPoint>[] splits = parsedData.randomSplit(new double[] { 0.8, 0.2 });
        JavaRDD<LabeledPoint> trainingSet = splits[0].cache();
        JavaRDD<LabeledPoint> testingSet = splits[1].cache();



        // Building the model
        int numIterations = 20;
        double stepSize = 5;

        final LinearRegressionModel model = LinearRegressionWithSGD.train(JavaRDD.toRDD(trainingSet), numIterations, stepSize);

        JavaRDD<Double> prediction = model.predict(testingSet.map(LabeledPoint::features));

        // Evaluate the model and compute training error through Root Mean Squared Error (RMSE)
        // https://en.wikipedia.org/wiki/Root-mean-square_deviation
        //JavaPairRDD predictionAndTarget = prediction.zip(testingSet.map(LabeledPoint::features));
        //double RMSE = Math.sqrt(predictionAndTarget.map())

        // Evaluate model on training examples and compute training error
        JavaRDD<Tuple2<Double, Double>> valuesAndPreds = testingSet.map(
                new Function<LabeledPoint, Tuple2<Double, Double>>() {
                    public Tuple2<Double, Double> call(LabeledPoint point) {
                        double prediction = model.predict(point.features());
                        //System.out.println("" + (point.label() + "," + prediction));
                        //System.out.println("" + (point.label() + "," + point.features()));
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
//
        System.out.println("training Mean Squared Error = " + MSE);




        System.out.println("Printing X,Y, submit in: http://www.alcula.com/calculators/statistics/scatter-plot/");


//        // Original
        parsedData.foreach(new VoidFunction<LabeledPoint>() {
            @Override
            public void call(LabeledPoint labeledPoint) throws Exception {
                // Original
                //System.out.println("" + labeledPoint.features().toArray()[0] + "," + labeledPoint.label());
                System.out.println("" + labeledPoint.features().toArray()[0] + "," + model.predict(Vectors.dense(labeledPoint.features().toArray()[0])));
            }
        });

//        System.out.println("Original Value: 463 851 520");
//        System.out.println("Prediction: " + model.predict(Vectors.dense(1.0)));

        // Save and load model
//        model.save(sc.sc(), "target/tmp/javaLinearRegressionWithSGDModel");
//        LinearRegressionModel sameModel = LinearRegressionModel.load(sc.sc(),
//                "target/tmp/javaLinearRegressionWithSGDModel");
//        // $example off$

        sc.stop();
    }
}
