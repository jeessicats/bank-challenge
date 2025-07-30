package br.com.compass.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/bank_challenge";
    private static final String USER = "bankchallenge";
    private static final String PASSWORD = "B@nkChallenge";
    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void testConnection() {
        try (Connection connection = getConnection()) {
            LOGGER.info("Database connected successfully!");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to connect to the database: {0}", e.getMessage());
        }
    }
}
