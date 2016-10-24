
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
public class Diskio {

    @SerializedName("io_service_bytes")
    @Expose
    private List<IoServiceByte> ioServiceBytes = new ArrayList<IoServiceByte>();
    @SerializedName("io_serviced")
    @Expose
    private List<IoServiced> ioServiced = new ArrayList<IoServiced>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Diskio() {
    }

    /**
     * 
     * @param ioServiced
     * @param ioServiceBytes
     */
    public Diskio(List<IoServiceByte> ioServiceBytes, List<IoServiced> ioServiced) {
        this.ioServiceBytes = ioServiceBytes;
        this.ioServiced = ioServiced;
    }

    /**
     * 
     * @return
     *     The ioServiceBytes
     */
    public List<IoServiceByte> getIoServiceBytes() {
        return ioServiceBytes;
    }

    /**
     * 
     * @param ioServiceBytes
     *     The io_service_bytes
     */
    public void setIoServiceBytes(List<IoServiceByte> ioServiceBytes) {
        this.ioServiceBytes = ioServiceBytes;
    }

    /**
     * 
     * @return
     *     The ioServiced
     */
    public List<IoServiced> getIoServiced() {
        return ioServiced;
    }

    /**
     * 
     * @param ioServiced
     *     The io_serviced
     */
    public void setIoServiced(List<IoServiced> ioServiced) {
        this.ioServiced = ioServiced;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(ioServiceBytes).append(ioServiced).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Diskio) == false) {
            return false;
        }
        Diskio rhs = ((Diskio) other);
        return new EqualsBuilder().append(ioServiceBytes, rhs.ioServiceBytes).append(ioServiced, rhs.ioServiced).isEquals();
    }

}
