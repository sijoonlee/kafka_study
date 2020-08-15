## Kafka Streams: state stores
- Kafka Streams uses 'state stroes' form streaming processing to store data for stateful operators
- by default, state store utilize a RocksDB db
    - falut-tolerance, local state store recovery
    - storage directory for stateful operations: "ksql.streams.state.dir"

## ksql property - ksql.streams.state.dir
- (ksql) list properties;
- copy the value of ksql.streams.state.dir
- (cmd) cd {ksql.streams.state.dir}
    - currently nothing there
- (ksql) create stream userprofile (userid INT, firstname VARCHAR, lastname VARCHAR, countrycode VARCHAR, rating DOUBLE) WITH (VALUE_FORMAT = 'JSON, KAFKA_TOPIC='USERPROFILE);
- (ksql) select countrycode, count(*) from userprofile group by countrycode
- (cmd) ls -a
    - you will see hidden files that store states for aggregation