package br.com.compass.util;

import br.com.compass.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseUtil {

    // Metodo para limpar a tabela accounts
    public static void clearAccountsTable() {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM accounts")) {
            statement.executeUpdate();
            System.out.println("Database cleared before test.");
        } catch (SQLException e) {
            System.out.println("Error while clearing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}