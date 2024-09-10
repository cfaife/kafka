package com.clerio.kafka;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws IOException,InterruptedException, ExecutionException{


        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("Server is listening in port 8080");
        while (true){
            Socket socket =    serverSocket.accept();
            handleRequest(socket);
            
        
        }
        //sendMessage("Some message");



    }

    static void handleRequest(Socket socket)throws IOException{
        BufferedReader bufferedReader 
            = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()));

        PrintWriter  printWriter
            = new PrintWriter(new ObjectOutputStream(socket.getOutputStream()));

        Map<String,String> headers = new HashMap<>();
        String line = bufferedReader.readLine();
        String header;
        while ((header = bufferedReader.readLine()) != null 
            && !header.isEmpty()){
            int colonIndex = header.indexOf(":");
            String key 
                =header.substring(0, colonIndex).trim();
            String value = header.substring(colonIndex+1).trim();
            headers.put(key, value);
        }
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
            = new ProducerRecord<>("my-topic", messageValue);

        Future<RecordMetadata> metadata = producer.send(record);
        System.out.println(metadata.get());

        producer.flush();
        producer.close();
    }
}
