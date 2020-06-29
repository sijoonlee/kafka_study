# start zookeeper
zookeeper-server-start.sh /home/sijoonlee/kafka_2.13-2.5.0/config/zookeeper.properties

# start kafka-server
kafka-server-start.sh /home/sijoonlee/kafka_2.13-2.5.0/config/server.properties

# Kafka Topics
kafka-topics.sh --zookeeper 127.0.0.1:2181 --topic fisrt_topic --create --partitions 3 --replication-factor 1

kafka-topics.sh --zookeeper localhost:2181 --list
```
fisrt_topic
```
kafka-topics.sh --zookeeper localhost:2181 --topic fisrt_topic --describe

```
Topic: fisrt_topic	PartitionCount: 3	ReplicationFactor: 1	Configs: 
	Topic: fisrt_topic	Partition: 0	Leader: 0	Replicas: 0	Isr: 0
	Topic: fisrt_topic	Partition: 1	Leader: 0	Replicas: 0	Isr: 0
	Topic: fisrt_topic	Partition: 2	Leader: 0	Replicas: 0	Isr: 0
```
kafka-topics.sh --zookeeper 127.0.0.1:2181 --topic fisrt_topic --delete 
```
Topic fisrt_topic is marked for deletion.
Note: This will have no impact if delete.topic.enable is not set to true.
```
kafka-topics.sh --zookeeper localhost:2181 --list
```
shows nothing
```
kafka-topics.sh --zookeeper 127.0.0.1:2181 --topic first_topic --create --partitions 3 --replication-factor 1

# Kafka Producer
kafka-console-producer.sh --broker-list 127.0.0.1:9092 --topic first_topic
```
>hello
>from console
>^C
```
kafka-console-producer.sh --broker-list 127.0.0.1:9092 --topic first_topic --producer-property acks=all
```
>ack message
>^C
```
kafka-console-producer.sh --broker-list 127.0.0.1:9092 --topic new_topic_that_was_not_created
```
>aaa
[2020-05-20 06:56:29,445] WARN [Producer clientId=console-producer] Error while fetching metadata with correlation id 3 : {new_topic_that_was_not_created=LEADER_NOT_AVAILABLE} (org.apache.kafka.clients.NetworkClient)
>bbb
>^C
```
--> new topic will be created using default parameters  
kafka-topics.sh --zookeeper localhost:2181 --topic new_topic_that_was_not_created --describe
```
Topic: new_topic_that_was_not_created	PartitionCount: 1	ReplicationFactor: 1	Configs: 
	Topic: new_topic_that_was_not_created	Partition: 0	Leader: 0	Replicas: 0	Isr: 0
```
config/server.properties contains the default values  
e.g. num.partitions=1

# Kafka Consumer
kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic first_topic
--> shows messages generated after the execution of command

kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic first_topic --from-beginning
--> shows all messages from beginning

If runing two consoles like below, messages will be spread over the group members  
kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic first_topic --group my_first_group
kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic first_topic --group my_first_group

If runing two consoles like below, all messages will be sent for each group
kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic first_topic --group my_first_group
kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic first_topic --group my_second_group

# Kafka Consumer Groups
kafka-consumer-groups.sh --bootstrap-server 127.0.0.1:9092 --list
```
my_first_group
my_second_group
```
kafka-consumer-groups.sh --bootstrap-server 127.0.0.1:9092 --describe --group my_first_group
```
GROUP           TOPIC           PARTITION  CURRENT-OFFSET  LOG-END-OFFSET  LAG             CONSUMER-ID     HOST            CLIENT-ID
my_first_group  first_topic     0          13              13              0               -               -               -
my_first_group  first_topic     1          11              11              0               -               -               -
my_first_group  first_topic     2          12              12              0               -               -               -
```

--reset-offsets
```
Reset offsets of consumer group. Supports one consumer group at the time, and instances should be inactive                             
Has 2 execution options: 
--dry-run (the default) to plan which offsets to reset, and 
--execute to update the offsets. 
--export option is used to export the results to a CSV format.
You must choose one of the following reset specifications:
--to-datetime, --by-period, --to-earliest, --to-latest,
--shift-by, --from-file, --to-current.                          
To define the scope use --all-topics or --topic. 
One scope must be specified unless you use '--from-file'.      
```

kafka-consumer-groups.sh --bootstrap-server 127.0.0.1:9092 --group my_first_group --reset-offsets --to-earliest --execute --topic first_topic 

```
GROUP                          TOPIC                          PARTITION  NEW-OFFSET     
my_first_group                 first_topic                    0          0              
my_first_group                 first_topic                    1          0              
my_first_group                 first_topic                    2          0  
```

kafka-consumer-groups.sh --bootstrap-server 127.0.0.1:9092 --group my_first_group --reset-offsets --shift-by 2 --execute --topic first_topic
```
GROUP                          TOPIC                          PARTITION  NEW-OFFSET     
my_first_group                 first_topic                    0          2              
my_first_group                 first_topic                    1          2              
my_first_group                 first_topic                    2          2  
```

# Key, Value Parsing
kafka-console-producer.sh --broker-list 127.0.0.1:9092 --topic first_topic --property parse.key=true --property key.separator=,
```
>this is key, this is value
```

kafka-console-consumer.sh --bootstrap-server 127.0.0.1:9092 --topic first_topic --property print.key=true --property key.separator-,
```
this is key	 this is value
```