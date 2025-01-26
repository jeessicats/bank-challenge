package br.com.compass;

import br.com.compass.model.Client;
import br.com.compass.repository.ClientRepository;
import br.com.compass.util.DatabaseUtil;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TestApplication {
    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // 1. Limpar todas as tabelas do banco antes de iniciar os testes
        System.out.println("Clearing all tables before tests...");
        DatabaseUtil.clearAllTables();
        System.out.println("All tables cleared. Ready for testing!");

        // 2. Criar um cliente
        Client oliviaWillow = new Client(
                "Matcha Latte",
                LocalDate.parse("01/01/1990", formatter),
                "12345678955",
                "1151234567",
                "matcha@example.com",
                "Password123"
        );

        System.out.println("\nTesting Client class:");
        System.out.println("Client to save: " + oliviaWillow);

        // 3. Instanciar o repositório
        ClientRepository clientRepository = new ClientRepository();

        // 4. Salvar o cliente no banco de dados
        boolean isSaved = clientRepository.save(oliviaWillow);

        if (isSaved) {
            System.out.println("\nClient saved successfully!");
        } else {
            System.out.println("\nFailed to save the client.");
            return; // Interrompe os testes em caso de falha
        }

        // 5. Buscar o cliente pelo CPF para verificar se foi salvo
        String cpfToSearch = "12345678901";
        Client foundClient = clientRepository.findByCpf(cpfToSearch);

        if (foundClient != null) {
            System.out.println("\nClient found in the database:");
            System.out.println(foundClient);
        } else {
            System.out.println("\nNo client found with CPF: " + cpfToSearch);
        }
    }
}
