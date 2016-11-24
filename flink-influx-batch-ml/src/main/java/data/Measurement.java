package data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Measurement {
    private String measurementName; // The name of the measurement

    private Date timestamp;
    private Map<String, Object> columns;
    private Map<String, String> tags;

    public Measurement(String measurementName, Date timestamp, HashMap<String, Object> columns, HashMap<String, String> tags) {
        this.measurementName = measurementName;
        this.columns = new HashMap<>();
        this.timestamp = timestamp;
        this.columns = columns;
        this.tags = tags;
    }

    public Measurement(String measurementName, HashMap<String, Object> columns, HashMap<String, String> tags) {
        this(measurementName, new Date(), columns, tags);
    }

    public String getMeasurementName() {
        return measurementName;
    }

    public long getTimeStampMs() {
        return timestamp.getTime();
    }

    public Map<String, Object> getColumns() {
        return columns;
    }

    public Map<String, String> getTags() {
        return tags;
    }
}
