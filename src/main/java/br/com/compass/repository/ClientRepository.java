package br.com.compass.repository;

import br.com.compass.model.Client;
import br.com.compass.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ClientRepository {

    // Salva o cliente no banco de dados
    public boolean save(Client client) {
        String sql = "INSERT INTO clients (full_name, birth_date, cpf, phone_number, email, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, client.getFullName());
            statement.setDate(2, java.sql.Date.valueOf(client.getBirthDate()));
            statement.setString(3, client.getCpf());
            statement.setString(4, client.getPhoneNumber());
            statement.setString(5, client.getEmail());
            statement.setString(6, client.getPassword());

            return statement.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Busca um cliente pelo CPF
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
                            resultSet.getString("password")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

