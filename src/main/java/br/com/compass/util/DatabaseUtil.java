package br.com.compass.util;

import br.com.compass.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseUtil {
    private static final Logger LOGGER = Logger.getLogger(DatabaseUtil.class.getName());
    private static final String[] TABLES = { "transactionlogs", "accounts", "clients" };

    public static void clearAllTables() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            disableForeignKeys(connection);

            for (String table : TABLES) {
                clearTable(connection, table);
            }

            resetAutoIncrement(connection);

            enableForeignKeys(connection);
            LOGGER.info("All tables cleared successfully.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error while clearing tables: {0}", e.getMessage());
        }
    }

    private static void disableForeignKeys(Connection connection) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 0")) {
            stmt.executeUpdate();
        }
    }

    private static void enableForeignKeys(Connection connection) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 1")) {
            stmt.executeUpdate();
        }
    }

    private static void clearTable(Connection connection, String table) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM " + table)) {
            stmt.executeUpdate();
            LOGGER.info("Table '" + table + "' cleared successfully.");
        }
    }

    private static void resetAutoIncrement(Connection connection) throws SQLException {
        for (String table : TABLES) {
            try (PreparedStatement stmt = connection.prepareStatement("ALTER TABLE " + table + " AUTO_INCREMENT = 1")) {
                stmt.executeUpdate();
            }
        }
        LOGGER.info("AUTO_INCREMENT reset for all tables.");
    }
}
