## Learning Resource
https://www.confluent.io/blog/crossing-streams-joins-apache-kafka/

## Definition
- taking a KStream and/or KTable and creating a new KStream or KTable from it

## 4 kinds
| Join operands              | Type     | (Inner) Join  | Left Join     | Outer Join    | Co-partitioning |
| -------------------------- | -------- | ------------- | ------------- | ------------- | --------------- |
| KStream to KStream         | Windowed | Supported     | Supported     | Supported     | Yes             |
| KTable to KTable           | Non-Wind | Supported     | Supported     | Not Supported | Yes             |
| KStream to KTable          | Non-Wind | Supported     | Supported     | Not Supported | Yes             |
| KStream to GlobalKTable    | Non-Wind | Supported     | Supported     | Not Supported | Not needed      |
| KTable to GlobalKTable     | N/A      | Not Supported | Not Supported | Not Supported |                 |
*Windowed* : limited time frame
*Co-partitioning* : there is the same number of partitions on both side of one and the other
    - if different, write back the topics through Kafka before the join - network cost
*GlobalKTable* : with GlobalKTable, any stream can be joined even if they have different number of partitions
    - downside is that it depends on disk - dataset's size should be reasonable