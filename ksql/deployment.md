## Two modes
- interactive
    - cli, confluent control center, rest api
- headless
    - used for production
    - script only
    - headless KSQL server is not aware of any streams/tables you define in other interactive KSQL sessions

## Headless mode
- Build an application file - all streams defined in a single file
- Shutdown the interactive server
- Start a headless server - pass in application file

## Application file (where-is-bob.ksql)
create stream rr_europe_raw with (kafka_topic='riderequest-europe', value_format='avro');
create stream bob as select * from rr_europe_raw where user='Bob;

## Shutdown interactive server
confluent stop ksql-server

## Start headless server
/{confluent-dir}/bin/ksql-server-start /{confluent-dir}/etc/ksql/ksql-server.properties --queries-file ./where-is-bob.ksql

## Create Topic and Consume
kafka-topics --zookeeper localhost:2181 --list --topic BOB
kafka-avro-console-consumer --bootstrap-server localhost:9092 --topic BOB
