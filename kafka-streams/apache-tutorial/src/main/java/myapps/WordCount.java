package myapps;
import org.apache.kafka.common.serialization.Serdes;

import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class WordCount {
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-wordcount");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> source = builder.stream("streams-plaintext-input");
        source.flatMapValues(value -> Arrays.asList(value.toLowerCase(Locale.getDefault()).split("\\W+")))
                .groupBy((key, value) -> value)
                .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store"))
                .toStream()
                .to("streams-wordcount-output", Produced.with(Serdes.String(), Serdes.Long()));

        final Topology topology = builder.build();
        System.out.println(topology.describe());
        /*
        Topologies:
        Sub-topology: 0
        Source: KSTREAM-SOURCE-0000000000 (topics: [streams-plaintext-input])
        --> KSTREAM-FLATMAPVALUES-0000000001
        Processor: KSTREAM-FLATMAPVALUES-0000000001 (stores: [])
        --> KSTREAM-KEY-SELECT-0000000002
                <-- KSTREAM-SOURCE-0000000000
        Processor: KSTREAM-KEY-SELECT-0000000002 (stores: [])
        --> counts-store-repartition-filter
                <-- KSTREAM-FLATMAPVALUES-0000000001
        Processor: counts-store-repartition-filter (stores: [])
        --> counts-store-repartition-sink
                <-- KSTREAM-KEY-SELECT-0000000002
        Sink: counts-store-repartition-sink (topic: counts-store-repartition)
      <-- counts-store-repartition-filter

        Sub-topology: 1
        Source: counts-store-repartition-source (topics: [counts-store-repartition])
        --> KSTREAM-AGGREGATE-0000000003
        Processor: KSTREAM-AGGREGATE-0000000003 (stores: [counts-store])
        --> KTABLE-TOSTREAM-0000000007
                <-- counts-store-repartition-source
        Processor: KTABLE-TOSTREAM-0000000007 (stores: [])
        --> KSTREAM-SINK-0000000008
                <-- KSTREAM-AGGREGATE-0000000003
        Sink: KSTREAM-SINK-0000000008 (topic: streams-wordcount-output)
      <-- KTABLE-TOSTREAM-0000000007
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
