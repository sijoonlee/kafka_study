## Example - FileStreamSourceConnector - Standalone mode
- Goal
    - read a file and load the content directly to Kafka
    - Run a connector in standalone mode

    file -> connector 1 (schema disabled) -> kafka topic 1


- procedure
```
docker-compose up kafka-cluster

# A) FileStreamSourceConnector in standalone mode
# Look at the source/demo-1/worker.properties file and edit bootstrap
# Look at the source/demo-1/file-stream-demo.properties file
# Look at the demo-file.txt file

docker run --rm -it -v "$(pwd)":/tutorial --net=host landoop/fast-data-dev:cp3.3.0 bash

# we launch the kafka connector in standalone mode:
cd /tutorial/demo-1

# create the topic we write to with 3 partitions
kafka-topics --create --topic demo-1-standalone --partitions 3 --replication-factor 1 --zookeeper 127.0.0.1:2181

# Usage is connect-standalone worker.properties connector1.properties [connector2.properties connector3.properties]
connect-standalone worker.properties file-stream-demo-standalone.properties