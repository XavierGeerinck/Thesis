
package cadvisor.pojo;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Stats {

    @SerializedName("Async")
    @Expose
    private long async;
    @SerializedName("Read")
    @Expose
    private long read;
    @SerializedName("Sync")
    @Expose
    private long sync;
    @SerializedName("Total")
    @Expose
    private long total;
    @SerializedName("Write")
    @Expose
    private long write;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Stats() {
    }

    /**
     * 
     * @param total
     * @param sync
     * @param write
     * @param read
     * @param async
     */
    public Stats(long async, long read, long sync, long total, long write) {
        this.async = async;
        this.read = read;
        this.sync = sync;
        this.total = total;
        this.write = write;
    }

    /**
     * 
     * @return
     *     The async
     */
    public long getAsync() {
        return async;
    }

    /**
     * 
     * @param async
     *     The Async
     */
    public void setAsync(long async) {
        this.async = async;
    }

    /**
     * 
     * @return
     *     The read
     */
    public long getRead() {
        return read;
    }

    /**
     * 
     * @param read
     *     The Read
     */
    public void setRead(long read) {
        this.read = read;
    }

    /**
     * 
     * @return
     *     The sync
     */
    public long getSync() {
        return sync;
    }

    /**
     * 
     * @param sync
     *     The Sync
     */
    public void setSync(long sync) {
        this.sync = sync;
    }

    /**
     * 
     * @return
     *     The total
     */
    public long getTotal() {
        return total;
    }

    /**
     * 
     * @param total
     *     The Total
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * 
     * @return
     *     The write
     */
    public long getWrite() {
        return write;
    }

    /**
     * 
     * @param write
     *     The Write
     */
    public void setWrite(long write) {
        this.write = write;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(async).append(read).append(sync).append(total).append(write).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Stats) == false) {
            return false;
        }
        Stats rhs = ((Stats) other);
        return new EqualsBuilder().append(async, rhs.async).append(read, rhs.read).append(sync, rhs.sync).append(total, rhs.total).append(write, rhs.write).isEquals();
    }

}
