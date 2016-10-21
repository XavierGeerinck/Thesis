
package pojo;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Cfs {

    @SerializedName("periods")
    @Expose
    private long periods;
    @SerializedName("throttled_periods")
    @Expose
    private long throttledPeriods;
    @SerializedName("throttled_time")
    @Expose
    private long throttledTime;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Cfs() {
    }

    /**
     * 
     * @param periods
     * @param throttledPeriods
     * @param throttledTime
     */
    public Cfs(long periods, long throttledPeriods, long throttledTime) {
        this.periods = periods;
        this.throttledPeriods = throttledPeriods;
        this.throttledTime = throttledTime;
    }

    /**
     * 
     * @return
     *     The periods
     */
    public long getPeriods() {
        return periods;
    }

    /**
     * 
     * @param periods
     *     The periods
     */
    public void setPeriods(long periods) {
        this.periods = periods;
    }

    /**
     * 
     * @return
     *     The throttledPeriods
     */
    public long getThrottledPeriods() {
        return throttledPeriods;
    }

    /**
     * 
     * @param throttledPeriods
     *     The throttled_periods
     */
    public void setThrottledPeriods(long throttledPeriods) {
        this.throttledPeriods = throttledPeriods;
    }

    /**
     * 
     * @return
     *     The throttledTime
     */
    public long getThrottledTime() {
        return throttledTime;
    }

    /**
     * 
     * @param throttledTime
     *     The throttled_time
     */
    public void setThrottledTime(long throttledTime) {
        this.throttledTime = throttledTime;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(periods).append(throttledPeriods).append(throttledTime).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Cfs) == false) {
            return false;
        }
        Cfs rhs = ((Cfs) other);
        return new EqualsBuilder().append(periods, rhs.periods).append(throttledPeriods, rhs.throttledPeriods).append(throttledTime, rhs.throttledTime).isEquals();
    }

}
