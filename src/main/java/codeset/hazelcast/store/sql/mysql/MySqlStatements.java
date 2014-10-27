package codeset.hazelcast.store.sql.mysql;

import codeset.hazelcast.store.sql.Statements;

public class MySqlStatements implements Statements {

    private String storeSql;
    private String deleteSql;
    private String loadSql;
    private String loadAllSql;
    private String loadKeysSql;

    public MySqlStatements(String schema, String tableName) {

        if(schema == null || schema.length() == 0) {
            throw new IllegalArgumentException("Argument 'schema' cannot be null or empty");
        }
        if(tableName == null || tableName.length() == 0) {
            throw new IllegalArgumentException("Argument 'tableName' cannot be null or empty");
        }

        storeSql = String.format("INSERT INTO `%s`.`%s` ("
                + "map_key, class_name, bytes) VALUES (?, ?, ?)",
                schema, tableName);

        loadSql = String.format("SELECT bytes FROM `%s`.`%s` WHERE map_key = ?", schema, tableName);

        loadAllSql = String.format("SELECT map_key, bytes FROM `%s`.`%s` WHERE map_key IN ", schema, tableName);

        deleteSql = String.format("DELETE FROM `%s`.`%s` WHERE map_key = ?", schema, tableName);

        loadKeysSql = String.format("SELECT map_key FROM `%s`.`%s` WHERE class_name = ?", schema, tableName);

    }

    public String getStoreSql() {
        return storeSql;
    }

    public String getStoreAllSql() {
        return storeSql;
    }

    public String getDeleteAllSql() {
        return deleteSql;
    }

    public String getDeleteSql() {
        return deleteSql;
    }

    public String getLoadSql() {
        return loadSql;
    }

    public String getLoadAllSql() {
        return loadAllSql;
    }

    public String getLoadKeysSql() {
        return loadKeysSql;
    }

}
