## User Defined Functions (UDF)
- KSQL has a programming API for building user defined functions
- UDF - user defined functions
- UDAF - user defined aggregate functions
- these are implemented as custom jars, which copied to the 'ext' dir of KSQL server

## Demo - TAXI_WAIT
- TAXI_WAIT(weather, dist)
- to calculate wait time in minute based upon distance to travel and weather conditions
- write in Java -> compile -> add to KSQL server

## Prerequsite
- JDK, Maven
- **mvn clean package** to produce jar
- KSQL
    - LIST PROPERTIES;
    - find "ksql.extension.dir' property (by default it's 'ext')
- confluent status
- confluent stop ksql-server
- mkdir /{confluent.dir}/ext
- cp target/ksql-udf-taxi-1.0.jar /{confluent.dir}/ext
- confluent start ksql-server
- KSQL
    - list functions;
    - describe function TAXI_WAIT;

## Use
(ksql)
select user, round(dist) as dist, weather_description,
round(TAXI_WAIT(weather_description, dist)) as taxi_wait_min
from ridetodest;

