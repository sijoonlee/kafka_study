**KafKa < 0.11**
- acks = all
- min.insync.replicas = 2
- retries = MAX_INT
- max.in.flight.requests.per.connection = 1

**Kafka >= 0.11**
- enable.idempotence = true
- min.insync.replicas = 2

