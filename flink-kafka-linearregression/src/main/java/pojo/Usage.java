
package pojo;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Usage {

    @SerializedName("total")
    @Expose
    private long total;
    @SerializedName("per_cpu_usage")
    @Expose
    private List<Long> perCpuUsage = new ArrayList<Long>();
    @SerializedName("user")
    @Expose
    private long user;
    @SerializedName("system")
    @Expose
    private long system;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Usage() {
    }

    /**
     * 
     * @param total
     * @param system
     * @param perCpuUsage
     * @param user
     */
    public Usage(long total, List<Long> perCpuUsage, long user, long system) {
        this.total = total;
        this.perCpuUsage = perCpuUsage;
        this.user = user;
        this.system = system;
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
     *     The total
     */
    public void setTotal(long total) {
        this.total = total;
    }

    /**
     * 
     * @return
     *     The perCpuUsage
     */
    public List<Long> getPerCpuUsage() {
        return perCpuUsage;
    }

    /**
     * 
     * @param perCpuUsage
     *     The per_cpu_usage
     */
    public void setPerCpuUsage(List<Long> perCpuUsage) {
        this.perCpuUsage = perCpuUsage;
    }

    /**
     * 
     * @return
     *     The user
     */
    public long getUser() {
        return user;
    }

    /**
     * 
     * @param user
     *     The user
     */
    public void setUser(long user) {
        this.user = user;
    }

    /**
     * 
     * @return
     *     The system
     */
    public long getSystem() {
        return system;
    }

    /**
     * 
     * @param system
     *     The system
     */
    public void setSystem(long system) {
        this.system = system;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(total).append(perCpuUsage).append(user).append(system).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Usage) == false) {
            return false;
        }
        Usage rhs = ((Usage) other);
        return new EqualsBuilder().append(total, rhs.total).append(perCpuUsage, rhs.perCpuUsage).append(user, rhs.user).append(system, rhs.system).isEquals();
    }

}
