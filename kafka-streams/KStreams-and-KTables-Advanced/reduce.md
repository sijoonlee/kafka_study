## KGroupedStream / KGroupedTable - Reduce
- the result type has to be the same as an input
- ex) (int, int) => int
- Java Example
    ```
    KTable<String, Long> aggregatedStream = groupedStream.reduce(
        (aggValue, newValue) -> aggValue + newValue,
        "reduced-stream-store"
    );

    KTable<String, Long> aggregatedTable = groupedTable.reduce(
        (aggValue, newValue) -> aggValue + newValue,
        (aggValue, oldValue) -> aggValue - oldValue,
        "reduced-table-store"
    )
    ```