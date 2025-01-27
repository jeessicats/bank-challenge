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

            // Obter o ID da conta de origem
            if (transaction.getSourceAccount() != null) {
                statement.setInt(1, transaction.getSourceAccount().getIdAccount());
            } else {
                throw new IllegalArgumentException("Source account cannot be null");
            }

            // Obter o ID da conta de destino (pode ser nulo)
            if (transaction.getDestinationAccount() != null) {
                statement.setInt(2, transaction.getDestinationAccount().getIdAccount());
            } else {
                statement.setNull(2, java.sql.Types.INTEGER);
            }

            // Tipo de transação
            if (transaction.getType() != null) {
                statement.setString(3, transaction.getType().name());
            } else {
                throw new IllegalArgumentException("Transaction type cannot be null");
            }

            // Valor da transação
            statement.setBigDecimal(4, transaction.getAmount());

            // Data da transação
            if (transaction.getTimestamp() != null) {
                statement.setTimestamp(5, java.sql.Timestamp.valueOf(transaction.getTimestamp()));
            } else {
                throw new IllegalArgumentException("Transaction timestamp cannot be null");
            }

            // Executar o SQL e retornar o resultado
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Transaction saved successfully.");
            } else {
                System.err.println("Transaction was not saved.");
            }
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error saving transaction: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
