import org.apache.commons.math3.stat.regression.SimpleRegression;

public class RAMUsagePredictionModel {

    SimpleRegression model;

    public RAMUsagePredictionModel() {
        model = new SimpleRegression(false); // Create simple regression that does not include intercept
    }

    public Long predictRamUsage(Long nextTime) {
        double prediction = model.predict(nextTime.doubleValue());

        if (Double.isNaN(prediction)) {
            return -1L;
        } else {
            return Double.valueOf(prediction).longValue();
        }
    }

    /**
     * Refines the RAMUsage Prediction model by adding a data point
     */
    public void refineModel(Long time, Long ramUsage) {
        model.addData(time.doubleValue(), ramUsage.doubleValue());
    }
}
