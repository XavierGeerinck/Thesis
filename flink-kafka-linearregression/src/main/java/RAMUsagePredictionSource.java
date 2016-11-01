import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.time.Instant;

/**
 * This class will create predictions based on an already existing model and return an x, y value
 */
public class RAMUsagePredictionSource implements SourceFunction<Tuple4<String, String, Long, Long>> {
    private Boolean isRunning;
    private MinuteConsumer.PredictionModel model;
    private String machineName;
    private String containerName;

    public RAMUsagePredictionSource(String machineName, String containerName) {
        this.isRunning = true;
        this.machineName = machineName;
        this.containerName = containerName;
    }

    @Override
    public void run(SourceContext<Tuple4<String, String, Long, Long>> sourceContext) throws Exception {
        while (isRunning) {
            try {
                RAMUsagePredictionModel ramUsageModel = model.getModelState().value();

                Instant instant = Instant.now();
                Long epoch = instant.getEpochSecond();
                sourceContext.collect(new Tuple4<>(this.machineName, this.containerName, epoch, ramUsageModel.predictRamUsage(this.machineName, this.containerName, epoch)));
                Thread.sleep(1000);
            } catch (NullPointerException ex) {
                System.out.println("EXCEPTION");
                // This can happen if the modelState is not initialized yet, so we ignore the error
            }
        }
    }

    @Override
    public void cancel() {
        this.isRunning = false;
    }
}
