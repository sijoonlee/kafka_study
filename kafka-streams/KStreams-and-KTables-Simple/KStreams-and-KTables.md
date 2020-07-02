# KStreams
- All inserts
- Similar to a log
- Infinite
- Unbounded data streams
- used when new data is partial information/transactional

```
input           Kstream
(alice, 1)      (alice, 1)
(marc, 4)       (alice, 1), (marc, 4)
(alice, 2)      (alice, 1), (marc, 4), (alice, 2)
```

# KTables
- All upserts on non-null values
- Deletes on null values
- Similar to a table
- Parallel with log compacted topics
- used when every update is self-sufficient
```
(alice, 1)      (alice, 1)
(marc, 4)       (alice, 1), (marc, 4)
(alice, 2)      (alice, 2), (marc, 4)       // alice updated
(marc, null)    (alice, 2)                  // marc deleted
```

# Stateless vs Stateful Operations
- Stateless
    - the result of a transformation only depends on the data-point you process
    - ex) multiply by 2, 1 -> 2, 1 -> 2
- Stateful
    - the result of a transformation also depends on an external information
    - ex) count, X -> 1, X -> 2

