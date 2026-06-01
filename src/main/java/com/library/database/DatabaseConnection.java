package com.library.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton Database Connection Manager
 * Handles MySQL connectivity for the Library Management System
 */
public class DatabaseConnection {

    // ✅ UPDATE THESE WITH YOUR MYSQL CREDENTIALS
    private static final String URL      = "jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "root";        // your MySQL username
    private static final String PASSWORD = "your_password"; // your MySQL password

    private static Connection connection = null;

    // Private constructor - Singleton pattern
    private DatabaseConnection() {}

    /**
     * Get (or create) the single database connection
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("✅ Database connected successfully!");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed: " + e.getMessage());
        }
        return connection;
    }

    /**
     * Close the connection when the app shuts down
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
