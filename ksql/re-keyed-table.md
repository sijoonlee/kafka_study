## Build a re-keyed table
- for table lookups: Kafka message key must be same as the contents of the column set in Key
- What if we don't have Key field in KSQL?
    ```
    describe extended weatherraw;

    Name: WEATHERRAW
    Type: STREAM
    Key field:
    Key format: STRING
    Value format: AVRO
    Kafka topic: WEATHERRAW
    ```

- trick: JSON sourced stream -> AVRO stream can be done by creating new stream
create stream weatherraw with (value_format='AVRO') as SELECT city->name AS city_name,
city->country AS city_country, city->latitude AS latitude, city->longitude AS longitude, rain from weather;

- create stream using partition
create stream weatherrekeyed as select * from weatherraw partition by city_name;
-> rowkey is set using city_name

- create a table from it
create table weathernow with (kafka_topic='WEATHERREKEYED', value_format='AVRO', key='CITY_NAME')

