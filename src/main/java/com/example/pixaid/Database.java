package com.example.pixaid;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database{
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/login_schema";
    private static final String USER = "root";
    private static final String PASSWORD = "@forcode5419";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // MySQL JDBC driver load kortesi
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establishing the connection
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Database connection successful!");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found. Include it in your library path!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database!");
            e.printStackTrace();
        }
        return connection;
    }
    public static void main(String[] args) {
        // Test the connection
        Connection conn = Database.getConnection();
        if (conn != null) {
            System.out.println("Connection established successfully!");
        } else {
            System.out.println("Failed to make connection!");
        }
    }
}
