package br.com.compass;

import br.com.compass.model.Account;
import br.com.compass.model.AccountType;
import br.com.compass.model.Client;
import br.com.compass.repository.AccountRepository;
import br.com.compass.repository.ClientRepository;
import br.com.compass.repository.TransactionRepository;
import br.com.compass.util.DatabaseUtil;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TestApplication {
    public static void main(String[] args) {
        // Limpar tabelas antes do teste
        DatabaseUtil.clearAllTables();

        // Repositórios
        ClientRepository clientRepository = new ClientRepository();
        AccountRepository accountRepository = new AccountRepository();
        TransactionRepository transactionRepository = new TransactionRepository();

        // Criar e salvar o primeiro cliente
        Client client1 = new Client(null, "John Doe", LocalDate.of(1990, 5, 10), "12345678901", "5551234567", "john.doe@example.com", "Password123");
        if (clientRepository.save(client1)) {
            System.out.println("Client 1 saved successfully!");

            // Buscar o cliente pelo CPF para obter o ID gerado
            Client savedClient1 = clientRepository.findByCpf("12345678901");
            if (savedClient1 != null) {
                client1.setIdClient(savedClient1.getIdClient());

                // Criar e salvar a conta para o primeiro cliente
                Account account1 = new Account(client1, AccountType.SAVINGS);
                if (accountRepository.save(account1)) {
                    System.out.println("Account 1 saved successfully!");

                    // Realizar depósito na conta 1
                    account1.deposit(new BigDecimal("1000.00"), accountRepository, transactionRepository);

                    // Realizar saque na conta 1
                    account1.withdrawal(new BigDecimal("200.00"), accountRepository, transactionRepository);
                } else {
                    System.err.println("Failed to save Account 1.");
                }
            } else {
                System.err.println("Failed to retrieve Client 1.");
            }
        } else {
            System.err.println("Failed to save Client 1.");
        }

        // Criar e salvar o segundo cliente
        Client client2 = new Client(null, "Jane Smith", LocalDate.of(1995, 8, 15), "98765432100", "5557654321", "jane.smith@example.com", "Password456");
        if (clientRepository.save(client2)) {
            System.out.println("Client 2 saved successfully!");

            // Buscar o cliente pelo CPF para obter o ID gerado
            Client savedClient2 = clientRepository.findByCpf("98765432100");
            if (savedClient2 != null) {
                client2.setIdClient(savedClient2.getIdClient());

                // Criar e salvar a conta para o segundo cliente
                Account account2 = new Account(client2, AccountType.CHECKING);
                if (accountRepository.save(account2)) {
                    System.out.println("Account 2 saved successfully!");

                    // Realizar transferência da conta 1 para a conta 2
                    Account account1 = accountRepository.findByCpf("12345678901").get(0); // Buscar a conta 1
                    account1.transfer(account2, new BigDecimal("300.00"), accountRepository, transactionRepository);
                } else {
                    System.err.println("Failed to save Account 2.");
                }
            } else {
                System.err.println("Failed to retrieve Client 2.");
            }
        } else {
            System.err.println("Failed to save Client 2.");
        }
    }
}
