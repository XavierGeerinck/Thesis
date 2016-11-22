
package pojo;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Memory {

    @SerializedName("usage")
    @Expose
    private long usage;
    @SerializedName("cache")
    @Expose
    private long cache;
    @SerializedName("rss")
    @Expose
    private long rss;
    @SerializedName("swap")
    @Expose
    private long swap;
    @SerializedName("working_set")
    @Expose
    private long workingSet;
    @SerializedName("failcnt")
    @Expose
    private long failcnt;
    @SerializedName("container_data")
    @Expose
    private ContainerData containerData;
    @SerializedName("hierarchical_data")
    @Expose
    private HierarchicalData hierarchicalData;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Memory() {
    }

    /**
     * 
     * @param failcnt
     * @param hierarchicalData
     * @param cache
     * @param rss
     * @param usage
     * @param workingSet
     * @param containerData
     * @param swap
     */
    public Memory(long usage, long cache, long rss, long swap, long workingSet, long failcnt, ContainerData containerData, HierarchicalData hierarchicalData) {
        this.usage = usage;
        this.cache = cache;
        this.rss = rss;
        this.swap = swap;
        this.workingSet = workingSet;
        this.failcnt = failcnt;
        this.containerData = containerData;
        this.hierarchicalData = hierarchicalData;
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
     *     The cache
     */
    public long getCache() {
        return cache;
    }

    /**
     * 
     * @param cache
     *     The cache
     */
    public void setCache(long cache) {
        this.cache = cache;
    }

    /**
     * 
     * @return
     *     The rss
     */
    public long getRss() {
        return rss;
    }

    /**
     * 
     * @param rss
     *     The rss
     */
    public void setRss(long rss) {
        this.rss = rss;
    }

    /**
     * 
     * @return
     *     The swap
     */
    public long getSwap() {
        return swap;
    }

    /**
     * 
     * @param swap
     *     The swap
     */
    public void setSwap(long swap) {
        this.swap = swap;
    }

    /**
     * 
     * @return
     *     The workingSet
     */
    public long getWorkingSet() {
        return workingSet;
    }

    /**
     * 
     * @param workingSet
     *     The working_set
     */
    public void setWorkingSet(long workingSet) {
        this.workingSet = workingSet;
    }

    /**
     * 
     * @return
     *     The failcnt
     */
    public long getFailcnt() {
        return failcnt;
    }

    /**
     * 
     * @param failcnt
     *     The failcnt
     */
    public void setFailcnt(long failcnt) {
        this.failcnt = failcnt;
    }

    /**
     * 
     * @return
     *     The containerData
     */
    public ContainerData getContainerData() {
        return containerData;
    }

    /**
     * 
     * @param containerData
     *     The container_data
     */
    public void setContainerData(ContainerData containerData) {
        this.containerData = containerData;
    }

    /**
     * 
     * @return
     *     The hierarchicalData
     */
    public HierarchicalData getHierarchicalData() {
        return hierarchicalData;
    }

    /**
     * 
     * @param hierarchicalData
     *     The hierarchical_data
     */
    public void setHierarchicalData(HierarchicalData hierarchicalData) {
        this.hierarchicalData = hierarchicalData;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(usage).append(cache).append(rss).append(swap).append(workingSet).append(failcnt).append(containerData).append(hierarchicalData).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Memory) == false) {
            return false;
        }
        Memory rhs = ((Memory) other);
        return new EqualsBuilder().append(usage, rhs.usage).append(cache, rhs.cache).append(rss, rhs.rss).append(swap, rhs.swap).append(workingSet, rhs.workingSet).append(failcnt, rhs.failcnt).append(containerData, rhs.containerData).append(hierarchicalData, rhs.hierarchicalData).isEquals();
    }

}
