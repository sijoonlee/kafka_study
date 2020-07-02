## You can write any KStream or KTable back to Kafka

## To : Terminal Operation, write the records to a topic
    ```
    stream.to("my-stream-output-topic)
    table.to("my-table-output-topic)
    ```
## Through : Write to a topic and get a stream / table from the topic
    ```
    KStream<String, Long> newStream = stream.though("user-clicks-topic");
    KTable<String, Long> newTable = table.through("my-table-output-topic");
    ```