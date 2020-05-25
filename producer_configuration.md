# acks
### acks = 0 (no acks)  
- no response is requested
- if the broker goes offline or an exception happens, we won't know and will lose data
- useful where it's okay to lose messages - metrics collection, log collection

### acks = 1 (leader acks)
- leader response is requested, but replication is not a grueantee
(happens in the background)
- if an ack is not received, the producer may retry
- if the leader broker goes offline while replicas haven't replicated the data yet, data loss happens

### acks = all (replicats acks)
- leader + replicas ack requested
- latency & safety
- necessary setting if you don't want to lose data
- acks=all must be used in conjunction with **min.insync.replicas**
- min.insync.replicas=2 implies that at least 2 brokers must respond
- replication.factor=3, min.insync=2, acks=all means  
It can tolerate only 1 broker going down  
Otherwise the producer will receive an exception on send  
  
# Producer retries
- How many times?   
    retries : 2147483647 by default for Kafka >= 2.1
- How frequently?  
    retry.backoff.ms : 100 ms
- How long?  
    delivery.timeout.ms = 120000 ms (2minutes)  
  
# Caution to Producer retries
- Basically, the same key falls under the same category  
- There's cahance that messages will be sent out of order  
- If you rely on key-based ordering, it might be an issue  
- Set the number of requests being sent in parallel  
    max.in.flight.requests.per.connection: 5 (by default)  
    --> set to 1 if you want to ensure ordering  
    --> it may impact throughput
- Idempotent producers is the better solution

**resource**: 

1. max.in.flight.requests.per.connection  
https://docs.confluent.io/current/installation/configuration/producer-configs.html  
The maximum number of unacknowledged requests the client will send on a single connection before blocking. Note that if this setting is set to be greater than 1 and there are failed sends, there is a risk of message re-ordering due to retries (i.e., if retries are enabled).

2. How does Apache Kafka producer send records to partitions parallelly?
https://stackoverflow.com/questions/53011303/how-does-apache-kafka-producer-send-records-to-partitions-parallelly

```
Kafka client uses a org.apache.kafka.common.requests.ProduceRequest that can carry payloads for multiple partitions at once (see http://kafka.apache.org/protocol.html#The_Messages_Produce).

So it sends (using org.apache.kafka.clients.NetworkClient) in three requests in parallel, to each of (three) brokers, i.e.:

- sends records for topic-partition0, topic-partition1, topic-partition2 to broker 1
- sends records for topic-partition3, topic-partition4, topic-partition5 to broker 2
- sends records for topic-partition6, topic-partition7, topic-partition8 to broker 3
You can control how much batching is done with producer configuration.

(notice I answered with 9 unique partitions, if you meant replicated partitions, you send only to leader - then the replication will handle the propagation).
```

3. Understanding the max.inflight property of kafka producer  
https://stackoverflow.com/questions/49802686/understanding-the-max-inflight-property-of-kafka-producer
```
Your use case is slightly unclear. You mention ordering and no data loss but don't specify if you tolerate duplicate messages. So it's unclean if you want At least Once (QoS 1) or Exactly Once

Either way, as you're using 1.0.0 and only using a single partition, you should have a look at the Idempotent Producer instead of tweaking the Producer configs. It allows to properly and efficiently guarantee ordering and no data loss.

From the documentation:

Idempotent delivery ensures that messages are delivered exactly once to a particular topic partition during the lifetime of a single producer.

The early Idempotent Producer was forcing max.in.flight.requests.per.connection to 1 (for the same reasons you mentioned) but in the latest releases it can now be used with max.in.flight.requests.per.connection set to up to 5 and still keep its guarantees.

Using the Idempotent Producer you'll not only get stronger delivery semantics (Exactly Once instead of At least Once) but it might even perform better!

I recommend you check the delivery semantics in the docs
Back to your question

Yes without the idempotent (or transactional) producer, if you want to avoid data loss (QoS 1) and preserve ordering, you have to set max.in.flight.requests.per.connection to 1, allow retries and use acks=all. As you saw this comes at a significant performance cost.
```

4. Message Delivery Semantics
http://kafka.apache.org/documentation/#semantics
```
At most once — Messages may be lost but are never redelivered.
At least once — Messages are never lost but may be redelivered.
Exactly once — this is what people actually want, each message is delivered once and only once.

...

Prior to 0.11.0.0, if a producer failed to receive a response indicating that a message was committed, it had little choice but to resend the message. This provides at-least-once delivery semantics since the message may be written to the log again during resending if the original request had in fact succeeded. Since 0.11.0.0, the Kafka producer also supports an idempotent delivery option which guarantees that resending will not result in duplicate entries in the log. To achieve this, the broker assigns each producer an ID and deduplicates messages using a sequence number that is sent by the producer along with every message. Also beginning with 0.11.0.0, the producer supports the ability to send messages to multiple topic partitions using transaction-like semantics: i.e. either all messages are successfully written or none of them are. The main use case for this is exactly-once processing between Kafka topics (described below).

...

So what about exactly once semantics (i.e. the thing you actually want)? When consuming from a Kafka topic and producing to another topic (as in a Kafka Streams application), we can leverage the new transactional producer capabilities in 0.11.0.0 that were mentioned above. The consumer's position is stored as a message in a topic, so we can write the offset to Kafka in the same transaction as the output topics receiving the processed data. If the transaction is aborted, the consumer's position will revert to its old value and the produced data on the output topics will not be visible to other consumers, depending on their "isolation level." In the default "read_uncommitted" isolation level, all messages are visible to consumers even if they were part of an aborted transaction, but in "read_committed," the consumer will only return messages from transactions which were committed (and any messages which were not part of a transaction).

```