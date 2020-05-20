# install java
sudo apt install openjdk-8-jdk

# download and extract Kafka
https://kafka.apache.org/downloads

# add below to .bashrc
export PATH="/home/sijoonlee/kafka_2.13-2.5.0/bin:$PATH"

# setting zookeeper
- make data directory
[kafka dir]/data/zookeeper
- modify config
[kafka dir]/config/zookeeper.properties
dataDir=/tmp/zookeeper ==> dataDir=/home/sijoonlee/kafka_2.13-2.5.0/data/zookeeper

# setting kafka log dir
- make data directory
[kafka dir]/data/kafka
- [kafka dir]/config/server.properties
log.dirs=/tmp/kafka-logs ==> /home/sijoonlee/kafka_2.13-2.5.0/data/kafka

# start zookeeper
zookeeper-server-start.sh /home/sijoonlee/kafka_2.13-2.5.0/config/zookeeper.properties

# start kafka-server
kafka-server-start.sh /home/sijoonlee/kafka_2.13-2.5.0/config/server.properties