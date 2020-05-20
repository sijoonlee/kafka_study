package practice1;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerDemoWithGroup {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(ConsumerDemoWithGroup.class.getName());

        String bootstrapServers = "127.0.0.1:9092";
        String groupID = "my_java_group";
        String topic = "first_topic";
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupID);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // latest, and so on

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);
        // consumer.subscribe(Collections.singleton(topic));
        consumer.subscribe(Arrays.asList(topic));
        while(true){
            // consumer.poll(100); // decapreiated
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records){
                logger.info("Key: " + record.key() + " Value: " + record.value() +
                        "\nPartition: " + record.partition() + " Offset: " + record.offset());
            }
        }

    }
}
/* When the second consumer starts, the first consumer's partitions are re-balanced
[main] INFO org.apache.kafka.clients.consumer.internals.AbstractCoordinator - [Consumer clientId=consumer-my_java_group-1, groupId=my_java_group] Attempt to heartbeat failed since group is rebalancing
[main] INFO org.apache.kafka.clients.consumer.internals.ConsumerCoordinator - [Consumer clientId=consumer-my_java_group-1, groupId=my_java_group] Revoke previously assigned partitions first_topic-0, first_topic-1, first_topic-2
[main] INFO org.apache.kafka.clients.consumer.internals.AbstractCoordinator - [Consumer clientId=consumer-my_java_group-1, groupId=my_java_group] (Re-)joining group
[main] INFO org.apache.kafka.clients.consumer.internals.ConsumerCoordinator - [Consumer clientId=consumer-my_java_group-1, groupId=my_java_group] Finished assignment for group at generation 4: {consumer-my_java_group-1-6299f533-eef4-47ac-b95b-c4ff905ddaac=Assignment(partitions=[first_topic-0, first_topic-1]), consumer-my_java_group-1-ffa3f3bf-12ed-49b6-a033-05221313276d=Assignment(partitions=[first_topic-2])}
[main] INFO org.apache.kafka.clients.consumer.internals.AbstractCoordinator - [Consumer clientId=consumer-my_java_group-1, groupId=my_java_group] Successfully joined group with generation 4
[main] INFO org.apache.kafka.clients.consumer.internals.ConsumerCoordinator - [Consumer clientId=consumer-my_java_group-1, groupId=my_java_group] Adding newly assigned partitions: first_topic-2
[main] INFO org.apache.kafka.clients.consumer.internals.ConsumerCoordinator - [Consumer clientId=consumer-my_java_group-1, groupId=my_java_group] Setting offset for partition first_topic-2 to the committed offset FetchPosition{offset=14, offsetEpoch=Optional[0], currentLeader=LeaderAndEpoch{leader=Optional[ubuntu:9092 (id: 0 rack: null)], epoch=0}}
*/
