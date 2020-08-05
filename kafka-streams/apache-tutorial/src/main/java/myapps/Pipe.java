package myapps;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class Pipe {
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());


        // Next we will define the computational logic of our Streams application.
        // In Kafka Streams this computational logic is defined as a topology of connected processor nodes.
        // We can use a topology builder to construct such a topology,
        final StreamsBuilder builder = new StreamsBuilder();

        // then create a source stream from a Kafka topic named streams-plaintext-input using this topology builder
        KStream<String, String> source = builder.stream("streams-plaintext-input");
        // we get a KStream that is continuously generating records from its source Kafka topic streams-plaintext-input.
        // The records are organized as String typed key-value pairs.

        // The simplest thing we can do with this stream is to write it into another Kafka topic,
        // say it's named streams-pipe-output
        source.to("streams-pipe-output");

        // Note that we can also concatenate the above two lines into a single line as:
        // builder.stream("streams-plaintext-input").to("streams-pipe-output");

        final Topology topology = builder.build();
        System.out.println(topology.describe());
        /*
        Sub-topologies:
        Sub-topology: 0
        Source: KSTREAM-SOURCE-0000000000(topics: streams-plaintext-input) --> KSTREAM-SINK-0000000001
        Sink: KSTREAM-SINK-0000000001(topic: streams-pipe-output) <-- KSTREAM-SOURCE-0000000000
        Global Stores:
        none
         */
        final KafkaStreams streams = new KafkaStreams(topology, props);

        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);

    }
}
