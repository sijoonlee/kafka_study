package practice1;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

// https://kafka.apache.org/20/documentation.html#producerapi
// https://kafka.apache.org/25/javadoc/index.html?org/apache/kafka/clients/producer/KafkaProducer.html
public class ProducerDemoWithKeys {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final Logger logger = LoggerFactory.getLogger(ProducerDemoWithKeys.class);
        String bootstrapServer = "127.0.0.1:9092";
        Properties properties = new Properties();
//        properties.setProperty("bootstrap.servers", bootstrapServer);
//        properties.setProperty("key.serializer", StringSerializer.class.getName());
//        properties.setProperty("value.serializer", StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        for(int i = 0; i < 5; i++){

            String topic = "first_topic";
            String value = "hello from java " + Integer.toString(i);
            String key = "id_" + Integer.toString(i);

            final ProducerRecord<String ,String> record = new ProducerRecord<String, String>(topic, key, value);

            logger.info("Key: " + key);

            producer.send(record, new Callback(){
                public void onCompletion(RecordMetadata recordMetadata, Exception e){
                    // executes every time a record is successfully sent or an exception is thrown
                    if (e == null){
                        logger.info("Received new metatdata" +
                                "\nTopic: " + recordMetadata.topic() +
                                "\nPartition: " + recordMetadata.partition() +
                                "\nOffset: " + recordMetadata.offset() +
                                "\nTimestampe: " + recordMetadata.timestamp());
                    } else {
                        logger.error("Error while producing " + e);
                    }
                }
            }).get(); // use get() to make it synchronous, don't do this in production

            producer.flush();
        }

        producer.close(); // flush and close
    }
}

// Note that the same key ends up with the same partition
