
package cadvisor.pojo;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class ContainerStats {

    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("cpu")
    @Expose
    private Cpu cpu;
    @SerializedName("diskio")
    @Expose
    private Diskio diskio;
    @SerializedName("memory")
    @Expose
    private Memory memory;
    @SerializedName("network")
    @Expose
    private Network network;
    @SerializedName("filesystem")
    @Expose
    private List<Filesystem> filesystem = new ArrayList<Filesystem>();
    @SerializedName("task_stats")
    @Expose
    private TaskStats taskStats;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ContainerStats() {
    }

    /**
     * 
     * @param timestamp
     * @param cpu
     * @param diskio
     * @param taskStats
     * @param filesystem
     * @param network
     * @param memory
     */
    public ContainerStats(String timestamp, Cpu cpu, Diskio diskio, Memory memory, Network network, List<Filesystem> filesystem, TaskStats taskStats) {
        this.timestamp = timestamp;
        this.cpu = cpu;
        this.diskio = diskio;
        this.memory = memory;
        this.network = network;
        this.filesystem = filesystem;
        this.taskStats = taskStats;
    }

    /**
     * 
     * @return
     *     The timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * 
     * @param timestamp
     *     The timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 
     * @return
     *     The cpu
     */
    public Cpu getCpu() {
        return cpu;
    }

    /**
     * 
     * @param cpu
     *     The cpu
     */
    public void setCpu(Cpu cpu) {
        this.cpu = cpu;
    }

    /**
     * 
     * @return
     *     The diskio
     */
    public Diskio getDiskio() {
        return diskio;
    }

    /**
     * 
     * @param diskio
     *     The diskio
     */
    public void setDiskio(Diskio diskio) {
        this.diskio = diskio;
    }

    /**
     * 
     * @return
     *     The memory
     */
    public Memory getMemory() {
        return memory;
    }

    /**
     * 
     * @param memory
     *     The memory
     */
    public void setMemory(Memory memory) {
        this.memory = memory;
    }

    /**
     * 
     * @return
     *     The network
     */
    public Network getNetwork() {
        return network;
    }

    /**
     * 
     * @param network
     *     The network
     */
    public void setNetwork(Network network) {
        this.network = network;
    }

    /**
     * 
     * @return
     *     The filesystem
     */
    public List<Filesystem> getFilesystem() {
        return filesystem;
    }

    /**
     * 
     * @param filesystem
     *     The filesystem
     */
    public void setFilesystem(List<Filesystem> filesystem) {
        this.filesystem = filesystem;
    }

    /**
     * 
     * @return
     *     The taskStats
     */
    public TaskStats getTaskStats() {
        return taskStats;
    }

    /**
     * 
     * @param taskStats
     *     The task_stats
     */
    public void setTaskStats(TaskStats taskStats) {
        this.taskStats = taskStats;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(timestamp).append(cpu).append(diskio).append(memory).append(network).append(filesystem).append(taskStats).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ContainerStats) == false) {
            return false;
        }
        ContainerStats rhs = ((ContainerStats) other);
        return new EqualsBuilder().append(timestamp, rhs.timestamp).append(cpu, rhs.cpu).append(diskio, rhs.diskio).append(memory, rhs.memory).append(network, rhs.network).append(filesystem, rhs.filesystem).append(taskStats, rhs.taskStats).isEquals();
    }

}
