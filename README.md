hazelcast-store
===============
Hazelcast maps keep data in memory. Not news to anyone of course. By default, the data only exists in memory but sometimes you might not be able to hold all the data in your universe in memory or, you might want to be able to shut down the whole cluster without losing the data. For those scenarios and others, you can provide a MapStore implementation. Its responsibility is to provide permanent, disk based persistence.

Our implementations aim to be simple and generic. We favour blob storage where a serialized version of the map values are saved in binary form. If you want a more elaborate, normalized version we recommend your own implementation based on Hibernate or MyBatis for example.

When saving blobs, all data can be saved in one big table, and the assumption is that all access goes through the cache, since all you'd see is binary objects and if you're lucky, a key against them.

There are a few challenges however. As your domain model evolves, previously saved values may no longer be deserializable into newer versions of the classes. One approach to upgrading existing records is to go through all of them and read them using the old class version and re-save them with the new version. Transformation logic can be provided between versions to make this process easier.

Hazelcast does not yet provide access to the raw Portable bytes. So typically de/serialization involves a library like Kryo.

##Implementations

####SqlMapStore
Generic SQL based MapStore which saves map values as blobs in a variety of RDBMS databases.

######User Guide
