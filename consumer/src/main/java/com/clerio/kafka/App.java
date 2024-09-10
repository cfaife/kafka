package com.clerio.kafka;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ){
        Properties properties = new  Properties();
        properties.put(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, 
            "127.0.0.1:9092");
        properties.put(
            ConsumerConfig.GROUP_ID_CONFIG, 
            "my-consumer-groupId");
        properties.put(
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, 
            StringDeserializer.class.getName());
        properties.put(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, 
            StringDeserializer.class.getName());
        properties.put(
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
             "earliest");

        KafkaConsumer<String,String> consumer =
            new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singleton("my-topic"));

        while (true) {
           ConsumerRecords<String,String> records =
                consumer.poll(Duration.ofMillis(5000));
            for(ConsumerRecord<String, String> record: records){
                System.out.println(
                    "key: "+record.key()+
                    "offset:"+record.offset()+
                    "value:"+record.value()
                    );
            }
            
        }
    }
}
