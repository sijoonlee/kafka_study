# When something bad happens to Consumer
- Consumer is to read a log continusouly
- Something happens and the consumer could be down
- After a certain time (ex 7days) since Consumer is down, the offsets will be considered as 'invalid'
- Then, the behavior of the consumer is to use
    -auto.offset.reset = latest: will read from the end of the log
    -auto.offset.reset = earliest: will read from the start of the log
    -auto.offset.reset = none: will throw exception if no offset is found
- Consumer offsets can be lost
    - if a consumer has not read new data in 1 day (Kafka < 2.0)
    - if a consumer has not read new data in 7 days (Kafka >= 2.0)
    - can be set by offset.retention.minutes

# Replaying data for consumers
- takes all the consumers in a group down
- use 'kafka-consumer-groups' command to set offset you want
- restart consumers
