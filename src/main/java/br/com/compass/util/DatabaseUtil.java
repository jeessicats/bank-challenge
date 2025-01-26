package br.com.compass.util;

import br.com.compass.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseUtil {

    public static void clearAllTables() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Desabilitar verificações de chave estrangeira (necessário para evitar erros)
            try (PreparedStatement disableFK = connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 0")) {
                disableFK.executeUpdate();
            }

            // Apagar os dados das tabelas na ordem correta
            try (PreparedStatement clearTransactionLogs = connection.prepareStatement("DELETE FROM transactionlogs");
                 PreparedStatement clearAccounts = connection.prepareStatement("DELETE FROM accounts");
                 PreparedStatement clearClients = connection.prepareStatement("DELETE FROM clients")) {

                clearTransactionLogs.executeUpdate();
                System.out.println("Table 'TransactionLogs' cleared successfully.");

                clearAccounts.executeUpdate();
                System.out.println("Table 'Accounts' cleared successfully.");

                clearClients.executeUpdate();
                System.out.println("Table 'Clients' cleared successfully.");
            }

            // Opcional: Resetar os contadores de AUTO_INCREMENT
            try (PreparedStatement resetTransactionLogs = connection.prepareStatement("ALTER TABLE transactionlogs AUTO_INCREMENT = 1");
                 PreparedStatement resetAccounts = connection.prepareStatement("ALTER TABLE accounts AUTO_INCREMENT = 1");
                 PreparedStatement resetClients = connection.prepareStatement("ALTER TABLE clients AUTO_INCREMENT = 1")) {

                resetTransactionLogs.executeUpdate();
                resetAccounts.executeUpdate();
                resetClients.executeUpdate();
                System.out.println("AUTO_INCREMENT reset for all tables.");
            }

            // Reabilitar verificações de chave estrangeira
            try (PreparedStatement enableFK = connection.prepareStatement("SET FOREIGN_KEY_CHECKS = 1")) {
                enableFK.executeUpdate();
            }

        } catch (SQLException e) {
            System.err.println("Error while clearing tables: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
