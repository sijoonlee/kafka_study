# Filter 
- does not chage keys/values
- does not trigger re-partition
- For KStreams and KTables
- Java example
    ```
    KStream<String, Long> onlyPositives = stream.filter( (key, value) -> value > 0);
    ```

# FilterNot
- inverse of Filter

