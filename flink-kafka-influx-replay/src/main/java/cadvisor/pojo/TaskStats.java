
package cadvisor.pojo;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class TaskStats {

    @SerializedName("nr_sleeping")
    @Expose
    private long nrSleeping;
    @SerializedName("nr_running")
    @Expose
    private long nrRunning;
    @SerializedName("nr_stopped")
    @Expose
    private long nrStopped;
    @SerializedName("nr_uninterruptible")
    @Expose
    private long nrUninterruptible;
    @SerializedName("nr_io_wait")
    @Expose
    private long nrIoWait;

    /**
     * No args constructor for use in serialization
     * 
     */
    public TaskStats() {
    }

    /**
     * 
     * @param nrStopped
     * @param nrUninterruptible
     * @param nrRunning
     * @param nrSleeping
     * @param nrIoWait
     */
    public TaskStats(long nrSleeping, long nrRunning, long nrStopped, long nrUninterruptible, long nrIoWait) {
        this.nrSleeping = nrSleeping;
        this.nrRunning = nrRunning;
        this.nrStopped = nrStopped;
        this.nrUninterruptible = nrUninterruptible;
        this.nrIoWait = nrIoWait;
    }

    /**
     * 
     * @return
     *     The nrSleeping
     */
    public long getNrSleeping() {
        return nrSleeping;
    }

    /**
     * 
     * @param nrSleeping
     *     The nr_sleeping
     */
    public void setNrSleeping(long nrSleeping) {
        this.nrSleeping = nrSleeping;
    }

    /**
     * 
     * @return
     *     The nrRunning
     */
    public long getNrRunning() {
        return nrRunning;
    }

    /**
     * 
     * @param nrRunning
     *     The nr_running
     */
    public void setNrRunning(long nrRunning) {
        this.nrRunning = nrRunning;
    }

    /**
     * 
     * @return
     *     The nrStopped
     */
    public long getNrStopped() {
        return nrStopped;
    }

    /**
     * 
     * @param nrStopped
     *     The nr_stopped
     */
    public void setNrStopped(long nrStopped) {
        this.nrStopped = nrStopped;
    }

    /**
     * 
     * @return
     *     The nrUninterruptible
     */
    public long getNrUninterruptible() {
        return nrUninterruptible;
    }

    /**
     * 
     * @param nrUninterruptible
     *     The nr_uninterruptible
     */
    public void setNrUninterruptible(long nrUninterruptible) {
        this.nrUninterruptible = nrUninterruptible;
    }

    /**
     * 
     * @return
     *     The nrIoWait
     */
    public long getNrIoWait() {
        return nrIoWait;
    }

    /**
     * 
     * @param nrIoWait
     *     The nr_io_wait
     */
    public void setNrIoWait(long nrIoWait) {
        this.nrIoWait = nrIoWait;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(nrSleeping).append(nrRunning).append(nrStopped).append(nrUninterruptible).append(nrIoWait).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TaskStats) == false) {
            return false;
        }
        TaskStats rhs = ((TaskStats) other);
        return new EqualsBuilder().append(nrSleeping, rhs.nrSleeping).append(nrRunning, rhs.nrRunning).append(nrStopped, rhs.nrStopped).append(nrUninterruptible, rhs.nrUninterruptible).append(nrIoWait, rhs.nrIoWait).isEquals();
    }

}
