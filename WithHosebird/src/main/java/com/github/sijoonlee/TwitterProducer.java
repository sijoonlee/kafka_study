package com.github.sijoonlee;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TwitterProducer {
    Logger logger = LoggerFactory.getLogger(TwitterProducer.class.getName());
    public TwitterProducer(){

    }
    public static void main(String[] args) {
        new TwitterProducer().run();
    }
    public void run(){
        logger.info("Start");

        // https://github.com/twitter/hbc
        String consumerKey = "k";
        String consumerSecret = "s";
        String token = "t";
        String tokenSecret = "ts";
        List<String> terms = Lists.newArrayList("Korea");

        /** Set up your blocking queues: Be sure to size these properly based on expected TPS of your stream */
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(1000);
        // BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>(1000);

        // create a twitter client
        Client client = createTwitterClient(msgQueue, consumerKey, consumerSecret, token, tokenSecret, terms);
        client.reconnect();

        KafkaProducer<String, String> producer = createKafkaProducer();

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            logger.info("stop client");
            client.stop();
            logger.info("stop producer");
            producer.close();
        }));



        // loop to send tweets to kafka
        // on a different thread, or multiple different threads....
        while (!client.isDone()) {
            String msg = null;
            try{
                msg = msgQueue.poll(5, TimeUnit.SECONDS); // timeout - 5 seconds
            } catch (InterruptedException e){
                e.printStackTrace();
                client.stop();
            }

            if (msg != null){
                logger.info(msg);
                producer.send(new ProducerRecord<>("tweets", null, msg), new Callback(){
                   @Override
                   public void onCompletion(RecordMetadata recordMetadata, Exception e){
                       if(e!= null){
                           logger.error("error: ",  e);
                       }
                   }
                });
            }
        }
        logger.info("End of app");
    }
    public Client createTwitterClient(BlockingQueue<String> msgQueue,
                                      String consumerKey,
                                      String consumerSecret,
                                      String token,
                                      String tokenSecret,
                                      List<String> terms){

/** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
// Optional: set up some followings and track terms
        // List<Long> followings = Lists.newArrayList(1234L, 566788L);
        // List<String> terms = Lists.newArrayList("Korea");
        // hosebirdEndpoint.followings(followings);
        hosebirdEndpoint.trackTerms(terms);

// These secrets should be read from a config file

        Authentication hosebirdAuth = new OAuth1(consumerKey, consumerSecret, token, tokenSecret);

        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")                              // optional: mainly for the logs
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue));
                // .eventMessageQueue(eventQueue);                          // optional: use this if you want to process client events

        Client hosebirdClient = builder.build();
// Attempts to establish a connection.
        hosebirdClient.connect();
        return hosebirdClient;
    }

    public KafkaProducer<String, String> createKafkaProducer(){
        String bootstrapServer = "127.0.0.1:9092";
        Properties properties = new Properties();
//        properties.setProperty("bootstrap.servers", bootstrapServer);
//        properties.setProperty("key.serializer", StringSerializer.class.getName());
//        properties.setProperty("value.serializer", StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        return producer;
//        ProducerRecord<String ,String> record = new ProducerRecord<String, String>("first_topic", "hello from java");
//        producer.send(record); // asynchronous
//        producer.flush();
//        producer.close(); // flush and close
    }

}
