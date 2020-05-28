### message compression  
message batch -> compression would improve overall performance  

### compression.type
- 'none'(default), 'gzip', 'lz4', 'snappy'
- gzip has the highest compression ratio
- but it also has the highest load on CPU when compressing/decompressing
- lz4 or snappy would be the optimal

### default behaviour
- by default, Kafka tries to send records as soon as possible
- up to 5 requests in flight (sent at the same time)
- while they are being on the fly, Kafka will start batching new messages  
- This behaviour allows Kafka to increase throughput and to lower latency

### linger.ms
- Number of milliseconds a producer is willing to wait before sending a batch out
- defalut: 0
- The expense of a small dely(ex linger.ms=5) might increase throughput
- If a batch is full before the end of the linger.ms, Kafka sent it right away

### batch.size
- maximum number of bytes in a batch
- default: 16kb
- 32kb/64kb can help increasing the efficiency
- any messages that is bigger than the batch size will not be batched
- do not set the batch size too high, it will waste memory
- Using Kafka Producer Metrics, the average batch size can be monitored


