import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.util.HashMap;

/**
 * Collection of LinearRegression models for the different machines and containers
 * This LinearRegression is of the form a + bx and is written as: y = intercept + slope * x
 */
public class RAMUsagePredictionModel {
    // Create our SimpleRegression models for the machineName and containerName
    private HashMap<String, HashMap<String, SimpleRegression>> models;

    public RAMUsagePredictionModel() {
        this.models = new HashMap<>();
    }

    public SimpleRegression getModel(String machineName, String containerName) {
        this.models.putIfAbsent(machineName, new HashMap<>());
        this.models.get(machineName).putIfAbsent(containerName, new SimpleRegression(true));

        return this.models.get(machineName).get(containerName);
    }

    public Long predictRamUsage(String machineName, String containerName, Long nextTime) {
        SimpleRegression model = getModel(machineName, containerName);
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
    public void refineModel(String machineName, String containerName, Long time, Long ramUsage) {
        SimpleRegression model = getModel(machineName, containerName);
        model.addData(time.doubleValue(), ramUsage.doubleValue());
    }
}
