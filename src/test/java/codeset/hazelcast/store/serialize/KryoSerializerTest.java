package codeset.hazelcast.store.serialize;

import org.junit.Test;

import codeset.hazelcast.store.PortableClass;
import codeset.hazelcast.store.serialize.KryoSerializer;

public class KryoSerializerTest {

    @Test
    public void testSerialization() {

        KryoSerializer serializer = new KryoSerializer();
        PortableClass object = new PortableClass();
        byte[] toResult = serializer.toBytes(object);
        PortableClass fromResult = (PortableClass) serializer.fromBytes(PortableClass.class, toResult);

    }

    @Test
    public void testPerformance() {

        KryoSerializer serializer = new KryoSerializer();
        PortableClass object = new PortableClass();
        // warm up
        byte[] toResult = null;
        for(int i = 0; i < 10; i++) {
            toResult = serializer.toBytes(object);
            Object fromResult = serializer.fromBytes(PortableClass.class, toResult);
        }

        long startTime = System.currentTimeMillis();
        for(int i = 0; i < 1000000; i++) {
            toResult = serializer.toBytes(object);
        }
        System.out.println("toByte() duration " + (System.currentTimeMillis() - startTime) + "ms");

        startTime = System.currentTimeMillis();
        for(int i = 0; i < 1000000; i++) {
            Object fromResult = serializer.fromBytes(PortableClass.class, toResult);
        }
        System.out.println("fromByte() duration " + (System.currentTimeMillis() - startTime) + "ms");

    }
}
