## Example

### Assumed Setting
- 2 KSQL instances, 3 Kafka Brokers

### configuration
- bootstrap.server=kb1:9092, kb2:9092, kb3:9092
- ksql.service.id=myservice

- *ksql.service.id* is an id representing a cluster that contains 2 KSQL instances


## Demo
- docker-compose -f docker-compose-prod.yml up -d
(2 ksql server, 1 broker)
- ksql-datagen schema=./datagen/userprofile.avro format=json topic USERPROFILE key=userid maxInterval=1000 iterations=10000
- ksql
create stream up_lastseen as
select TIMPSTAMPTOSTRING(rowtime, 'dd/MMM HH:mm:ss') as createtime, firstname
from userprofile;
- kafka-console-consumer --bootstrap-server localhost:9092 --topic UP_LASTSEEN
- docker-compose -f docker-compose-prod.yml stop ksql-server-1
(even if one ksql server stopped, consumer still can see data coming in)
- docker-compose -f docker-compose-prod.yml stop ksql-server-2
(if all two ksql server stopped, consumer can't see data coming)