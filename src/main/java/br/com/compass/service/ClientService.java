package br.com.compass.service;

import br.com.compass.exception.ValidationException;
import br.com.compass.model.Client;
import br.com.compass.repository.ClientRepository;
import br.com.compass.util.Validator;

public class ClientService {

    private final ClientRepository clientRepository;

    // Construtor
    public ClientService() {
        this.clientRepository = new ClientRepository();
    }

    // Valida os dados do cliente antes de qualquer operação
    public void validateClient(Client client) {
        if (client.getFullName() == null || client.getFullName().isEmpty()) {
            throw new ValidationException("Full name cannot be null or empty.");
        }
        if (client.getBirthDate() == null) {
            throw new ValidationException("Birth date cannot be null.");
        }
        if (!Validator.isValidCpf(client.getCpf())) {
            throw new ValidationException("Invalid CPF format.");
        }
        if (!Validator.isValidEmail(client.getEmail())) {
            throw new ValidationException("Invalid email format.");
        }
        if (!Validator.isValidPassword(client.getPassword())) {
            throw new ValidationException("Password does not meet the required criteria.");
        }
    }

    // Registra o cliente no sistema
    public void registerClient(Client client) {
        validateClient(client); // Valida os dados do cliente
        boolean success = clientRepository.save(client); // Persiste o cliente no banco de dados

        if (!success) {
            throw new RuntimeException("Failed to register client in the database.");
        }

        System.out.println("Client successfully registered!");
    }

    // Busca cliente por CPF
    public Client findClientByCpf(String cpf) {
        if (!Validator.isValidCpf(cpf)) {
            throw new ValidationException("Invalid CPF format.");
        }

        Client client = clientRepository.findByCpf(cpf);
        if (client == null) {
            throw new RuntimeException("Client not found.");
        }
        return client;
    }
}
