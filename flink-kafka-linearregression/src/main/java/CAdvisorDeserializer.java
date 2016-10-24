import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.TypeExtractor;
import org.apache.flink.streaming.util.serialization.DeserializationSchema;
import org.apache.flink.streaming.util.serialization.SerializationSchema;
import pojo.CAdvisor;

import java.io.IOException;

public class CAdvisorDeserializer implements DeserializationSchema<CAdvisor>, SerializationSchema<CAdvisor> {
    public CAdvisorDeserializer() {
    }

    @Override
    public CAdvisor deserialize(byte[] message) throws IOException {
        Gson g = new Gson();
        return g.fromJson(new String(message), CAdvisor.class);
    }

    @Override
    public boolean isEndOfStream(CAdvisor cAdvisor) {
        return false;
    }

    @Override
    public TypeInformation<CAdvisor> getProducedType() {
        return TypeExtractor.getForClass(CAdvisor.class);
    }

    @Override
    public byte[] serialize(CAdvisor cAdvisor) {
        Gson gson = new Gson();
        return gson.toJson(cAdvisor).getBytes();
    }
}
