
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
public class Network {

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
    @SerializedName("interfaces")
    @Expose
    private List<Interface> interfaces = new ArrayList<Interface>();
    @SerializedName("tcp")
    @Expose
    private Tcp tcp;
    @SerializedName("tcp6")
    @Expose
    private Tcp6 tcp6;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Network() {
    }

    /**
     * 
     * @param rxDropped
     * @param txDropped
     * @param rxErrors
     * @param name
     * @param interfaces
     * @param txPackets
     * @param tcp6
     * @param tcp
     * @param txBytes
     * @param rxBytes
     * @param txErrors
     * @param rxPackets
     */
    public Network(String name, long rxBytes, long rxPackets, long rxErrors, long rxDropped, long txBytes, long txPackets, long txErrors, long txDropped, List<Interface> interfaces, Tcp tcp, Tcp6 tcp6) {
        this.name = name;
        this.rxBytes = rxBytes;
        this.rxPackets = rxPackets;
        this.rxErrors = rxErrors;
        this.rxDropped = rxDropped;
        this.txBytes = txBytes;
        this.txPackets = txPackets;
        this.txErrors = txErrors;
        this.txDropped = txDropped;
        this.interfaces = interfaces;
        this.tcp = tcp;
        this.tcp6 = tcp6;
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

    /**
     * 
     * @return
     *     The interfaces
     */
    public List<Interface> getInterfaces() {
        return interfaces;
    }

    /**
     * 
     * @param interfaces
     *     The interfaces
     */
    public void setInterfaces(List<Interface> interfaces) {
        this.interfaces = interfaces;
    }

    /**
     * 
     * @return
     *     The tcp
     */
    public Tcp getTcp() {
        return tcp;
    }

    /**
     * 
     * @param tcp
     *     The tcp
     */
    public void setTcp(Tcp tcp) {
        this.tcp = tcp;
    }

    /**
     * 
     * @return
     *     The tcp6
     */
    public Tcp6 getTcp6() {
        return tcp6;
    }

    /**
     * 
     * @param tcp6
     *     The tcp6
     */
    public void setTcp6(Tcp6 tcp6) {
        this.tcp6 = tcp6;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(name).append(rxBytes).append(rxPackets).append(rxErrors).append(rxDropped).append(txBytes).append(txPackets).append(txErrors).append(txDropped).append(interfaces).append(tcp).append(tcp6).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Network) == false) {
            return false;
        }
        Network rhs = ((Network) other);
        return new EqualsBuilder().append(name, rhs.name).append(rxBytes, rhs.rxBytes).append(rxPackets, rhs.rxPackets).append(rxErrors, rhs.rxErrors).append(rxDropped, rhs.rxDropped).append(txBytes, rhs.txBytes).append(txPackets, rhs.txPackets).append(txErrors, rhs.txErrors).append(txDropped, rhs.txDropped).append(interfaces, rhs.interfaces).append(tcp, rhs.tcp).append(tcp6, rhs.tcp6).isEquals();
    }

}
