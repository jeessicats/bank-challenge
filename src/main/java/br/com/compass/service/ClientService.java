package br.com.compass.service;

import br.com.compass.exception.ValidationException;
import br.com.compass.model.Client;
import br.com.compass.repository.ClientRepository;
import br.com.compass.util.ClientValidator;

public class ClientService {

    private final ClientRepository clientRepository;

    // Construtor
    public ClientService() {
        this.clientRepository = new ClientRepository();
    }

    public void validateClient(Client client) {
        // Verifica se o objeto client é nulo
        if (client == null) {
            throw new ValidationException("Client cannot be null.");
        }

        // Nome completo
        if (!ClientValidator.isNotNullOrEmpty(client.getFullName())) {
            throw new ValidationException("Full name cannot be null or empty.");
        }

        // Data de nascimento
        if (!ClientValidator.isValidDate(client.getBirthDate())) {
            throw new ValidationException("Birth date cannot be null.");
        }

        if (!ClientValidator.isNotInFuture(client.getBirthDate())) {
            throw new ValidationException("Birth date cannot be in the future.");
        }

        if (!ClientValidator.isAtLeastAge(client.getBirthDate(), 18)) {
            throw new ValidationException("Client must be at least 18 years old.");
        }

        // CPF
        if (!ClientValidator.isNotNullOrEmpty(client.getCpf())) {
            throw new ValidationException("CPF cannot be null or empty.");
        }
        if (!ClientValidator.isValidCpf(client.getCpf())) {
            throw new ValidationException("Invalid CPF format.");
        }

        // Phone Number
        if (!ClientValidator.isNotNullOrEmpty(client.getPhoneNumber())) {
            throw new ValidationException("Phone number cannot be null or empty.");
        }

        // Email
        if (!ClientValidator.isNotNullOrEmpty(client.getEmail())) {
            throw new ValidationException("Email cannot be null or empty.");
        }
        if (!ClientValidator.isValidEmail(client.getEmail())) {
            throw new ValidationException("Invalid email format.");
        }

        // Senha
        if (!ClientValidator.isNotNullOrEmpty(client.getPassword())) {
            throw new ValidationException("Password cannot be null or empty.");
        }
        if (!ClientValidator.isValidPassword(client.getPassword())) {
            throw new ValidationException("Password does not meet the required criteria.");
        }
    }
}
