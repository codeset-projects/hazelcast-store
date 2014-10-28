hazelcast-store
===============
Hazelcast maps keep data in memory. Not news to anyone of course. By default, the data only exists in memory but sometimes you might not be able to hold all the data in your universe in memory or, you might want to be able to shut down the whole cluster without losing the data. For those scenarios and others, you can provide a MapStore implementation. Its responsibility is to provide permanent, disk based persistence.

Our implementations aim to be simple and generic. We favour blob storage where a serialized version of the map values are saved in binary form. If you want a more elaborate, normalized version we recommend your own implementation based on Hibernate or MyBatis for example.

When saving blobs, all data can be saved in one big table, and the assumption is that all access goes through the cache, since all you'd see is binary objects and if you're lucky, a key against them.

There are a few challenges however. As your domain model evolves, previously saved values may no longer be deserializable into newer versions of the classes. One approach to upgrading existing records is to go through all of them and read them using the old class version and re-save them with the new version. Transformation logic can be provided between versions to make this process easier.

Hazelcast does not yet provide access to the raw Portable bytes. So typically de/serialization involves a library like Kryo.

##Implementations

####SqlMapStore
A generic SQL based MapStore which saves map values as blobs in a variety of RDBMS databases.

######User Guide
Create the backing database table. You can see an example in /src/main/schema/mysql_schema.sql.
```
CREATE DATABASE <REPLACE WITH YOUR DATABASE NAME>;

CREATE TABLE <REPLACE WITH YOUR DATABASE NAME>.<REPLACE WITH YOUR TABLE NAME> (
  map_key       VARCHAR(256)    PRIMARY KEY,
  class_name    VARCHAR(256)    NOT NULL,
  bytes         BLOB            NULL
)
```
As you can see, it's a very simple table with a column for the key (map_key) and a column for the Java className (class_name) and a column for the bytes. The map_key is the primary key of the table.

Start with replacing the values in the brackets with yours. Then run the script in your target database.

Create an instance of the SqlMapStore for each map you wish to persist. An important assumption is that the values stored in the map are of the same class.
```
Class<?> type = MyPortable.class;
DataSource dataSource = ...;
Statements statements = new MySqlStatements("codeset_db", "hz_mapstore");
SqlMapStore myMapStore = new SqlMapStore(type, dataSource, statements);
```
This might look like a pain, but we wanted to keep everything nicely decoupled to allow for various customizations.

You can provide your own serializer implementation:
```
SqlMapStore myMapStore = new SqlMapStore(type, dataSource, statements, new MyOwnSerializer());
```
Arguments:
* type (Class<?>). This argument specifies the class that is going to be saved in the Map and database. Without this information, it would be a bit difficult to work out what the bytes held in the database should be deserialized into.
* dataSource (javax.sql.DataSource). Any standard implementation will do.
* statements (codeset.hazelcast.store.sql.Statements). The Statements hold the SQL for the MapStore implementation. You can easily provide your own implementation or use one of ours. Each implementation might required different configuration, but typically the database and table are required at least.
* serializer (codeset.hazelcast.store.serialize.Serializer). The Serializer transforms values from objects into bytes and back again. We provide a Kryo based implementation which is very fast.

Configure the MapStore in the config (see the Hazelcast docs for all the options):
```
MapConfig mapConfig = config.getMapConfig("myMap");
MapStoreConfig mapStoreConfig = mapConfig.getMapStoreConfig();
mapStoreConfig.setImplementation(myMapStore);
mapStoreConfig.setEnabled(true);
```
####Todo
* Add support for more database vendors.
* Add class version byte upgrader.
