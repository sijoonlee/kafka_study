## set-up
run below command under /ksql-course-master
docker-compose up -d

## postgres setting (postgres-setup.sql)
docker-compose exec postgres psql -U postgres -f /postgres-setup.sql

CREATE TABLE carusers (
    username VARCHAR
  , ref SERIAL PRIMARY KEY
  );

INSERT INTO carusers (username) VALUES ('Alice');
INSERT INTO carusers (username) VALUES ('Bob');
INSERT INTO carusers (username) VALUES ('Charlie');

## check
docker-compose exec postgres psql -U postgres -c "select * from carsusers;"

## ksql
CREATE SOURCE CONNECTOR 'postgres-jdbc-source' WITH(
    "connector.class"='io.confluent.connect.jdbc.JdbcSourceConnector',
    "connection.url"='jdbc:postgresql://postgres:5432/postgres',
    "mode"='incrementing',
    "table.whitelist"='carusers',
    "connection.password"='password',
    "connection.user"='postgres',
    "topic.prefix"='db-',
    "key"='username');
)

print 'db-carusers' from beginning;

## postgres
docker exec -it postgres psql -U postgres -c "INSERT INTO carusers (username) VALUES ('Tester');"
-> data will show up in KSQL automatically

## Data formats
- CSV, JSON, AVRO

## Demo - Handling Customers' Complaints
|Column          |Example Data|Avro Type|KSQL type| 
|----------------|------------|---------|---------|
|customer_name   |Alice       |string   |VARCHAR  |
|complaint_type  |Late Arrival|string   |VARCHAR  |
|trip_cost       |43.10       |float    |DOUBLE   |
|new_customer    |true        |boolean  |BOOLEAN  |

## Demo - CSV
(cmd)
kafka-topics --zookeeper localhost:2181 --create --partition 1 --replication-factor 1 --topic COMPLAINTS_CSV

(ksql)
CREATE STREAM complaints_csv (customer_name VARCHAR, complaint_type VARCHAR, trip_cost DOUBLE, new_customer BOOLEAN) \
WITH (VALUE_FORMAT = 'DELIMITED', KAFKA_TOPIC = 'COMPLAINTS_CSV');

select * from complaints_csv;

(cmd)
kafka-console-producer --broker-list localhost:9092 --topic COMPLIANTS_CSV
Alice,Late Arrival,43.10,true       // NOTE: true is shown as false in KSQL if you use white space after comma
Alice,Bob,Late Arrival,43.10,true   // this is not shown in KSQL

confluent log ksql-server // this is to find error log (Unexpected field count)
confluent log ksql-server -f // will watch tails of logs

## Demo - JSON
(cmd)
kafka-topics --zookeeper localhost:2181 --create --partition 1 --replication-factor 1 --topic COMPLAINTS_JSON

(ksql)
CREATE STREAM complaints_json (customer_name VARCHAR, complaint_type VARCHAR, trip_cost DOUBLE, new_customer BOOLEAN) \
WITH (VALUE_FORMAT = 'JSON', KAFKA_TOPIC = 'COMPLAINTS_JSON');

select * from complaints_json;

(cmd)
kafka-console-producer --broker-list localhost:9092 --topic COMPLIANTS_JSON
{"customer_name":"Alice, Bob", "complaint_type":"Bad driver", "trip_cose":40.22, "new_customer": true}
{"customer_name":"Alice, Bob", "complaint_type":"Bad driver", "trip_cose":40.22, "new_customer": THISISBOOLEAN} // error


## Demo - AVRO
- avro is binary
- when Avro data is read, the schema used
- When writing, it is used to deserialize it

(cmd)
kafka-topics --zookeeper localhost:2181 --create --partition 1 --replication-factor 1 --topic COMPLAINTS_AVRO
kafka-avro-console-producer --broker-list localhost:9092 --topic COMPLAINTS_AVRO \
--property value.schema='
{
    "type":"record",
    "name":"myrecord",
    "fields":[
        {"name":"customer_name", "type":"string"},
        {"name":"customer_type", "type":"string"},
        {"name":"trip_cost", "type":"float"},
        {"name":"new_customer", "type":"boolean"}
    ]
}'
{"customer_name":"Carol", "complaint_type":"Late arrival", "trip_cost":19.60, "new_customer":false}

(ksql)
print 'COMPLAINTS_AVRO' from beginning;
CREATE STREAM complaints_avro with (kafka_topic='COMPLAINTS_AVRO', value_format='AVRO')
select * from complains_avro;

(cmd)
{"customer_name":"Bad data", "complaint_type":"Late arrival", "trip_cost":19.60, "new_customer":SomeWrongData}
--> this will show error as soon as you execute the line (don't even need to check log)

## Schema Evolution with AVRO
- Add a field to our complaints schema: 'number_of_rides'

## Confluent Platform - contro center
confluent local status
confluent local start
--> use web browser: localhost:9021/clusters

## cmd
kafka-avro-console-producer --broker-list localhost:9092 --topic COMPLAINTS_AVRO \
--property value.schema='
{
    "type":"record",
    "name":"myrecord",
    "fields":[
        {"name":"customer_name", "type":"string"},
        {"name":"customer_type", "type":"string"},
        {"name":"trip_cost", "type":"float"},
        {"name":"new_customer", "type":"boolean"},
        {"name":"number_of_rides", "type":"int", "default" : 1}
    ]
}'
{"customer_name":"Jack", "complaint_type":"Late arrival", "trip_cost":29.60, "new_customer":false, "number_of_rides": 3}

## ksql
select * from complaints_avro;
-> new data for Jack still has 4 fields instead of 5 fields 
describe complaints_avro;
-> there is no 'number_of_rides' field
--> this is because ksql is using version 1 of schema

create stream complaints_avro_v2 with (kafka_topic='COMPLAINTS_AVRO', value_format='AVRO'); // now version 2 of schema
describe complaints_avro_v2;
--> there is 'number_of_rides' field now

select * from complaints_avro_v2;
--> old data has 'null' value in the new field
The NULL represents the concept that there was no field (called number-of-rides) when the initial record was created against the first schema.  Adding a default (in this case "1") only applies to *new* records that don't specify a value.  That is, creating a new record against the newer schema


## Nested JSON
- KSQL supports both flat and nested data structures
- STRUCT data type

{
    "city":{
        "name":"Sydeny",
        "country":"AU",
        "latitude":-33.9,
        "longitude":151.2
    },
    "description":"light rain"
    ...
}


KSQL Nested JSON syntax
```
city STRUCT <
    name VARCHAR,
    country VARCHAR,
    latitude DOUBLE,
    longitude DOUBLE
>
```


## Demo
(cmd)
kafka-topics --zookeeper localhost:2181 --create --partitions 1 --replication-factor 1 --topic WEATHERNESTED
cat demo-weather.json | kafka-console-producer --broker-list localhost:9092 --topic WEATHERNESTED

(ksql)
CREATE STREAM weather
(city STRUCT <name VARCHAR, country VARCHAR, latitude DOUBLE, longitude DOUBLE>,
description VARCHAR,
clouds BIGINT,
deg BIGINT,
humidity BIGINT,
pressure DOUBLE, 
rain DOUBLE)
WITH (KAFKA_TOPIC='WEATHERNESTED', VALUE_FORMAT='JSON');


select * from weather;

select city->name AS city_name, rain from weather; // how you access to the nested item