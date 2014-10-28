package codeset.hazelcast.store.sql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Test;

import codeset.hazelcast.store.PortableClass;
import codeset.hazelcast.store.serialize.KryoSerializer;
import codeset.hazelcast.store.serialize.Serializer;
import codeset.hazelcast.store.sql.mysql.MySqlStatements;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;
import com.hazelcast.nio.serialization.ClassDefinition;
import com.hazelcast.nio.serialization.ClassDefinitionBuilder;

public class SqlMapStoreIntegrationTest {

    @Test
    public void testStoreLoadDelete() {

        SqlMapStore mapStore = getMapStore();
        IMap<String, Object> map = getMap();

        String key = UUID.randomUUID().toString();
        PortableClass value = new PortableClass();

        map.put(key, value);
        PortableClass result = (PortableClass) mapStore.load(key);
        assertNotNull(result);
        result = (PortableClass) map.get(key);
        assertNotNull(result);

        value.setStringProperty("UPDATED");
        map.put(key, value);
        result = (PortableClass) mapStore.load(key);
        assertEquals("UPDATED", result.getStringProperty());
        result = (PortableClass) map.get(key);
        assertEquals("UPDATED", result.getStringProperty());

        map.delete(key);
        result = (PortableClass) mapStore.load(key);
        assertNull(result);
        result = (PortableClass) map.get(key);
        assertNull(result);

     }

    @Test
    public void testStoreLoadDeleteAll() {

        SqlMapStore mapStore = getMapStore();
        IMap<String, Object> map = getMap();

        Map<String, Object> values = new HashMap<>();
        Set<String> keys = new HashSet<>();
        int numberOfRecords = 1000;

        for(int i = 0; i < numberOfRecords; i++) {
            String key = UUID.randomUUID().toString();
            keys.add(key);
            PortableClass value = new PortableClass();
            values.put(key, value);
        }
        map.putAll(values);

        Map<String, Object> results = mapStore.loadAll(keys);
        assertEquals(keys.size(), results.size());

        results = map.getAll(keys);
        assertEquals(keys.size(), results.size());

        Set<String> allKeys = mapStore.loadAllKeys();
        for(String key : keys) {
            assertTrue(allKeys.contains(key));
        }

    }

    private IMap<String, Object> getMap() {

        Config config = new Config();

        config.getSerializationConfig().addPortableFactory(1, new TestPortableFactory());

        MapConfig mapConfig = config.getMapConfig("SqlMapStoreTestMap");
        MapStoreConfig mapStoreConfig = new MapStoreConfig();
        mapStoreConfig.setImplementation(getMapStore());
        mapStoreConfig.setEnabled(true);
        mapConfig.setMapStoreConfig(mapStoreConfig);

        ClassDefinitionBuilder nestedPortableClassBuilder = new ClassDefinitionBuilder(1, 2);
        nestedPortableClassBuilder.addLongField("dateProperty");
        nestedPortableClassBuilder.addIntField("intProperty");
        nestedPortableClassBuilder.addLongField("longProperty");
        nestedPortableClassBuilder.addDoubleField("doubleProperty");
        nestedPortableClassBuilder.addUTFField("stringProperty");
        nestedPortableClassBuilder.addBooleanField("_has__stringProperty");
        nestedPortableClassBuilder.addBooleanField("booleanProperty");
        ClassDefinition nestedPortableClassDefinition = nestedPortableClassBuilder.build();
        config.getSerializationConfig().addClassDefinition(nestedPortableClassDefinition);

        ClassDefinitionBuilder portableClassBuilder = new ClassDefinitionBuilder(1, 1);
        portableClassBuilder.addLongField("dateProperty");
        portableClassBuilder.addIntField("intProperty");
        portableClassBuilder.addLongField("longProperty");
        portableClassBuilder.addDoubleField("doubleProperty");
        portableClassBuilder.addUTFField("stringProperty");
        portableClassBuilder.addBooleanField("_has__stringProperty");
        portableClassBuilder.addBooleanField("booleanProperty");
        portableClassBuilder.addPortableField("nestedProperty", nestedPortableClassDefinition);
        portableClassBuilder.addBooleanField("_has__nestedProperty");
        portableClassBuilder.addPortableArrayField("listProperty", nestedPortableClassDefinition);
        portableClassBuilder.addBooleanField("_has__listProperty");
        config.getSerializationConfig().addClassDefinition(portableClassBuilder.build());

        return Hazelcast.newHazelcastInstance(config).getMap("SqlMapStoreTestMap");

    }

    private SqlMapStore getMapStore() {

        String url = "jdbc:mysql://127.0.0.1:3306/codeset?relaxAutoCommit=true&amp;rewriteBatchedStatements=true&amp;autoReconnect=true&amp;useConfigs=maxPerformance";
        String username = "dev";
        String password = "dev";

        int availableProcessors = Runtime.getRuntime().availableProcessors();
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaxTotal(availableProcessors * 10);
        dataSource.setMaxWaitMillis(5000);
        dataSource.setDefaultAutoCommit(true);
        dataSource.setEnableAutoCommitOnReturn(true);

        Class<?> type = PortableClass.class;
        Statements statements = new MySqlStatements("codeset", "hz_mapstore");
        Serializer serializer = new KryoSerializer();

        return new SqlMapStore(type, dataSource, statements, serializer);

    }

}
