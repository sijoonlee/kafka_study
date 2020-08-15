## KSQL configuration settings
- server level
    - file: ksql-server.properties
    - dir: {confluent-dir}/etc/ksql
- session level (effective only in that session)
    - '--config-file' argument
    - SET command
- LIST PROPERTIES
    - show properties

## DEMO
- confluent destory
- open file, ksql-server.properties
- add a line: ksql.service.id=myservicename
- confluent start ksql-server
- (ksql) LIST PROPERTIES;
    - you can see ksql.service.id is 'myservicename', and it's SERVER level
- (ksql) SET 'auto.offset.reset'='earliest'
- (ksql) LIST PROPERTIES;
    - you can see auto.offset.reset is set as 'earliest' and it's SESSON level
