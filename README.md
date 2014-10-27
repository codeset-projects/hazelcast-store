hazelcast-store
===============
Hazelcast maps keep data in memory. Not news to anyone of course. By default, the data only exists in memory, plus a number of copies for safety. The copies will ensure that you don't lose anything, even if a node in your cluster dies. Sometimes, you might not be able to hold all the data in your universe in memory. Sometimes, you might want to be able to shut down the whole cluster without losing the data. For those scenarios and others, you can provide a MapStore implementation.

##Implementations

####SqlMapStore
Generic SQL based MapStore which saves map values as blobs (binary large objects) in a variety of RDBMS databases.

######User Guide
