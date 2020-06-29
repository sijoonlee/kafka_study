# Two most common patterns

### Auto commit
- enable.auto.commit = true
- synchronous processing of batches
- offsets will be committed automatically at regular interval every time you call .poll()
    - auto.commit.interval.ms = 5000 (by default)  
- if you don't use synchronous processing, you will be in "at-most-once" behavior because offsets will be committed before your data is processed
- psedo code looks like  
    ```
        while(true) {
            List<Record> batch = consumer.poll(Duration.ofMillis(100))
            doSomethingSynchronous(batch)
        }
    ```

### Manual commit
- enable.auto.commit = false
- manual commit of offsets
- you control when to commit offsets
- example: accumulating records into a buffer and then flushing the buffer to a database + commiting offsets then
- psedo code looks like
    ```
        while(true) {
            batch += consumer.poll(Duration.ofMillis(100))
            if isReady(batch) {
                doSomethingSynchronous(batch)
                consumer.commitSync()
            }
            
        }
    ```
