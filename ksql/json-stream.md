## Create topic
kafka-topics --zookeeper localhost:2181 --create --partitions 1 --replication-factor 1 --topic USERPROFILE  
  
## Produce data
kafka-console-producer --broker-list localhost:9092 --topic USERPROFILE << EOF  
> {"userid" : 1, "firstname":"joe", "lastname":"doe", "countrycode":"GB", "rating":4.1}  
> EOF  

kafka-console-producer --broker-list localhost:9092 --topic USERPROFILE << EOF  
> {"userid" : 2, "firstname":"joe", "lastname":"black", "countrycode":"CA", "rating":3.1}  
> EOF  

## Check Topics in KSQL
ksql
list topics;  

## Create Stream in KSQL
CREATE STREAM userprofile userid INT, firstname VARCHAR, lastname VARCHAR, countrycode VARCHAR, rating DOUBLE) \
WITH (VALUE_FORMAT = 'JSON', KAFKA_TOPIC = 'USERPROFILE');  

## Check/Query the topic in KSQL
list streams;
describe userprofile;
select firstname, lastname, countrycode, rating from userprofile;

## Generate stream using KSQL datagen
- generate random test data
- generate a stream of CSV, JSOn, AVRO, ...  
ksql-datagen schema =./ksql-course-master/datagen/userprofile.avro format=json topic=USERPROFILE key=userid maxInterval=5000 iterations=10

## KSQL
print 'USERPROFILE' interval 5;
describe userprofile;
select rowtime, firstname from userprofile;
select TIMESTAMPETOSTRING((rowtime, 'dd/MMM HH:mm') as createtime, firstname || ' ' || ucase(lastname) from userprofile;

## Streams from streams - CASE statement
select filename, ucase(lastname), countrycode, rating
    ,case when rating < 2.5 then 'Poor'
          when rating between 2.5 and 4.2 then 'Good'
          else 'Excellent'
    end
from userprofile;

## Streams management
- script using RUN 
    - check ./ksql-course-master/user_profile_pretty.ksql
- drop stream
    - conditional : if exists
    - optional : delete topic
- running streams
    - find active streams
    - show extended details & metrics
    - terminate queries

## Run a script in KSQL
run script './ksql-course-master/user_profile_pretty.ksql';
list streams;
describe extended user_profile_pretty;
select description from user_profile_pretty;

## Terminate a query and Drop a stream
terminate query CSAS_USER_PROFILE_PRETTY_0;
drop stream IF EXISTS user_profile_pretty DELETE TOPIC;
