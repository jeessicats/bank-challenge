package br.com.compass.service;

import br.com.compass.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountService {

    public void openAccount(String name, String birthDate, String cpf, String phone, String email, String address, String accountType) {
        String sql = "INSERT INTO accounts (name, birth_date, cpf, phone, email, address, account_type) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setString(2, birthDate);
            statement.setString(3, cpf);
            statement.setString(4, phone);
            statement.setString(5, email);
            statement.setString(6, address);
            statement.setString(7, accountType);

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Account created successfully!");
            } else {
                System.out.println("Failed to create account.");
            }
        } catch (SQLException e) {
            System.out.println("Error while creating account: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
