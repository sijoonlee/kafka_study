package practice1;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

// https://kafka.apache.org/20/documentation.html#producerapi
// https://kafka.apache.org/25/javadoc/index.html?org/apache/kafka/clients/producer/KafkaProducer.html
public class ProducerDemoWithCallback {
    public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(ProducerDemoWithCallback.class);
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
            final ProducerRecord<String ,String> record = new ProducerRecord<String, String>("first_topic", "hello from java " + Integer.toString(i));
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
            }); // asynchronous

            producer.flush();
        }

        producer.close(); // flush and close
    }
}
