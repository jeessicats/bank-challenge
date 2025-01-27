package br.com.compass.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bank_challenge";
    private static final String USER = "bank-challenge";
    private static final String PASSWORD = "B@nkChallenge";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found. Include it in your library path!", e);
        }
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        return connection;
    }

    public static void testConnection() {
        try (Connection connection = getConnection()) {
            if (connection != null) {
                System.out.println("Database connected successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
        }
    }
}
