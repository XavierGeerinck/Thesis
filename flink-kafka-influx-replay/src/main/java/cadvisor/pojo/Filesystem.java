
package cadvisor.pojo;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Filesystem {

    @SerializedName("device")
    @Expose
    private String device;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("capacity")
    @Expose
    private long capacity;
    @SerializedName("usage")
    @Expose
    private long usage;
    @SerializedName("base_usage")
    @Expose
    private long baseUsage;
    @SerializedName("available")
    @Expose
    private long available;
    @SerializedName("has_inodes")
    @Expose
    private boolean hasInodes;
    @SerializedName("inodes")
    @Expose
    private long inodes;
    @SerializedName("inodes_free")
    @Expose
    private long inodesFree;
    @SerializedName("reads_completed")
    @Expose
    private long readsCompleted;
    @SerializedName("reads_merged")
    @Expose
    private long readsMerged;
    @SerializedName("sectors_read")
    @Expose
    private long sectorsRead;
    @SerializedName("read_time")
    @Expose
    private long readTime;
    @SerializedName("writes_completed")
    @Expose
    private long writesCompleted;
    @SerializedName("writes_merged")
    @Expose
    private long writesMerged;
    @SerializedName("sectors_written")
    @Expose
    private long sectorsWritten;
    @SerializedName("write_time")
    @Expose
    private long writeTime;
    @SerializedName("io_in_progress")
    @Expose
    private long ioInProgress;
    @SerializedName("io_time")
    @Expose
    private long ioTime;
    @SerializedName("weighted_io_time")
    @Expose
    private long weightedIoTime;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Filesystem() {
    }

    /**
     * 
     * @param writesMerged
     * @param hasInodes
     * @param writesCompleted
     * @param readTime
     * @param sectorsRead
     * @param ioTime
     * @param readsMerged
     * @param available
     * @param type
     * @param inodesFree
     * @param weightedIoTime
     * @param readsCompleted
     * @param writeTime
     * @param inodes
     * @param device
     * @param capacity
     * @param usage
     * @param sectorsWritten
     * @param ioInProgress
     * @param baseUsage
     */
    public Filesystem(String device, String type, long capacity, long usage, long baseUsage, long available, boolean hasInodes, long inodes, long inodesFree, long readsCompleted, long readsMerged, long sectorsRead, long readTime, long writesCompleted, long writesMerged, long sectorsWritten, long writeTime, long ioInProgress, long ioTime, long weightedIoTime) {
        this.device = device;
        this.type = type;
        this.capacity = capacity;
        this.usage = usage;
        this.baseUsage = baseUsage;
        this.available = available;
        this.hasInodes = hasInodes;
        this.inodes = inodes;
        this.inodesFree = inodesFree;
        this.readsCompleted = readsCompleted;
        this.readsMerged = readsMerged;
        this.sectorsRead = sectorsRead;
        this.readTime = readTime;
        this.writesCompleted = writesCompleted;
        this.writesMerged = writesMerged;
        this.sectorsWritten = sectorsWritten;
        this.writeTime = writeTime;
        this.ioInProgress = ioInProgress;
        this.ioTime = ioTime;
        this.weightedIoTime = weightedIoTime;
    }

    /**
     * 
     * @return
     *     The device
     */
    public String getDevice() {
        return device;
    }

    /**
     * 
     * @param device
     *     The device
     */
    public void setDevice(String device) {
        this.device = device;
    }

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The capacity
     */
    public long getCapacity() {
        return capacity;
    }

    /**
     * 
     * @param capacity
     *     The capacity
     */
    public void setCapacity(long capacity) {
        this.capacity = capacity;
    }

    /**
     * 
     * @return
     *     The usage
     */
    public long getUsage() {
        return usage;
    }

    /**
     * 
     * @param usage
     *     The usage
     */
    public void setUsage(long usage) {
        this.usage = usage;
    }

    /**
     * 
     * @return
     *     The baseUsage
     */
    public long getBaseUsage() {
        return baseUsage;
    }

    /**
     * 
     * @param baseUsage
     *     The base_usage
     */
    public void setBaseUsage(long baseUsage) {
        this.baseUsage = baseUsage;
    }

    /**
     * 
     * @return
     *     The available
     */
    public long getAvailable() {
        return available;
    }

    /**
     * 
     * @param available
     *     The available
     */
    public void setAvailable(long available) {
        this.available = available;
    }

    /**
     * 
     * @return
     *     The hasInodes
     */
    public boolean isHasInodes() {
        return hasInodes;
    }

    /**
     * 
     * @param hasInodes
     *     The has_inodes
     */
    public void setHasInodes(boolean hasInodes) {
        this.hasInodes = hasInodes;
    }

    /**
     * 
     * @return
     *     The inodes
     */
    public long getInodes() {
        return inodes;
    }

    /**
     * 
     * @param inodes
     *     The inodes
     */
    public void setInodes(long inodes) {
        this.inodes = inodes;
    }

    /**
     * 
     * @return
     *     The inodesFree
     */
    public long getInodesFree() {
        return inodesFree;
    }

    /**
     * 
     * @param inodesFree
     *     The inodes_free
     */
    public void setInodesFree(long inodesFree) {
        this.inodesFree = inodesFree;
    }

    /**
     * 
     * @return
     *     The readsCompleted
     */
    public long getReadsCompleted() {
        return readsCompleted;
    }

    /**
     * 
     * @param readsCompleted
     *     The reads_completed
     */
    public void setReadsCompleted(long readsCompleted) {
        this.readsCompleted = readsCompleted;
    }

    /**
     * 
     * @return
     *     The readsMerged
     */
    public long getReadsMerged() {
        return readsMerged;
    }

    /**
     * 
     * @param readsMerged
     *     The reads_merged
     */
    public void setReadsMerged(long readsMerged) {
        this.readsMerged = readsMerged;
    }

    /**
     * 
     * @return
     *     The sectorsRead
     */
    public long getSectorsRead() {
        return sectorsRead;
    }

    /**
     * 
     * @param sectorsRead
     *     The sectors_read
     */
    public void setSectorsRead(long sectorsRead) {
        this.sectorsRead = sectorsRead;
    }

    /**
     * 
     * @return
     *     The readTime
     */
    public long getReadTime() {
        return readTime;
    }

    /**
     * 
     * @param readTime
     *     The read_time
     */
    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }

    /**
     * 
     * @return
     *     The writesCompleted
     */
    public long getWritesCompleted() {
        return writesCompleted;
    }

    /**
     * 
     * @param writesCompleted
     *     The writes_completed
     */
    public void setWritesCompleted(long writesCompleted) {
        this.writesCompleted = writesCompleted;
    }

    /**
     * 
     * @return
     *     The writesMerged
     */
    public long getWritesMerged() {
        return writesMerged;
    }

    /**
     * 
     * @param writesMerged
     *     The writes_merged
     */
    public void setWritesMerged(long writesMerged) {
        this.writesMerged = writesMerged;
    }

    /**
     * 
     * @return
     *     The sectorsWritten
     */
    public long getSectorsWritten() {
        return sectorsWritten;
    }

    /**
     * 
     * @param sectorsWritten
     *     The sectors_written
     */
    public void setSectorsWritten(long sectorsWritten) {
        this.sectorsWritten = sectorsWritten;
    }

    /**
     * 
     * @return
     *     The writeTime
     */
    public long getWriteTime() {
        return writeTime;
    }

    /**
     * 
     * @param writeTime
     *     The write_time
     */
    public void setWriteTime(long writeTime) {
        this.writeTime = writeTime;
    }

    /**
     * 
     * @return
     *     The ioInProgress
     */
    public long getIoInProgress() {
        return ioInProgress;
    }

    /**
     * 
     * @param ioInProgress
     *     The io_in_progress
     */
    public void setIoInProgress(long ioInProgress) {
        this.ioInProgress = ioInProgress;
    }

    /**
     * 
     * @return
     *     The ioTime
     */
    public long getIoTime() {
        return ioTime;
    }

    /**
     * 
     * @param ioTime
     *     The io_time
     */
    public void setIoTime(long ioTime) {
        this.ioTime = ioTime;
    }

    /**
     * 
     * @return
     *     The weightedIoTime
     */
    public long getWeightedIoTime() {
        return weightedIoTime;
    }

    /**
     * 
     * @param weightedIoTime
     *     The weighted_io_time
     */
    public void setWeightedIoTime(long weightedIoTime) {
        this.weightedIoTime = weightedIoTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(device).append(type).append(capacity).append(usage).append(baseUsage).append(available).append(hasInodes).append(inodes).append(inodesFree).append(readsCompleted).append(readsMerged).append(sectorsRead).append(readTime).append(writesCompleted).append(writesMerged).append(sectorsWritten).append(writeTime).append(ioInProgress).append(ioTime).append(weightedIoTime).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Filesystem) == false) {
            return false;
        }
        Filesystem rhs = ((Filesystem) other);
        return new EqualsBuilder().append(device, rhs.device).append(type, rhs.type).append(capacity, rhs.capacity).append(usage, rhs.usage).append(baseUsage, rhs.baseUsage).append(available, rhs.available).append(hasInodes, rhs.hasInodes).append(inodes, rhs.inodes).append(inodesFree, rhs.inodesFree).append(readsCompleted, rhs.readsCompleted).append(readsMerged, rhs.readsMerged).append(sectorsRead, rhs.sectorsRead).append(readTime, rhs.readTime).append(writesCompleted, rhs.writesCompleted).append(writesMerged, rhs.writesMerged).append(sectorsWritten, rhs.sectorsWritten).append(writeTime, rhs.writeTime).append(ioInProgress, rhs.ioInProgress).append(ioTime, rhs.ioTime).append(weightedIoTime, rhs.weightedIoTime).isEquals();
    }

}
