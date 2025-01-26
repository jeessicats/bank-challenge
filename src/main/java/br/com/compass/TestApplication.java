package br.com.compass;

import br.com.compass.model.Account;
import br.com.compass.model.AccountType;
import br.com.compass.model.Client;
import br.com.compass.repository.AccountRepository;
import br.com.compass.repository.ClientRepository;
import br.com.compass.util.DatabaseUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TestApplication {
    public static void main(String[] args) {
        // Limpar tabelas antes do teste
        DatabaseUtil.clearAllTables();

        // Criar repositório de clientes
        ClientRepository clientRepository = new ClientRepository();

        // Criar cliente e salvar no banco
        Client client = new Client(null, "John Doe", LocalDate.of(1990, 5, 10), "12345678901", "5551234567", "john.doe@example.com", "Password123");
        boolean isClientSaved = clientRepository.save(client);

        System.out.println(isClientSaved ? "Client saved successfully!" : "Failed to save client.");
        if (!isClientSaved) {
            System.out.println("Cannot proceed without a valid client.");
            return;
        }

        // Criar repositório de contas
        AccountRepository accountRepository = new AccountRepository();

        // Salvar uma nova conta
        Account account = new Account(client, AccountType.SAVINGS);
        boolean isSaved = accountRepository.save(account);
        System.out.println(isSaved ? "Account saved successfully!" : "Failed to save account.");

        // Buscar conta pelo ID
        Account foundAccount = accountRepository.findById(account.getIdAccount());
        System.out.println(foundAccount != null ? "Account found: " + foundAccount : "No account found.");

        // Atualizar saldo
        boolean isUpdated = accountRepository.updateBalance(account.getIdAccount(), new BigDecimal("1000.00"));
        System.out.println(isUpdated ? "Balance updated successfully!" : "Failed to update balance.");

        // Buscar contas pelo CPF
        List<Account> accounts = accountRepository.findByCpf("12345678901");
        accounts.forEach(System.out::println);
    }
}
