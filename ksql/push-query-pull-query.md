## Push queries
- constantly query & output results
- until
    1. user terminates
    2. it exceed LIMIT condition
- KSQL 5.3 and earlier
    select name, countrycode from users_stream;
- KSQL on ksqlDB 5.4 and later - 'emit changes' needed
    select name, countrycode from users_stream emit changes;

## Pull queries
- current state
- returns a result and terminates
- since 5.4
- KSQL currently only supports pull queries on aggregate tables
- must query against rowkey
    ```
    select * from country where rowkey = 'AU'
    ```

## Example
(ksql)
CREATE STREAM driverLocations (driverId INTEGER, countrycode VARCHAR, city VARCHAR, dirverName VARCHAR)
> WITH (kafka_topic='driverlocations', key='dirverId', value_format='json', partition=1);

INSERT INTO driverLocations (driverId, countrycode, city, drivername) VALUES (1, 'AU', 'Sydney', 'Alice');
INSERT INTO driverLocations (driverId, countrycode, city, drivername) VALUES (2, 'AU', 'Melbourne', 'Bob');
INSERT INTO driverLocations (driverId, countrycode, city, drivername) VALUES (3, 'GB', 'London', 'Carole');
INSERT INTO driverLocations (driverId, countrycode, city, drivername) VALUES (4, 'US', 'New York', 'Joe');

select * from driverLocations emit changes;

create table countryDrivers as select countrycode, count(*) as numDrivers from driverLocations group by countrycode;
select countrycode, numdrivers from contryDrivers where rowkey='AU'; // pull query

INSERT INTO driverLocations (driverId, countrycode, city, drivername) VALUES (5, 'AU', 'Sydney', 'Joe');
select countrycode, numdrivers from contryDrivers where rowkey='AU'; // pull query