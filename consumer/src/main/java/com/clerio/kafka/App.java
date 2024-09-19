package com.clerio.kafka;

import java.sql.Connection;
import java.time.Duration;
import java.util.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.RangeAssignor;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Clerio Alfredo Faife
 *
 */
public class App {
    
    public static void main( String[] args ){

        

        Database db =new Database();
        Properties properties = new  Properties();
        properties.put(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, 
            "kafka:9092");
        properties.put(
            ConsumerConfig.GROUP_ID_CONFIG, 
            "my-consumer-groupId22");
        properties.put(
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, 
            StringDeserializer.class.getName());
        properties.put(
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, 
            StringDeserializer.class.getName());
        properties.put(
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
             "earliest");
        properties.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, "false");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");     // Commit offsets automatically
        //properties.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, RangeAssignor.class.getName());
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");




        try (KafkaConsumer<String,String> consumer =
                            new KafkaConsumer<>(properties);){
            consumer.subscribe(Collections.singleton("my-topic"),new ConsumerRebalanceListener() {
                @Override
                public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                    System.out.println("Partitions revoked: " + partitions);
                }

                @Override
                public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                    System.out.println("Partitions assigned: " + partitions);
                }
            });


            

            //consumer.assign(Collections.singleton(new TopicPartition("my-topic", 0)));
            java.util.Map<String,java.util.List<PartitionInfo>> listTopics = consumer.listTopics();
            System.out.println("list of topic size :" + listTopics.size());

            for(String topic : listTopics.keySet()){
                System.out.println("topic name :"+topic);
            }

            
            System.out.println("Consumer service is ready to poll messasges:");

            while (true) {
            ConsumerRecords<String,String> records =
                    consumer.poll(Duration.ofMillis(1000));
                for(ConsumerRecord<String, String> record: records){
                    Connection connection = db.getConnection();
                    db.insert(record.value(), connection);
                    System.out.println(
                        "key: "+record.key()+
                        ", offset:"+record.offset()+
                        ", value:"+record.value()
                        );
                        
                }
                
            }

        }        
    }
}
