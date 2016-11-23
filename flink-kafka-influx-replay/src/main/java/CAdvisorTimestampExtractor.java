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
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;


public class CAdvisorTimestampExtractor extends BoundedOutOfOrdernessTimestampExtractor<CAdvisor> {
    private static final int MAX_EVENT_DELAY = 60; // Max time between events

    public CAdvisorTimestampExtractor() {
        super(Time.seconds(MAX_EVENT_DELAY));
    }

    @Override
    public long extractTimestamp(CAdvisor cAdvisor) {
//        String removedNanoseconds = cAdvisor.getTimestamp().substring(0, cAdvisor.getTimestamp().indexOf('.')) + 'Z';
//        DateTimeFormatter FORMAT_DT = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'").withZone(ZoneOffset.UTC);
//        LocalDateTime ldt = LocalDateTime.from(FORMAT_DT.parse(removedNanoseconds));
//        Instant instant = Instant.from(ldt.atZone(ZoneOffset.UTC));

        Instant instant = null;
        Long epoch = -1L;

        try {
            instant = Instant.parse(cAdvisor.getTimestamp());
            epoch = instant.getEpochSecond();
        } catch (DateTimeException ex) {
            System.out.println(ex.getMessage());
        }

        return epoch;
    }
}
