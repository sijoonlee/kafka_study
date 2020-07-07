## outline
- User Purchase (KStream)
- User Data (GlobalKTable)

## Topology
1. Read one topic from Kafka (KStream)
2. Read the other topic from Kafka (GlobalKTable)
3. Inner Join
4. Write to Kafka the join result
5. Left Join
6. Write to Kafka the join result