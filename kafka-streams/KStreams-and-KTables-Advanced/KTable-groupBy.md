## GroupBy
- allows to perform more aggregations within a KTable
- Java Example
    ```
    // Group the table by a new key and key type
    KGroupedTable<String, Integer> groupedTable = table.groupBy(
        (key, value) -> KeyValue.pair(value, value.length()),
        Serdes.String(), // key, note that type was modified
        Serdes.Integer() // value, note that type was modified
    )
    ```