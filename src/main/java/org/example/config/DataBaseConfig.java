package org.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConfig {

    public static Connection getConnection() {

        String url = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "1234";
        Connection connection=null;
        try {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Successfully connected");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return connection;
    }
}
