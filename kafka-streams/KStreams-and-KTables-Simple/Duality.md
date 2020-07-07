## KStreams and KTables Duality
- Stream as Table
    - A stream can be considered a changelog of a table, where each data record in the stream captures a state change of the table
- Table as Stream  
    - A table can be considered a shapshot, at a point in time, of the latest value for each key in a stream(a stream's data records are key-value pairs)  


## Transforming a KTable to a KStream
```
KTable<byte[], String> table = ... ;
KStream<byte[], String> stream = table.toStream();
```

## Transforming a KStream to a KTable
- Two ways
    - Chain a groupByKey() and an aggregation (count, aggregate, reduce)
    ```
    KTable<String, Long> table = usersAndColour.groupByKey().count();

    ```
    - Write back to Kafka and read as KTable
    ```
    stream.to("intermediary-topic");
    KTable<String, String> table = builder.table("intermediary-topic");
    ```