# default setting
- keys are hashed using the "murmur2" algorithm
- targetPartition = Utils.abs(Utils.murmur2(record.key())) % numPartitions;
- It is not recommended to override the default behaviour of the partitioner ( partitioner.class )