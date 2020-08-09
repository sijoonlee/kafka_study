## Introducing Table
- A table in Kafka is the state of 'now'
- A message for a table
    - updates the previous message in the set with the same key
    - add a new message if the same key is not there

## What to do
1. Create "COUNTRYTABLE" using a topic "COUNTRY-CSV"
2. Create "COUNTRYTABLE" using KSQL
    - requirement to specify the KEY
    - how to specify the key within COUNTRY-CSV topic
3. What happens to the COUNTRYTABLE table for
    - update with the same key
    - new message when there is no message with the same key

## Create a topic
kafka-topics --zookeeper localhost:2181 --create --partition 1 --replication-factor 1 --topic COUNTRY-CSV  
kafka-console-producer --broker-list localhost:9092 --topic COUNTRY-CSV --property "parse.key=true" --property "key.seperator=:"  
>AU:AU,Australia  
>IN:IN,India  
>GB:GB,Greate Britain  

## Create a table in KSQL
CREATE TABLE COUNTRYTABLE (countrycode VARCHAR, countryname VARCHAR) WITH (KAFKA_TOPIC='COUNTRY-CSV', VALUE_FORMAT='DELIMITED', KEY='contrycode')  
show  tables;
describe extended COUNTRYTABLE;
select countrycode, countryname from contrytable where countrycode='GB' limit 1;
select countrycode, countryname from contrytable where countrycode='FR';

## Update an item in a Table
(Kafka producer)  
>GB:GB,United Kingdom
(KSQL)  
select countrycode, countryname from contrytable where countrycode='GB' limit 1;