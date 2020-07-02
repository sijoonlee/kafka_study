# Branch
- split a KStream based on one or more predicates
- Predicates are evaluated in order
- if not matches, records are dropped
- it brings multple KStreams
- Java Example
    ```
    KStream<String, Long>[] branches = stream.branch(
        (key, value) -> value > 100,        // first predicate  --> branch[0]
        (key, value) -> value > 10,         // second predicate --> branch[1]
        (key, value) -> value > 0           // third predicate  --> branch[2]
    );
    
    ```
    - 