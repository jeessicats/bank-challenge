package br.com.compass.repository;

import br.com.compass.config.DatabaseConnection;
import br.com.compass.model.Account;
import br.com.compass.model.AccountType;
import br.com.compass.model.Client;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountRepository {

    // Salvar uma nova conta no banco de dados
    public boolean save(Account account) {
        String sql = "INSERT INTO accounts (id_client, account_type, balance, creation_date) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, account.getClient().getIdClient()); // ID do cliente
            statement.setString(2, account.getAccountType().name()); // Tipo de conta (ENUM)
            statement.setBigDecimal(3, account.getBalance()); // Saldo inicial
            statement.setTimestamp(4, Timestamp.valueOf(account.getCreationDate())); // Data de criação

            int rowsInserted = statement.executeUpdate();

            // Recuperar o ID gerado da conta
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        account.setIdAccount(generatedKeys.getInt(1)); // Define o ID no objeto
                        System.out.println("Account ID generated: " + account.getIdAccount());
                    }
                }
            }

            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Error saving account: " + e.getMessage());
            return false;
        }
    }

    // Buscar contas pelo CPF
    public List<Account> findByCpf(String cpf) {
        String sql = "SELECT a.id_account, a.id_client, a.account_type, a.balance, a.creation_date " +
                "FROM accounts a " +
                "JOIN clients c ON a.id_client = c.id_client " +
                "WHERE c.cpf = ?";
        List<Account> accounts = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cpf); // Define o CPF no parâmetro
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    accounts.add(new Account(
                            resultSet.getInt("id_account"),
                            new Client(resultSet.getInt("id_client")), // Apenas o ID do cliente
                            AccountType.valueOf(resultSet.getString("account_type")),
                            resultSet.getBigDecimal("balance"),
                            resultSet.getTimestamp("creation_date").toLocalDateTime()
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding accounts by CPF: " + e.getMessage());
        }
        return accounts;
    }

    // Atualizar o saldo de uma conta
    public boolean updateBalance(int idAccount, BigDecimal newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE id_account = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBigDecimal(1, newBalance);
            statement.setInt(2, idAccount);

            boolean updated = statement.executeUpdate() > 0;
            if (updated) {
                System.out.println("Updated balance for account ID " + idAccount + " to: " + newBalance);
            }
            return updated;
        } catch (SQLException e) {
            System.err.println("Error updating account balance: " + e.getMessage());
            return false;
        }
    }
}
