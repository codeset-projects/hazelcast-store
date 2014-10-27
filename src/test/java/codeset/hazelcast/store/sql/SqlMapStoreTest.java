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
import codeset.hazelcast.store.sql.SqlMapStore;
import codeset.hazelcast.store.sql.Statements;
import codeset.hazelcast.store.sql.mysql.MySqlStatements;

public class SqlMapStoreTest {

    @Test
    public void testStoreLoadDelete() {

        SqlMapStore mapStore = getMapStore();

        String key = UUID.randomUUID().toString();
        PortableClass value = new PortableClass();

        mapStore.store(key, value);

        PortableClass result = (PortableClass) mapStore.load(key);
        assertNotNull(result);

//        value.setStringProperty("UPDATED");
//        mapStore.store(key, value);
//        result = (PortableClass) mapStore.load(key);
//        assertEquals("UPDATED", result.getStringProperty());

        mapStore.delete(key);
        result = (PortableClass) mapStore.load(key);
        assertNull(result);

    }

    @Test
    public void testStoreLoadDeleteAll() {

        SqlMapStore mapStore = getMapStore();

        Map<String, Object> values = new HashMap<>();
        Set<String> keys = new HashSet<>();
        int numberOfRecords = 1000;

        for(int i = 0; i < numberOfRecords; i++) {
            String key = UUID.randomUUID().toString();
            keys.add(key);
            PortableClass value = new PortableClass();
            values.put(key, value);
        }
        mapStore.storeAll(values);

        Map<String, Object> results = mapStore.loadAll(keys);
        assertEquals(keys.size(), results.size());

        Set<String> allKeys = mapStore.loadAllKeys();
        for(String key : keys) {
            assertTrue(allKeys.contains(key));
        }

        mapStore.deleteAll(keys);
        results = mapStore.loadAll(keys);
        assertEquals(0, results.size());

    }

    public SqlMapStore getMapStore() {

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
