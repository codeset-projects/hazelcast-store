package codeset.hazelcast.store.serialize;

/**
 * Serialize to and from objects into byte blobs.
 * 
 * @author ingemar.svensson
 *
 */
public interface Serializer {

    Object fromBytes(Class<?> cls, byte[] bytes);

    byte[] toBytes(final Object object);

}
