## Hazelstack



### Introduction

Hazelstack is a project to demo the features of [Hazelcast](http://www.hazelcast.org)
against the public [Stack Exchange data-set.](https://archive.org/details/stackexchange)

The demo currently showcases the following features of Hazelcast where cached data provides faster transaction times than direct access to the Database.

1. Partial Key Queries.
2. Read Through.
3. Write Behind.

### Future Plans

The demo currently uses only the USERS table from the Stack Overflow dump.  I have plans to extend this demo to provide
a fully featured Stack Overflow website that uses Hazelcast as a Cache over the Postgresql Database.  I'd like to have
the entire Stack Exchange data-set in memory in the Hazelcast Cluster, this runs to about 90gb of data, so to achieve
this I'll have to run the cluster in the cloud.  Most likely AWS.

### Getting Started

Hazelstack works with a Postgresql Database to store the Stack Exchange data-set and the Hazelcast cluster
reads from this Database. The Stack Exchange data-set is provided in XML format and you will have to run
a special ruby script to import the XML files into your Postgresql instance.

1. Download the following Stack Overflow files, you'll need the [7zip](http://www.7-zip.org/) utility to unzip.

  [stackoverflow.com-Users.7z](https://archive.org/download/stackexchange/stackoverflow.com-Users.7z)

2. Download and Install [Postgresql](http://www.postgresql.org/download/).  I am running on Mac OS X...

  PostgreSQL 9.3.4 on x86_64-apple-darwin13.1.0, compiled by Apple LLVM version 5.1 (clang-503.0.38) (based on LLVM 3.4svn), 64-bit

3. Install [SO2DB](https://github.com/tessellator/so2db) from Github.  This is a Ruby utility to import the XML files into Postgresql.

### Running the Demo

Once you have Postgresql running with the USERS table installed you are ready to start-up the Hazelcast cluster.

1. There is a simple bootstrap class that will create Hazelcast cluster members.  For the Demo just start one instance
by running ````com.craftedbytes.hazelcast.HazelcastMemberBootstrap````

2. You can run the examples from ````com.craftedbytes.benchmarks.TimedBenchmarks```` by passing in one of 3 arguments (partialKeyQuery, readThrough, writeThrough)


### Worth knowing.

* When the ````HazelcastUserService```` class is initialised it will call ````loadAllKeys```` on the ````UserMapStore````.  This will prime the cache.

* The ````HazelcastUserService```` creates a client connection to the Hazelcast cluster.

* The Hazelcast cluster is configured by the ````hazelcast.xml```` file found in the resources folder.

* You can enable or disable write-behind by setting the ````write-delay-seconds```` attribute in the ````hazelcast.xml```` file.
