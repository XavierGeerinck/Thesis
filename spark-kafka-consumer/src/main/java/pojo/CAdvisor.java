
package pojo;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class CAdvisor {

    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("machine_name")
    @Expose
    private String machineName;
    @SerializedName("container_Name")
    @Expose
    private String containerName;
    @SerializedName("container_Id")
    @Expose
    private String containerId;
    @SerializedName("container_labels")
    @Expose
    private ContainerLabels containerLabels;
    @SerializedName("container_stats")
    @Expose
    private ContainerStats containerStats;

    /**
     * No args constructor for use in serialization
     * 
     */
    public CAdvisor() {
    }

    /**
     * 
     * @param machineName
     * @param timestamp
     * @param containerName
     * @param containerStats
     * @param containerId
     * @param containerLabels
     */
    public CAdvisor(String timestamp, String machineName, String containerName, String containerId, ContainerLabels containerLabels, ContainerStats containerStats) {
        this.timestamp = timestamp;
        this.machineName = machineName;
        this.containerName = containerName;
        this.containerId = containerId;
        this.containerLabels = containerLabels;
        this.containerStats = containerStats;
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
     *     The machineName
     */
    public String getMachineName() {
        return machineName;
    }

    /**
     * 
     * @param machineName
     *     The machine_name
     */
    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    /**
     * 
     * @return
     *     The containerName
     */
    public String getContainerName() {
        return containerName;
    }

    /**
     * 
     * @param containerName
     *     The container_Name
     */
    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    /**
     * 
     * @return
     *     The containerId
     */
    public String getContainerId() {
        return containerId;
    }

    /**
     * 
     * @param containerId
     *     The container_Id
     */
    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    /**
     * 
     * @return
     *     The containerLabels
     */
    public ContainerLabels getContainerLabels() {
        return containerLabels;
    }

    /**
     * 
     * @param containerLabels
     *     The container_labels
     */
    public void setContainerLabels(ContainerLabels containerLabels) {
        this.containerLabels = containerLabels;
    }

    /**
     * 
     * @return
     *     The containerStats
     */
    public ContainerStats getContainerStats() {
        return containerStats;
    }

    /**
     * 
     * @param containerStats
     *     The container_stats
     */
    public void setContainerStats(ContainerStats containerStats) {
        this.containerStats = containerStats;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(timestamp).append(machineName).append(containerName).append(containerId).append(containerLabels).append(containerStats).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CAdvisor) == false) {
            return false;
        }
        CAdvisor rhs = ((CAdvisor) other);
        return new EqualsBuilder().append(timestamp, rhs.timestamp).append(machineName, rhs.machineName).append(containerName, rhs.containerName).append(containerId, rhs.containerId).append(containerLabels, rhs.containerLabels).append(containerStats, rhs.containerStats).isEquals();
    }



}
