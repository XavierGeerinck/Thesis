
package cadvisor.pojo;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Cpu {

    @SerializedName("usage")
    @Expose
    private Usage usage;
    @SerializedName("cfs")
    @Expose
    private Cfs cfs;
    @SerializedName("load_average")
    @Expose
    private long loadAverage;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Cpu() {
    }

    /**
     * 
     * @param usage
     * @param loadAverage
     * @param cfs
     */
    public Cpu(Usage usage, Cfs cfs, long loadAverage) {
        this.usage = usage;
        this.cfs = cfs;
        this.loadAverage = loadAverage;
    }

    /**
     * 
     * @return
     *     The usage
     */
    public Usage getUsage() {
        return usage;
    }

    /**
     * 
     * @param usage
     *     The usage
     */
    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    /**
     * 
     * @return
     *     The cfs
     */
    public Cfs getCfs() {
        return cfs;
    }

    /**
     * 
     * @param cfs
     *     The cfs
     */
    public void setCfs(Cfs cfs) {
        this.cfs = cfs;
    }

    /**
     * 
     * @return
     *     The loadAverage
     */
    public long getLoadAverage() {
        return loadAverage;
    }

    /**
     * 
     * @param loadAverage
     *     The load_average
     */
    public void setLoadAverage(long loadAverage) {
        this.loadAverage = loadAverage;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(usage).append(cfs).append(loadAverage).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Cpu) == false) {
            return false;
        }
        Cpu rhs = ((Cpu) other);
        return new EqualsBuilder().append(usage, rhs.usage).append(cfs, rhs.cfs).append(loadAverage, rhs.loadAverage).isEquals();
    }

}
