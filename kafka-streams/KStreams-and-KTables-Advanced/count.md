## KGroupedStream / KGroupedTable - Count
- groupBy/groupByKey() produce KGroupedStream
- 'Count' is the count aggregator of records by grouped key
- if used on KGroupedStream
    - null keys or values are ignored
- if used on KGroupedTable
    - null keys are ignored
    - null values are treated as 'delete' (= tombstones)    