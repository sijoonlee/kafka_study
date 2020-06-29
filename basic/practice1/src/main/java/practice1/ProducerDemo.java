package practice1;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
// https://kafka.apache.org/20/documentation.html#producerapi
// https://kafka.apache.org/25/javadoc/index.html?org/apache/kafka/clients/producer/KafkaProducer.html
public class ProducerDemo {
    public static void main(String[] args) {
        String bootstrapServer = "127.0.0.1:9092";
        Properties properties = new Properties();
//        properties.setProperty("bootstrap.servers", bootstrapServer);
//        properties.setProperty("key.serializer", StringSerializer.class.getName());
//        properties.setProperty("value.serializer", StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        ProducerRecord<String ,String> record = new ProducerRecord<String, String>("first_topic", "hello from java");
        producer.send(record); // asynchronous
        producer.flush();
        producer.close(); // flush and close
    }
}
