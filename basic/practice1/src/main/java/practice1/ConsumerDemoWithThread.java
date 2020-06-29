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
import java.util.concurrent.CountDownLatch;

public class ConsumerDemoWithThread {
    public static void main(String[] args) {
        new ConsumerDemoWithThread().run();

    }
    public void run(){
        Logger logger = LoggerFactory.getLogger(ConsumerDemoWithThread.class.getName());
        String bootstrapServers = "127.0.0.1:9092";
        String groupID = "my_java_group";
        String offsetReset = "earliest";
        String topic = "first_topic";
        CountDownLatch latch = new CountDownLatch(1);
        Runnable myConsumerRunnable = new ConsumerRunnable(topic, bootstrapServers, groupID, offsetReset, latch);
        Thread myThread = new Thread(myConsumerRunnable);
        myThread.start();
        Runtime.getRuntime().addShutdownHook(new Thread( () -> {
            logger.info("Caught shutdown hook");
            ((ConsumerRunnable) myConsumerRunnable).shutdown();
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("Application has exited");

        }));
        try {
            latch.await();
        } catch (InterruptedException e) {
            logger.info("Applicaton is interrupted", e);
        } finally {
            logger.info("Application is closing");
        }
    }
    public class ConsumerRunnable implements Runnable {

        private CountDownLatch latch;
        private KafkaConsumer<String, String> consumer;
        private Logger logger = LoggerFactory.getLogger(ConsumerDemoWithThread.class.getName());
        public ConsumerRunnable(String topic, String bootstrapServers, String groupID, String offsetReset, CountDownLatch latch){

            Properties properties = new Properties();
            properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupID);
            properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offsetReset); // latest, and so on
            consumer = new KafkaConsumer<String, String>(properties);
            consumer.subscribe(Arrays.asList(topic));
            this.latch = latch;
        }
        @Override
        public void run() {
            try{
                while(true){
                    // consumer.poll(100); // decapreiated
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                    for (ConsumerRecord<String, String> record : records){
                        logger.info("Key: " + record.key() + " Value: " + record.value() +
                                "\nPartition: " + record.partition() + " Offset: " + record.offset());
                    }
                }
            } catch (Exception e) {
                logger.info("Received shutdown signal");
            } finally {
                consumer.close();
                latch.countDown();
            }

        }

        public void shutdown(){
            // wakeup method is a special method to interrupt consumer.poll
            // it will throw the exception WakeUpException
            consumer.wakeup();
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
