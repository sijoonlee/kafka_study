## Joins
- the result of KSQL join is a new strea or table
- Streams and tables can be joined
    - stream + stream -> stream
    - table + table -> table
    - stream + table -> stream

## Prerequsite
- co-partitioned for join fields
- Table
    - KEY: VARCHAR or STRINg
    - The Kafka message key must be the same as the contents of the column set in KEY

## KSQL
select up.firstname, up.lastname, up.countrycode, ct.countryname
>from USERPROFILE up
>left join COUNTRYTABLE ct on ct.contrycode=up.countrycode;

create stream up_joined as
>select up.firstname
>|| ' ' || ucase(up.lastname)
>|| ' from ' || ct.countryname
>|| ' has a rating of ' || cast(rating as varchar) || ' stars. ' as description
>from USERPROFILE up
>left join COUNTRYTABLE ct on ct.contrycode=up.countrycode;

describe up_joined;  
describe extended up_joined;  
