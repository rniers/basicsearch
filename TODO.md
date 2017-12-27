TODO:
====

XL:
---

 - [ ] Refactor Document model to minimal sane example
 - [ ] Write efficient InvertedIndex
 - [ ] Ensure index is thread safe and performant
 - [ ] Add cluster state and node discovery (Raft?, gossip?)
 - [ ] Write simple sharding based on consistent hashing algorithm, develop routines for data migration, 
       get rid of master node
 - [ ] Use functional data structures for indexes?

L:
---

 - [ ] Use Finch as http layer
 - [ ] Add TF-IDF score to inverted index
    
M:
---
 - [x] Add docker file for app, and write minimal docker compose setup
 - [ ] Resolve issue with artifact copying from outside the docker context
 - [ ] Check debug and production options for the finatra, update dockerfile in order to use them
 - [ ] Add cluster state 
 
S:
---
 - [ ] Unpack gitHeadCommit and gitHeadCommitDate in sbt buildInfoSettings
 - [ ] Validation for the input flags on startup
 - [ ] Use sbt-native-packager instead of sbt-assembly
 - [ ] Rename root package