package com.clerio.kafka;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Database {

    public Connection getConnection() {
        Connection connection ;
        try{

            connection 
                = DriverManager.getConnection("jdbc:postgresql://postgres:5432/kafkadb", 
                                                "postgres",
                                             "postgres");
            System.out.println("Sucessfuly connected to Kafkadb");

            return connection;

        }catch(SQLException e){
            e.printStackTrace();

        }

        return null;
                                    
    }

    public void insert(String record, Connection connection){
        String r = record+"-proccessed";

        try{
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement =connection.prepareStatement("insert into info (record) values(?)");
            preparedStatement.setString(1, r);
            preparedStatement.executeUpdate();
            System.out.println("Inserting <"+r+ "> in the 'info' table in the kafkadb");


            connection.commit();
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    
}
