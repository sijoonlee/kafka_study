# Controlling consumer liveliness
- poll thread : a broker  <-----> consumers
- hearbeat thread : a consumer coordinator(acting broker)  <-----> consumers

*heartbeats and poll are decoupled since Kafka 0.10.1*

### Rebalance
- when consumer corrdinator finds a consumer being down, it will ask for broker to rebalance
To avoid frequent rebalance, consumers are encouraged to process data fast and poll often

### Consumer heartbeat thread
- Session.timeout.ms (default 10 sec)
    - hearbeats are sent periodically to the broker
    - if no heartbeat is sent during the period, the consumer is considered 'down'
    - set the period lower to fasten consumer rebalance
- Heartbeat.interval.ms (default 3 sec)
    - the frequency to send heartbeats
    - usually 1/3 of session.timeout.ms

### Consumer poll thread
- max.poll.interval.ms (default 5 min)
    - maxinum amount of time between two .poll() calls before declaring the consumer dead
    - this is particularly relevant for Big Data frameworks like Spark - processing takes time