## Requirement for Joins
- co-partitioned for join fields


## Joining two streams
- topic: DRIVER_PROFILE, partitons: 2
kafka-topics --zookeeper localhost:2181 --create --partitions 2 - replication-factor 1 --topic DRIVER_PROFILE
kafka-console-producer --broker-list localhost:9092 --topic DRIVER_PROFILE
{"driver_name":"Jone", "countrycode":"AU", "rating":2.4}

(ksql)
CREATE STREAM DRIVER_PROFILE (driver_name VARCHAR, countrycode VARCHAR, rating DOUBLE)
WITH (VALUE_FORMAT = 'JSON', KAFKA_TOPIC='DRIVER_PROFILE');

select dp.driver_name, ct.countryname, dp.rating
from DRIVER_PROFILE dp
left join COUNTRYTABLE ct on ct.countrycode=dp.countrycode;
-> error message: can't join since the number of partitions doesn't match

## New stream repartitioned
create stream driverprofile_rekeyed with (partitions=1) as select * from DRIVER_PROFILE partition by driver_name;

select dp.driver_name, ct.countryname, dp.rating
from DRIVERPROFILE_REKEYED dp
left join COUNTRYTABLE ct on ct.countrycode=dp.countrycode;

