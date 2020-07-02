# Both takee one record and produces zero,one or more records

# FlatMapValues
- does not change keys
- does not trigger re-partition
- for KStreams only
- Java Example
    ```
    words = sentences.flatMapValues( value -> Arrays.asList(value.split("\\s+"))); // the return has to be list
    ```
    - (alice, alice is nice) -> (alice, alice), (alice, is), (alice, nice)

# FlatMap
- changes keys
- trigger re-partitions
- for KStreams only
