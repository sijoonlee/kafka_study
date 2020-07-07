## Scheme
1. Origninally... Data streams go through like below  
Kafka -> Kafka Streams -> Kafka

2. Testing would be  
Consumer Record Generator(Factory) -> Kafka Streams -> Producer Record Reader + Test


## Setting up
1. You should use Kafka-streams version 2.0.0, which matchs with the version of test util
    ```
    <dependency>
        <groupId>org.apache.kafka</groupId>
        <artifactId>kafka-streams</artifactId>
        <version>2.0.0</version>
    </dependency>
    ```
2. Maven Dependency for Kafka-Streams-Test-Utils
- https://kafka.apache.org/20/documentation/streams/developer-guide/testing.html
    ```
    <dependency>
        <groupId>org.apache.kafka</groupId>
        <artifactId>kafka-streams-test-utils</artifactId>
        <version>2.0.0</version>
        <scope>test</scope>
    </dependency>
    ```

3. Maven Dependency for JUnit
- https://mvnrepository.com/artifact/junit/junit/4.13
    ```
    <!-- https://mvnrepository.com/artifact/junit/junit -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13</version>
        <scope>test</scope>
    </dependency>
    ```

## Reference
https://kafka.apache.org/11/documentation/streams/developer-guide/testing.html
