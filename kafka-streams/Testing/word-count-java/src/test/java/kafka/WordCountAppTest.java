package kafka;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.test.ConsumerRecordFactory;
import org.apache.kafka.streams.test.OutputVerifier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class WordCountAppTest {

    TopologyTestDriver testDriver;
    StringSerializer stringSerializer = new StringSerializer();
    ConsumerRecordFactory<String, String> recordFactory = new ConsumerRecordFactory<>(stringSerializer, stringSerializer);

    @Before
    public void setUpTopologyTestDriver() {
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "test");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "dummy:1234");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        StreamsStarterApp wordCountApp = new StreamsStarterApp();
        Topology topology = wordCountApp.createTopology();
        testDriver = new TopologyTestDriver(topology, config);
    }

    @After
    public void closeTestDriver(){
        testDriver.close();
    }

    public void pushNewInputRecord(String value){
        testDriver.pipeInput(recordFactory.create("word-count-input", null, value));
    }

    public ProducerRecord<String, Long> readOutput(){
        return testDriver.readOutput("word-count-output", new StringDeserializer(), new LongDeserializer());
    }

    @Test
    public void dummyTest(){
        assertEquals("1", "1");
    }

    @Test
    public void makeSureCountsAreCorrect() {
        String firstExample = "testing Kafka Streams";
        pushNewInputRecord(firstExample);
        OutputVerifier.compareKeyValue(readOutput(), "testing", 1L); // throws AssertionError if key or value does not match
        OutputVerifier.compareKeyValue(readOutput(), "kafka", 1L); // throws AssertionError if key or value does not match
        OutputVerifier.compareKeyValue(readOutput(), "streams", 1L); // throws AssertionError if key or value does not match
        assertEquals(readOutput(), null); // no more record
    }
}
