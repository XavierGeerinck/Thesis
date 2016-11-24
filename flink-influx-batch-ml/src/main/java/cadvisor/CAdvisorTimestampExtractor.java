package cadvisor;

import cadvisor.pojo.CAdvisor;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.time.DateTimeException;
import java.time.Instant;


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
