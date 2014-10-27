package codeset.hazelcast.store.sql.mysql;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import codeset.hazelcast.store.sql.Statements;

public class MySqlStatementsTest {

    @Test
    public void testStatements() {

        String schema = "test_schema";
        String tableName = "test_table";

        Statements statements = new MySqlStatements(schema, tableName);

        assertEquals("INSERT INTO `test_schema`.`test_table` (map_key, class_name, bytes) VALUES (?, ?, ?)", statements.getStoreSql());
        assertEquals("INSERT INTO `test_schema`.`test_table` (map_key, class_name, bytes) VALUES (?, ?, ?)", statements.getStoreAllSql());
        assertEquals("SELECT bytes FROM `test_schema`.`test_table` WHERE map_key = ?", statements.getLoadSql());
        assertEquals("SELECT map_key, bytes FROM `test_schema`.`test_table` WHERE map_key IN ", statements.getLoadAllSql());
        assertEquals("SELECT map_key FROM `test_schema`.`test_table` WHERE class_name = ?", statements.getLoadKeysSql());
        assertEquals("DELETE FROM `test_schema`.`test_table` WHERE map_key = ?", statements.getDeleteSql());
        assertEquals("DELETE FROM `test_schema`.`test_table` WHERE map_key = ?", statements.getDeleteAllSql());

        try {
            new MySqlStatements(null, tableName);
            fail("Null arg should throw exception");
        } catch(IllegalArgumentException e) {
        }
        try {
            new MySqlStatements("", tableName);
            fail("Null arg should throw exception");
        } catch(IllegalArgumentException e) {
        }
        try {
            new MySqlStatements(schema, null);
            fail("Null arg should throw exception");
        } catch(IllegalArgumentException e) {
        }
        try {
            new MySqlStatements(schema, "");
            fail("Null arg should throw exception");
        } catch(IllegalArgumentException e) {
        }

    }

}