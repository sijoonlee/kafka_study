## KStream - Peek
- Peek allows to apply a side-effect operation to a KStream and get the same KStream as a result
- A side effect could be
    - printing the stream to the console
    - statistics collection
- Warning: it could be executed multiple times as it is side-effect (in case of failures)
- Java Example
    ```
    KStream<byte[], String> stream = ...;
    Kstream<byte[], String> unmodifiedStream = stream.peek(
        (key, value) -> System.out.println("key=" + key + ", value=" + value);
    )
    ```
