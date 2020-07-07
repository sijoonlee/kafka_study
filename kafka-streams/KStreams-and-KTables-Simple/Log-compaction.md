## Log Compaction
- Log Compaction can be a huge improvement in performance when dealing with KTables, since records get discarded eventually
- less reads to get to the final state
- log compaction has to be enabled by you on the topics

## Log Cleanup Policy: Compact
- Log compaction ensures that your log contains as least the last known value for a specific key within a partition
- Very useful if we just require a SNAPSHOT instead of full history (ex data table in database)
- The idea is that we only keep the latest 'update' for a key in the log
- Any consumer that is reading from the head of a log will still see all the messages sent to the topic
- Ordering of messages is kept - compaction only removes but not re-order them
- Deleted records can still be seen by consumers for a period of **delete.retention.ms** (default: 24h)
- It doesn't prevent you from pushing duplicate data to Kafka
    - de-duplication is done after a segment is committed
    - your consumers will still read from head as soon as the data arrives
- It doesn't prevent you from reading duplicate data from Kafka
- Log compaction can fail
    - make sure there is enough memory

## Example
- topic: Employee-salary
- want to keep the most recent salary for employees
- Before Compaction
    ```
    [Segment 0]
    Key: 123 - {"john":"8000"}
    Key: 456 - {"Mark":"9000"}
    Key: 789 - {"Lisa":"9500"}

    [Segment 1]
    Key: 789 - {"Lisa":"9800"}
    Key: 123 - {"john":"9000"}

    ```

- After Compaction
    ```
    [Segment 0]
    (deleted)
    Key: 456 - {"Mark":"9000"}
    (deleted)

    [Segment 1]
    Key: 789 - {"Lisa":"9800"}
    Key: 123 - {"john":"9000"}

    ```
