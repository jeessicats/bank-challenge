package br.com.compass.repository;

import br.com.compass.config.DatabaseConnection;
import br.com.compass.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionRepository {

    public boolean save(Transaction transaction) {
        String sql = "INSERT INTO transactionlogs (source_account_id, destination_account_id, transaction_type, amount, transaction_date) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, transaction.getSourceAccountId());
            if (transaction.getDestinationAccountId() != null) {
                statement.setInt(2, transaction.getDestinationAccountId());
            } else {
                statement.setNull(2, java.sql.Types.INTEGER);
            }
            statement.setString(3, transaction.getType().name());
            statement.setBigDecimal(4, transaction.getAmount());
            statement.setTimestamp(5, java.sql.Timestamp.valueOf(transaction.getTimestamp()));

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error saving transaction: " + e.getMessage());
            return false;
        }
    }
}
