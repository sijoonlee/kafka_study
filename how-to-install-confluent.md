1. download confluent kafka community version (5.5.1)
extract file into /home/sijoonlee/confluent-5.5.1

2. create directories
- /home/sijoonlee/confluent-5.5.1/data/kafka
- /home/sijoonlee/confluent-5.5.1/data/zookeeper
- /home/sijoonlee/confluent-5.5.1/cli

3. configure kafka server log file directory
- edit server.properties file under /home/sijoonlee/confluent-5.5.1/etc/kafka
- log.dirs=/home/sijoonlee/confluent-5.5.1/data/kafka

4. configure zookeeper data file directory
- edit zookeeper.properties under /home/sijoonlee/confluent-5.5.1/etc/kafka
- dataDir=/home/sijoonlee/confluent-5.5.1/data/zookeeper

5. install confluent cli
- curl -L --http1.1 https://cnfl.io/cli | sh -s -- -b /home/sijoonlee/confluent-5.5.1/cli

6. edit .bashrc file
export CONFLUENT_HOME=/home/sijoonlee/confluent-5.5.1
export PATH="${CONFLUENT_HOME}/bin:$PATH"
export PATH="${CONFLUENT_HOME}/cli:$PATH"