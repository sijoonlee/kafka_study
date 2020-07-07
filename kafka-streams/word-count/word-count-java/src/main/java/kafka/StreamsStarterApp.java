package kafka;

import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;

public class StreamsStarterApp {
    public static void main(String[] args) {
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-starter-word-count");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        /*
        Note that Kafka Streams is <Key, Value>
        1. Stream from Kafka                <null, "Kafka Kafka Streams"
        2. MapValues lowercase              <null, "kafka kafka streams"
        3. FlatMapValues                    <null, "kafka">, <null, "kafka">, <null, "streams">
        4. SelectKey to apply a key         <"kafka", "kafka">, <"kafka", "kafka">, <"streams", "streams">
        5. GroupByKey before aggregation    (<"kafka", "kafka">, <"kafka", "kafka">), (<"streams", "streams">)
        6. Count occurrences in each group  <"kafka", 2>, <"streams", 1>
        7. To (to write the result back to kafka)
         */

        StreamsBuilder builder = new StreamsBuilder();
        // 1
        KStream<String, String> wordCountInput = builder.stream("word-count-input");

        // 2
//        wordCountInput.mapValues(new ValueMapper<String, Object>() {
//            @Override
//            public Object apply(String value){
//                return null;
//            }
//        });
        KTable<String, Long> wordCounts = wordCountInput.mapValues(value -> value.toLowerCase()) // wordCountInput.mapValues(String::toLowerCase);

        // 3
        .flatMapValues( value -> Arrays.asList(value.split(" ")))
        // 4 - overwrite old keys with value
        .selectKey( (key, value) -> value )
        // 5 - group by key
        .groupByKey()
        // 6 - count
        .count(Materialized.as("Counts"));
        // 7 - get the result back to Kafka
        wordCounts.toStream().to("word-count-output", Produced.with(Serdes.String(), Serdes.Long()));

        KafkaStreams streams = new KafkaStreams(builder.build(), config);
        streams.start();

        // print out topology
        System.out.println(streams.toString());

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close)); // shutdown hook

        /*
        * Internal Topics
        * Managed by, Used by Kafka Streams to save/restore state and re-partition data
        * Prefixed by application.id parameter
        * Should never be deleted, altered, published -- "Internal"
        * Two types:
        *   Repartitioning topics - in case of transforming the key of stream, repartitioning will happen
        *   Changelog topics - in case of aggregations, Kafka Streams will save compacted data in this topic
        * */




    }
}
