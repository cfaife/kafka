package com.clerio.kafka;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
/**
 * @author Clerio Alfredo Faife
 */
public class App {
    public static void main( String[] args ) throws IOException,InterruptedException, ExecutionException{


        try(ServerSocket serverSocket = new ServerSocket(8080)){
            System.out.println("Publisher server is listening in port 8080");
            while (true){
                Socket socket = serverSocket.accept();
                handleRequest(socket);
            }
        }        
    }

    static void handleRequest(Socket socket)throws IOException, InterruptedException, ExecutionException{
        BufferedReader bufferedReader 
            = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));

        PrintWriter  printWriter
            = new PrintWriter(new ObjectOutputStream(socket.getOutputStream()));

        String line;
        while ((line = bufferedReader.readLine()) != null 
            && !line.isEmpty()){
            if(line.contains("GET")){
                String record=line.substring(5, line.indexOf("H"));
                System.out.println("record >>>>>>>>" +record);

                sendMessageToTopic(record);
                
            } 
            System.out.println(line);
            
           
        }
        System.out.println("\n");

        String response = "HTTP/1.1 200 OK\r\n" +
        "Content-Type: text/plain\r\n" +
        "Content-Length: " + "done".length() + "\r\n" +
        "\r\n" +
        "done";

        printWriter.println(response);
        printWriter.flush();
        
        

    }

    static void sendMessageToTopic(String messageValue) throws InterruptedException, ExecutionException{
        Properties properties = new Properties();
        properties.setProperty(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, 
            "127.0.0.1:9092");
        properties.setProperty(
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, 
            StringSerializer.class.getName());
        properties.setProperty(
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, 
            StringSerializer.class.getName());

        KafkaProducer<String,String> producer 
            = new KafkaProducer<>(properties);

        ProducerRecord<String,String> record 
            = new ProducerRecord<>("my-topic", "mycustomkay",messageValue);

        Future<RecordMetadata> metadata = producer.send(record);
        System.out.println(metadata.get());

        producer.flush();
        producer.close();
    }
}
