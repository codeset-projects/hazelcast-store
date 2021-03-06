package codeset.hazelcast.store.sql;

import codeset.hazelcast.store.NestedPortableClass;
import codeset.hazelcast.store.PortableClass;

import com.hazelcast.nio.serialization.Portable;
import com.hazelcast.nio.serialization.PortableFactory;

/**
 * Create instances of the Portable classes.
 * 
 * @author ingemar.svensson
 *
 */
public class TestPortableFactory implements PortableFactory {

    /**
     * Create an instance of a Portable matching the provided classId.
     */
    @Override
    public Portable create(int classId) {
        if(classId == 1) {
            return new PortableClass();
        } else if(classId == 2) {
            return new NestedPortableClass();
        } else {
            throw new IllegalArgumentException(classId + " unsupported classId");
        }
    }

}
