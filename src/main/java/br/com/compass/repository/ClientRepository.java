package br.com.compass.repository;

import br.com.compass.config.DatabaseConnection;
import br.com.compass.model.Client;

import java.sql.*;

public class ClientRepository {

    public boolean save(Client client) {
        String sql = "INSERT INTO clients (full_name, birth_date, cpf, phone_number, email, client_password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, client.getFullName());
            statement.setDate(2, java.sql.Date.valueOf(client.getBirthDate()));
            statement.setString(3, client.getCpf());
            statement.setString(4, client.getPhoneNumber());
            statement.setString(5, client.getEmail());
            statement.setString(6, client.getPassword());

            int rowsInserted = statement.executeUpdate();

            // Recuperar o ID gerado e configurar no objeto Client
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        client.setIdClient(generatedKeys.getInt(1)); // Define o ID no objeto Client
                    }
                }
            }

            return rowsInserted > 0;
        } catch (SQLException e) {
            System.err.println("Error saving client: " + e.getMessage());
            return false;
        }
    }

    public Client findByCpf(String cpf) {
        String sql = "SELECT * FROM clients WHERE cpf = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cpf);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Client(
                            resultSet.getString("full_name"),
                            resultSet.getDate("birth_date").toLocalDate(),
                            resultSet.getString("cpf"),
                            resultSet.getString("phone_number"),
                            resultSet.getString("email"),
                            resultSet.getString("client_password") // Corrigir aqui!
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding client by CPF: " + e.getMessage());
        }
        return null;
    }
}
