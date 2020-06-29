### Problem
the producer can introduce duplicate messages in Kafka due to network errors

### Idempotent Producer
send Request Id along with message
Kafka will detect duplicate, won't commit twice
- default setting  
    retries = Integer.MAX_VALUE  
    max.inflight.request=1 (Kafka>=1.0)  
    acks=all  
- how to set Idempotent Producer  
    producerProps.put("enable.idempotence", true);


### Idempotent Producer with max.in.flight.request > 1
https://issues.apache.org/jira/browse/KAFKA-5494
```
Currently, the idempotent producer (and hence transactional producer) requires max.in.flight.requests.per.connection=1.

This was due to simplifying the implementation on the client and server. With some additional work, we can satisfy the idempotent guarantees even with any number of in flight requests. The changes on the client be summarized as follows:

We increment sequence numbers when batches are drained.
If for some reason, a batch fails with a retriable error, we know that all future batches would fail with an out of order sequence exception.
As such, the client should treat some OutOfOrderSequence errors as retriable. In particular, we should maintain the 'last acked sequnece'. If the batch succeeding the last ack'd sequence has an OutOfOrderSequence, that is a fatal error. If a future batch fails with OutOfOrderSequence they should be reenqeued.
With the changes above, the the producer queues should become priority queues ordered by the sequence numbers.
The partition is not ready unless the front of the queue has the next expected sequence.
With the changes above, we would get the benefits of multiple inflights in normal cases. When there are failures, we automatically constrain to a single inflight until we get back in sequence.

With multiple inflights, we now have the possibility of getting duplicates for batches other than the last appended batch. In order to return the record metadata (including offset) of the duplicates inside the log, we would require a log scan at the tail to get the metadata at the tail. This can be optimized by caching the metadata for the last 'n' batches. For instance, if the default max.inflight is 5, we could cache the record metadata of the last 5 batches, and fall back to a scan if the duplicate is not within those 5.
```