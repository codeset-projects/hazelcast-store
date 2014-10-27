package codeset.hazelcast.store.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.ByteArrayOutputStream;

/**
 * Serialize and deserialize objects using Kryo.
 * 
 * @author ingemar.svensson
 *
 */
public class KryoSerializer implements Serializer {

    /**
     * Create a pool of Kryo instances.
     */
    private ThreadLocal<Kryo> pool = new ThreadLocal<Kryo>() {
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            return kryo;
        };
    };

    /**
     * Deserialize from bytes into a new object.
     * 
     * @param cls class of the new object.
     * @param bytes the bytes to deserialize into an object.
     * @return a new, populated object.
     */
    public Object fromBytes(Class<?> cls, byte[] bytes) {

        try (Input input = new Input(bytes)) {
            Kryo kryo = pool.get();
            return kryo.readObject(input, cls);
        }

    }

    /**
     * Serialize to bytes from a given object.
     * 
     * @param object the object to read from.
     * @return bytes of the object.
     */
    public byte[] toBytes(Object object) {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try (Output output = new Output(outStream)) {
            Kryo kryo = pool.get();
            kryo.writeObject(output, object);
            output.flush();
        }
        return outStream.toByteArray();

    }

}