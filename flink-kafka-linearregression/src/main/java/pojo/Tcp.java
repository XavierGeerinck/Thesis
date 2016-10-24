
package pojo;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@Generated("org.jsonschema2pojo")
public class Tcp {

    @SerializedName("Established")
    @Expose
    private long established;
    @SerializedName("SynSent")
    @Expose
    private long synSent;
    @SerializedName("SynRecv")
    @Expose
    private long synRecv;
    @SerializedName("FinWait1")
    @Expose
    private long finWait1;
    @SerializedName("FinWait2")
    @Expose
    private long finWait2;
    @SerializedName("TimeWait")
    @Expose
    private long timeWait;
    @SerializedName("Close")
    @Expose
    private long close;
    @SerializedName("CloseWait")
    @Expose
    private long closeWait;
    @SerializedName("LastAck")
    @Expose
    private long lastAck;
    @SerializedName("Listen")
    @Expose
    private long listen;
    @SerializedName("Closing")
    @Expose
    private long closing;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Tcp() {
    }

    /**
     * 
     * @param timeWait
     * @param finWait2
     * @param synSent
     * @param finWait1
     * @param established
     * @param closeWait
     * @param listen
     * @param lastAck
     * @param closing
     * @param close
     * @param synRecv
     */
    public Tcp(long established, long synSent, long synRecv, long finWait1, long finWait2, long timeWait, long close, long closeWait, long lastAck, long listen, long closing) {
        this.established = established;
        this.synSent = synSent;
        this.synRecv = synRecv;
        this.finWait1 = finWait1;
        this.finWait2 = finWait2;
        this.timeWait = timeWait;
        this.close = close;
        this.closeWait = closeWait;
        this.lastAck = lastAck;
        this.listen = listen;
        this.closing = closing;
    }

    /**
     * 
     * @return
     *     The established
     */
    public long getEstablished() {
        return established;
    }

    /**
     * 
     * @param established
     *     The Established
     */
    public void setEstablished(long established) {
        this.established = established;
    }

    /**
     * 
     * @return
     *     The synSent
     */
    public long getSynSent() {
        return synSent;
    }

    /**
     * 
     * @param synSent
     *     The SynSent
     */
    public void setSynSent(long synSent) {
        this.synSent = synSent;
    }

    /**
     * 
     * @return
     *     The synRecv
     */
    public long getSynRecv() {
        return synRecv;
    }

    /**
     * 
     * @param synRecv
     *     The SynRecv
     */
    public void setSynRecv(long synRecv) {
        this.synRecv = synRecv;
    }

    /**
     * 
     * @return
     *     The finWait1
     */
    public long getFinWait1() {
        return finWait1;
    }

    /**
     * 
     * @param finWait1
     *     The FinWait1
     */
    public void setFinWait1(long finWait1) {
        this.finWait1 = finWait1;
    }

    /**
     * 
     * @return
     *     The finWait2
     */
    public long getFinWait2() {
        return finWait2;
    }

    /**
     * 
     * @param finWait2
     *     The FinWait2
     */
    public void setFinWait2(long finWait2) {
        this.finWait2 = finWait2;
    }

    /**
     * 
     * @return
     *     The timeWait
     */
    public long getTimeWait() {
        return timeWait;
    }

    /**
     * 
     * @param timeWait
     *     The TimeWait
     */
    public void setTimeWait(long timeWait) {
        this.timeWait = timeWait;
    }

    /**
     * 
     * @return
     *     The close
     */
    public long getClose() {
        return close;
    }

    /**
     * 
     * @param close
     *     The Close
     */
    public void setClose(long close) {
        this.close = close;
    }

    /**
     * 
     * @return
     *     The closeWait
     */
    public long getCloseWait() {
        return closeWait;
    }

    /**
     * 
     * @param closeWait
     *     The CloseWait
     */
    public void setCloseWait(long closeWait) {
        this.closeWait = closeWait;
    }

    /**
     * 
     * @return
     *     The lastAck
     */
    public long getLastAck() {
        return lastAck;
    }

    /**
     * 
     * @param lastAck
     *     The LastAck
     */
    public void setLastAck(long lastAck) {
        this.lastAck = lastAck;
    }

    /**
     * 
     * @return
     *     The listen
     */
    public long getListen() {
        return listen;
    }

    /**
     * 
     * @param listen
     *     The Listen
     */
    public void setListen(long listen) {
        this.listen = listen;
    }

    /**
     * 
     * @return
     *     The closing
     */
    public long getClosing() {
        return closing;
    }

    /**
     * 
     * @param closing
     *     The Closing
     */
    public void setClosing(long closing) {
        this.closing = closing;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(established).append(synSent).append(synRecv).append(finWait1).append(finWait2).append(timeWait).append(close).append(closeWait).append(lastAck).append(listen).append(closing).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Tcp) == false) {
            return false;
        }
        Tcp rhs = ((Tcp) other);
        return new EqualsBuilder().append(established, rhs.established).append(synSent, rhs.synSent).append(synRecv, rhs.synRecv).append(finWait1, rhs.finWait1).append(finWait2, rhs.finWait2).append(timeWait, rhs.timeWait).append(close, rhs.close).append(closeWait, rhs.closeWait).append(lastAck, rhs.lastAck).append(listen, rhs.listen).append(closing, rhs.closing).isEquals();
    }

}
