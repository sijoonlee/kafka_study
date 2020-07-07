## KGroupedStream - Aggregate
- four arguements
    - Initializer - setting starting value
    - Adder
    - Serde for the aggregated value
    - State store name (name of the aggregation)
- Java Example
    ```
    KTable<byte[], Long> aggregatedStream = groupedStream.aggregate(
        () -> 0L, // means 0 with type of Long
        (aggKey, newValue, aggValue) -> aggValue + newValue.length(),
        Serdes.Long(),
        "aggregated-stream-store"
    );
    ```

## KGroupedTable - Aggregate
- five arguements
    - Initializer
    - Adder
    - Substractor - this is for tombstone (null value)
    - Serde
    - State Store Name
- Java Example
    ```
    KTable<byte[], Long> aggregatedStream = groupedStream.aggregate(
        () -> 0L,
        (aggKey, newValue, aggValue) -> aggValue + newValue.length(),
        (aggKey, oldValue, aggValue) -> aggValue - oldValue.length(),
        Serdes.Long(),
        "aggregated-table-score"
    )

    ```