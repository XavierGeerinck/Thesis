import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;
import pojo.CAdvisor;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CAdvisorTimestampExtractor extends BoundedOutOfOrdernessTimestampExtractor<CAdvisor> {
    private static final int MAX_EVENT_DELAY = 60; // Max time between events

    public CAdvisorTimestampExtractor() {
        super(Time.seconds(MAX_EVENT_DELAY));
    }

    @Override
    public long extractTimestamp(CAdvisor cAdvisor) {
        // Parse time
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        Date parsedTime = null;

        try {
            parsedTime = df.parse(cAdvisor.getTimestamp());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return parsedTime.getTime();
    }
}
