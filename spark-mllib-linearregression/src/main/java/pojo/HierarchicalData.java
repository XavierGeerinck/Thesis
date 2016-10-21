
package pojo;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class HierarchicalData {

    @SerializedName("pgfault")
    @Expose
    private long pgfault;
    @SerializedName("pgmajfault")
    @Expose
    private long pgmajfault;

    /**
     * No args constructor for use in serialization
     * 
     */
    public HierarchicalData() {
    }

    /**
     * 
     * @param pgmajfault
     * @param pgfault
     */
    public HierarchicalData(long pgfault, long pgmajfault) {
        this.pgfault = pgfault;
        this.pgmajfault = pgmajfault;
    }

    /**
     * 
     * @return
     *     The pgfault
     */
    public long getPgfault() {
        return pgfault;
    }

    /**
     * 
     * @param pgfault
     *     The pgfault
     */
    public void setPgfault(long pgfault) {
        this.pgfault = pgfault;
    }

    /**
     * 
     * @return
     *     The pgmajfault
     */
    public long getPgmajfault() {
        return pgmajfault;
    }

    /**
     * 
     * @param pgmajfault
     *     The pgmajfault
     */
    public void setPgmajfault(long pgmajfault) {
        this.pgmajfault = pgmajfault;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(pgfault).append(pgmajfault).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof HierarchicalData) == false) {
            return false;
        }
        HierarchicalData rhs = ((HierarchicalData) other);
        return new EqualsBuilder().append(pgfault, rhs.pgfault).append(pgmajfault, rhs.pgmajfault).isEquals();
    }

}
