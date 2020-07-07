1. Kafka Producer
- outputs ~ 100 messages per second
- outputs transactions evenly for 6 customers
- Each message has random positive value that represents money
- data looks like
    ```
    {"Name":"John", "amount": 123, "time":"2017-01-01T01:01:01"}
    ```

2. Kafka Streams
- takes those transations
- computes the total money(balance) for each person
- update the latest time
- the total moeny is not idempotent
- the latest time is idempotent

    1. Read one topic
    2. GroupByKey
    3. Aggregate
    4. to
