
package pojo;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Interface {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("rx_bytes")
    @Expose
    private long rxBytes;
    @SerializedName("rx_packets")
    @Expose
    private long rxPackets;
    @SerializedName("rx_errors")
    @Expose
    private long rxErrors;
    @SerializedName("rx_dropped")
    @Expose
    private long rxDropped;
    @SerializedName("tx_bytes")
    @Expose
    private long txBytes;
    @SerializedName("tx_packets")
    @Expose
    private long txPackets;
    @SerializedName("tx_errors")
    @Expose
    private long txErrors;
    @SerializedName("tx_dropped")
    @Expose
    private long txDropped;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Interface() {
    }

    /**
     * 
     * @param rxDropped
     * @param txDropped
     * @param rxErrors
     * @param name
     * @param txPackets
     * @param txBytes
     * @param rxBytes
     * @param txErrors
     * @param rxPackets
     */
    public Interface(String name, long rxBytes, long rxPackets, long rxErrors, long rxDropped, long txBytes, long txPackets, long txErrors, long txDropped) {
        this.name = name;
        this.rxBytes = rxBytes;
        this.rxPackets = rxPackets;
        this.rxErrors = rxErrors;
        this.rxDropped = rxDropped;
        this.txBytes = txBytes;
        this.txPackets = txPackets;
        this.txErrors = txErrors;
        this.txDropped = txDropped;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The rxBytes
     */
    public long getRxBytes() {
        return rxBytes;
    }

    /**
     * 
     * @param rxBytes
     *     The rx_bytes
     */
    public void setRxBytes(long rxBytes) {
        this.rxBytes = rxBytes;
    }

    /**
     * 
     * @return
     *     The rxPackets
     */
    public long getRxPackets() {
        return rxPackets;
    }

    /**
     * 
     * @param rxPackets
     *     The rx_packets
     */
    public void setRxPackets(long rxPackets) {
        this.rxPackets = rxPackets;
    }

    /**
     * 
     * @return
     *     The rxErrors
     */
    public long getRxErrors() {
        return rxErrors;
    }

    /**
     * 
     * @param rxErrors
     *     The rx_errors
     */
    public void setRxErrors(long rxErrors) {
        this.rxErrors = rxErrors;
    }

    /**
     * 
     * @return
     *     The rxDropped
     */
    public long getRxDropped() {
        return rxDropped;
    }

    /**
     * 
     * @param rxDropped
     *     The rx_dropped
     */
    public void setRxDropped(long rxDropped) {
        this.rxDropped = rxDropped;
    }

    /**
     * 
     * @return
     *     The txBytes
     */
    public long getTxBytes() {
        return txBytes;
    }

    /**
     * 
     * @param txBytes
     *     The tx_bytes
     */
    public void setTxBytes(long txBytes) {
        this.txBytes = txBytes;
    }

    /**
     * 
     * @return
     *     The txPackets
     */
    public long getTxPackets() {
        return txPackets;
    }

    /**
     * 
     * @param txPackets
     *     The tx_packets
     */
    public void setTxPackets(long txPackets) {
        this.txPackets = txPackets;
    }

    /**
     * 
     * @return
     *     The txErrors
     */
    public long getTxErrors() {
        return txErrors;
    }

    /**
     * 
     * @param txErrors
     *     The tx_errors
     */
    public void setTxErrors(long txErrors) {
        this.txErrors = txErrors;
    }

    /**
     * 
     * @return
     *     The txDropped
     */
    public long getTxDropped() {
        return txDropped;
    }

    /**
     * 
     * @param txDropped
     *     The tx_dropped
     */
    public void setTxDropped(long txDropped) {
        this.txDropped = txDropped;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(rxBytes).append(rxPackets).append(rxErrors).append(rxDropped).append(txBytes).append(txPackets).append(txErrors).append(txDropped).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Interface) == false) {
            return false;
        }
        Interface rhs = ((Interface) other);
        return new EqualsBuilder().append(name, rhs.name).append(rxBytes, rhs.rxBytes).append(rxPackets, rhs.rxPackets).append(rxErrors, rhs.rxErrors).append(rxDropped, rhs.rxDropped).append(txBytes, rhs.txBytes).append(txPackets, rhs.txPackets).append(txErrors, rhs.txErrors).append(txDropped, rhs.txDropped).isEquals();
    }

}
