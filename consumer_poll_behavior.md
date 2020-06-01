# Poll vs Push
- push : server pushes data into consumer and consumer waits
- poll : consumer requests data to server
- poll model allows Kafka consumer to control where in the log they want to consume, how fast they consume, whether they replay events

### Fetch.min.bytes (default 1)  
- controls how much data you want to pull at least on each request
- helps improving throughput and decreasing request number At the cost of latency

### Max.poll.records (default 500)  
- controls how many records to receive per poll request
- increase if your messages are very small and have a lot of available RAM
- good to monitor how many records are polled per request

### Max.partitions.fetch.bytes (default 1MB)  
- maximum data returned by the broker per partition  
- if you read from 100 partitions, you'll need a lot of memeory(RAM)

### Fetch.max.bytes (default 50MB)  
- maximum data returned for each fetch request(covers multiple partitions)  
- the consumer performs multiple fetches in parallel

*Change these settings only if the consumer maxes out on throughput already*