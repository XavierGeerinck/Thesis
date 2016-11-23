
package cadvisor.pojo;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class ContainerLabels {

    @SerializedName("com.docker.swarm.node.id")
    @Expose
    private String comDockerSwarmNodeId;
    @SerializedName("com.docker.swarm.service.id")
    @Expose
    private String comDockerSwarmServiceId;
    @SerializedName("com.docker.swarm.service.name")
    @Expose
    private String comDockerSwarmServiceName;
    @SerializedName("com.docker.swarm.task")
    @Expose
    private String comDockerSwarmTask;
    @SerializedName("com.docker.swarm.task.id")
    @Expose
    private String comDockerSwarmTaskId;
    @SerializedName("com.docker.swarm.task.name")
    @Expose
    private String comDockerSwarmTaskName;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ContainerLabels() {
    }

    /**
     * 
     * @param comDockerSwarmServiceName
     * @param comDockerSwarmServiceId
     * @param comDockerSwarmTask
     * @param comDockerSwarmTaskName
     * @param comDockerSwarmTaskId
     * @param comDockerSwarmNodeId
     */
    public ContainerLabels(String comDockerSwarmNodeId, String comDockerSwarmServiceId, String comDockerSwarmServiceName, String comDockerSwarmTask, String comDockerSwarmTaskId, String comDockerSwarmTaskName) {
        this.comDockerSwarmNodeId = comDockerSwarmNodeId;
        this.comDockerSwarmServiceId = comDockerSwarmServiceId;
        this.comDockerSwarmServiceName = comDockerSwarmServiceName;
        this.comDockerSwarmTask = comDockerSwarmTask;
        this.comDockerSwarmTaskId = comDockerSwarmTaskId;
        this.comDockerSwarmTaskName = comDockerSwarmTaskName;
    }

    /**
     * 
     * @return
     *     The comDockerSwarmNodeId
     */
    public String getComDockerSwarmNodeId() {
        return comDockerSwarmNodeId;
    }

    /**
     * 
     * @param comDockerSwarmNodeId
     *     The com.docker.swarm.node.id
     */
    public void setComDockerSwarmNodeId(String comDockerSwarmNodeId) {
        this.comDockerSwarmNodeId = comDockerSwarmNodeId;
    }

    /**
     * 
     * @return
     *     The comDockerSwarmServiceId
     */
    public String getComDockerSwarmServiceId() {
        return comDockerSwarmServiceId;
    }

    /**
     * 
     * @param comDockerSwarmServiceId
     *     The com.docker.swarm.service.id
     */
    public void setComDockerSwarmServiceId(String comDockerSwarmServiceId) {
        this.comDockerSwarmServiceId = comDockerSwarmServiceId;
    }

    /**
     * 
     * @return
     *     The comDockerSwarmServiceName
     */
    public String getComDockerSwarmServiceName() {
        return comDockerSwarmServiceName;
    }

    /**
     * 
     * @param comDockerSwarmServiceName
     *     The com.docker.swarm.service.name
     */
    public void setComDockerSwarmServiceName(String comDockerSwarmServiceName) {
        this.comDockerSwarmServiceName = comDockerSwarmServiceName;
    }

    /**
     * 
     * @return
     *     The comDockerSwarmTask
     */
    public String getComDockerSwarmTask() {
        return comDockerSwarmTask;
    }

    /**
     * 
     * @param comDockerSwarmTask
     *     The com.docker.swarm.task
     */
    public void setComDockerSwarmTask(String comDockerSwarmTask) {
        this.comDockerSwarmTask = comDockerSwarmTask;
    }

    /**
     * 
     * @return
     *     The comDockerSwarmTaskId
     */
    public String getComDockerSwarmTaskId() {
        return comDockerSwarmTaskId;
    }

    /**
     * 
     * @param comDockerSwarmTaskId
     *     The com.docker.swarm.task.id
     */
    public void setComDockerSwarmTaskId(String comDockerSwarmTaskId) {
        this.comDockerSwarmTaskId = comDockerSwarmTaskId;
    }

    /**
     * 
     * @return
     *     The comDockerSwarmTaskName
     */
    public String getComDockerSwarmTaskName() {
        return comDockerSwarmTaskName;
    }

    /**
     * 
     * @param comDockerSwarmTaskName
     *     The com.docker.swarm.task.name
     */
    public void setComDockerSwarmTaskName(String comDockerSwarmTaskName) {
        this.comDockerSwarmTaskName = comDockerSwarmTaskName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(comDockerSwarmNodeId).append(comDockerSwarmServiceId).append(comDockerSwarmServiceName).append(comDockerSwarmTask).append(comDockerSwarmTaskId).append(comDockerSwarmTaskName).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ContainerLabels) == false) {
            return false;
        }
        ContainerLabels rhs = ((ContainerLabels) other);
        return new EqualsBuilder().append(comDockerSwarmNodeId, rhs.comDockerSwarmNodeId).append(comDockerSwarmServiceId, rhs.comDockerSwarmServiceId).append(comDockerSwarmServiceName, rhs.comDockerSwarmServiceName).append(comDockerSwarmTask, rhs.comDockerSwarmTask).append(comDockerSwarmTaskId, rhs.comDockerSwarmTaskId).append(comDockerSwarmTaskName, rhs.comDockerSwarmTaskName).isEquals();
    }

}
