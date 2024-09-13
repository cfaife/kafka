
##  Tology

![alt text](essay-topology.png)

## Requirements

Ensure that you have the follwing `maven` dependecies in your java applications:

    <dependency>
      <groupId>org.apache.kafka</groupId>
      <artifactId>kafka-clients</artifactId>
      <version>3.8.0</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
        <version>2.0.16</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>2.0.16</version>
    </dependency>
    


Make sure you use the latest version. Please refer to the public maven repository to get latest ones: https://mvnrepository.com/artifact/org.slf4j

## Create Kafka topic

Log into your container go to /opt folder and access kafka folder. The kafka-topics.sh script is located in the /bin  folder.

    ./kafka-topics.sh --create --topic my-topic --bootstrap-server localhost:9092


## Topic status

To see if the messages are being sent to the topic in Kafka `/bin`  folder run the following command: 

    ./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic my-topic

This will log real time messages/events being sent to your topic


## Prometheus integration

### On prometheus side

Add the prometheus configurtion

    scrape_configs:
    - job_name: kafka
        static_configs:
        - targets: ['kafka:9200']

### On Kafka side

Add the following environment variable on your container configuration:

    KAFKA_OPTS: -javaagent:/usr/share/jmx_exporter/jmx_prometheus_javaagent-0.20.0.jar=9200:/usr/share/jmx_exporter/kafka-broker.yml



