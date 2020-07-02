# MapValues
- only affect values
- does not change keys
- does not trigger a re-partition
- Both KStreams and KTables
- Java example
    ```
    KStream<byte[], String> upperccased = stream.mapValues(value -> value.toUpperCase());
    ```
# Map
- affects both keys and values
- triggers re-partitions
- for KStreams only