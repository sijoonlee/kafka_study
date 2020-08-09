## run KSQL server
confluent local services ksql-server start

## use KSQL cli

kafka-topics --zookeeper localhost:2181 --create --partitions 1 --replication-factor 1 --topic USERS
kafka-console-producer --broker-list localhost:9092 --topics USERS
- Bob,ON
- Kate,BC

ksql
- list topics;
- print 'USERS';
- print 'USERS' from beginning;
- print 'USERS' from beginning limit 2;
- print 'USERS' from beginning interval 2;
- create stream users_stream (name VARCHAR, countrycode VARCHAR) WITH (KAFKA_TOPIC='USERS', VALUE_FORMAT='DELIMITED');
- list streams;
- select name, countrycode from users_stream emit changes;
- SET 'auto.offset.reset'='earliest';
- select name, countrycode from users_stream; // you can see all data
- select countrycode, count(*) from users_stream group by countrycode;
- drop stream if exists users_stream delete topic;
- show streams

## stop KSQL server
confluent local services ksql-server stop


