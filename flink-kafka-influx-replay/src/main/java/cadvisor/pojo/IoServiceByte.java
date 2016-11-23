
package cadvisor.pojo;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class IoServiceByte {

    @SerializedName("major")
    @Expose
    private long major;
    @SerializedName("minor")
    @Expose
    private long minor;
    @SerializedName("stats")
    @Expose
    private Stats stats;

    /**
     * No args constructor for use in serialization
     * 
     */
    public IoServiceByte() {
    }

    /**
     * 
     * @param minor
     * @param stats
     * @param major
     */
    public IoServiceByte(long major, long minor, Stats stats) {
        this.major = major;
        this.minor = minor;
        this.stats = stats;
    }

    /**
     * 
     * @return
     *     The major
     */
    public long getMajor() {
        return major;
    }

    /**
     * 
     * @param major
     *     The major
     */
    public void setMajor(long major) {
        this.major = major;
    }

    /**
     * 
     * @return
     *     The minor
     */
    public long getMinor() {
        return minor;
    }

    /**
     * 
     * @param minor
     *     The minor
     */
    public void setMinor(long minor) {
        this.minor = minor;
    }

    /**
     * 
     * @return
     *     The stats
     */
    public Stats getStats() {
        return stats;
    }

    /**
     * 
     * @param stats
     *     The stats
     */
    public void setStats(Stats stats) {
        this.stats = stats;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(major).append(minor).append(stats).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof IoServiceByte) == false) {
            return false;
        }
        IoServiceByte rhs = ((IoServiceByte) other);
        return new EqualsBuilder().append(major, rhs.major).append(minor, rhs.minor).append(stats, rhs.stats).isEquals();
    }

}
