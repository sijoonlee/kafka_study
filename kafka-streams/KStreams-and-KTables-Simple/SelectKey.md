# SelectKey
- assign a new key to a record (from old key and value)
- marks the data for re-partitioning
- best practice to isolate that transformation to know exactly where the partitioning happens
- Java Example
    ```
    rekeyed = stream.selectKey( (key, value) -> key.substring(0,1))
    ```
    - (alice, paris) -> (a, paris)
