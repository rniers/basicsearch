# Simple distributed search engine using Scala

A minimal implementation of search engine, using naive in-memory 
inverted index and in-memory document store

## Running

Running with sbt:

```bash
   # run shard node
   sbt server/run 
   
   # run master node
   sbt server/run -Drole=master -Dshards=address1:port1,address2:port2...
```

Running using jar files:
```bash
   # run with artifact
   java -jar server-assembly-0.1.0.jar -Drole=master -Dshards=address1:port1,address2:port2...
```

## Test
Run tests agains all projects:
```bash
    sbt test
```

## Build

Assemble fat jars with for client and server:

```bash
    sbt assembly
```