package com.dataartisans.sinks;

import com.dataartisans.data.Measurement;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class InfluxDBSink extends RichSinkFunction<com.dataartisans.data.Measurement> {

    private transient InfluxDB influxDB = null;
    private final String dataBaseName;
    private final String username;
    private final String password;
    private final String host;

    public InfluxDBSink(String host, String username, String password, String databaseName){
        this.host = host;
        this.username = username;
        this.password = password;
        this.dataBaseName = databaseName;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        influxDB = InfluxDBFactory.connect(host, password, username);
        influxDB.createDatabase(dataBaseName);
        influxDB.enableBatch(2000, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public void invoke(Measurement measurement) throws Exception {
        if (measurement.getColumns().isEmpty()) {
            throw new Exception("No columns defined");
        }

        // create the initial point (timestamp and name)
        Point.Builder builder = Point.measurement(measurement.getMeasurementName())
                .time(measurement.getTimeStampMs(), TimeUnit.MILLISECONDS);

        // Add fields
        Iterator it = (measurement.getColumns().entrySet().iterator());

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();

            // Add to builder
            if (pair.getValue() instanceof Number) {
                builder.addField((String)pair.getKey(), (Number)pair.getValue());
            } else if (pair.getValue() instanceof String) {
                builder.addField((String)pair.getKey(), (String)pair.getValue());
            } else if (pair.getValue() instanceof Double) {
                builder.addField((String)pair.getKey(), (Double)pair.getValue());
            } else if (pair.getValue() instanceof Boolean) {
                builder.addField((String)pair.getKey(), (Boolean)pair.getValue());
            } else if (pair.getValue() instanceof Long) {
                builder.addField((String)pair.getKey(), (Long)pair.getValue());
            } else {
                builder.addField((String)pair.getKey(), (Number)pair.getValue());
            }

            it.remove(); // avoids a ConcurrentModificationException
        }

        // Add tags
        Iterator it2 = (measurement.getTags().entrySet().iterator());

        while (it2.hasNext()) {
            Map.Entry pair = (Map.Entry)it2.next();
            builder.tag((String)pair.getKey(), (String)pair.getValue());
            it2.remove(); // avoids a ConcurrentModificationException
        }

        Point p = builder.build();

        influxDB.write(dataBaseName, "autogen", p);
    }
}