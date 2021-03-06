## Merging streams: INSERT INTO
- two or more streams having idential schema
- INSERT INTO a-stream SELECT from b-stream

## Demo - booking app
- one data gen -> stream of Europe requests
ksql-datagen schema=./ksql-course-master/datagen/riderequest-europe.avro format=avro topic=riderequest-europe key=rideid maxInterval=50
- another data gen -> stream of US requests
ksql-datagen schema=./ksql-course-master/datagen/riderequest-america.avro format=avro topic=riderequest-america key=rideid maxInterval=50
- ksql
create stream rr_america_raw with (kafka_topic='riderequest-america', value_format='avro')
create stream rr_europe_raw with (kafka_topic='riderequest-europe', value_format='avro')

create stream rr_world as select 'Europe' as data_source, * from rr_europe_raw;
insert into rr_world select 'America' as data_source, * from rr_america_raw;

## Windows in KSQL
- Tumbling: Fixed-duration time window, no overlaps
- Hopping: Fixed-duration, overlapping
- Session: Not-fixed, based on durations of activity, data separated by gaps of inactivity

## Within windows
- aggregate
- group
- COLLECT_LIST
- TOPK
- WindowStart() / WindowEnd() (start time and end time)

(ksql)
select data_source, city_name, count(*)
from rr_world
window tumbling (size 60 seconds)
group by data_source, city_name;

select data_source, city_name, COLLECT_LIST(user)
from rr_world
window tumbling (size 60 seconds)
group by data_source, city_name;

select TIMESTAMPTOSTRING(WindowStart(), 'HH:mm:ss'),
TIMESTAMPTOSTRING(WindowEnd(), 'HH:mm:ss'),
data_source, TOPK(city_name, 3), count(*)
FROM rr_world
WINDOW TUMBLING (SIZE 1 minute)
GROUP BY data_source;

cf) use WindowStart/WindowEnd intead of WindowStart()/WindowEnd() since confluent 5.5.0
cf) consider TOPKDISTINCT


## GEO_DISTANCE

- GEO_DISTANCE(lat1, long1, lat2, long2, unit)
- Distance between two points
- From and to both specified as (latitude, longitude) points
- unit: kilometers or miles

create stream requested_journey as
select rr.latitude as from_latitude,
rr.longitude as from_longitude,
rr.user,
rr.city_name as city_name,
w.city_country,
w.latitude as to_latitude,
w.longitude as to_longitude,
w.description as weather_description,
w.rain
from rr_world rr
left join weathernow w on rr.city_name = w.city_name;

create stream ridetodest as
select user, GEO_DISTANCE(from_latitude, from_longitude, to_latitude, to_longitude, 'km') as dist
from requested_journey;