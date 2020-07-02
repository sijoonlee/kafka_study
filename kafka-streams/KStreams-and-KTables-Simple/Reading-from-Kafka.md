1. KStream
```
KStream<String, Long> wordCounts = builder.stream(
    Serdes.String(), // key
    Serdes.Long(), // value
    "word-counts-input-topic" // input topic
);
```
2. KTable
```
KTable<String, Long> wordCounts = builder.table(
    Serdes.String(), // key
    Serdes.Long(), // value
    "word-counts-input-topic" // input topic
);
```
3. GlobalKTable
```
GlobalTable<String, Long> wordCounts = builder.globalTable(
    Serdes.String(), // key
    Serdes.Long(), // value
    "word-counts-input-topic" // input topic
)
```