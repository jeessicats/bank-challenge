package br.com.compass;

import br.com.compass.model.Account;
import br.com.compass.model.AccountType;
import br.com.compass.model.Client;
import br.com.compass.util.DatabaseUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TestApplication {
    public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Limpar a tabela antes de começar os testes
        System.out.println("Clearing all tables before tests...");
        DatabaseUtil.clearAllTables();
        System.out.println("All tables cleared. Ready for testing!");

        // Teste da classe Client
        Client oliviaWillow = new Client(
                "Olivia Willow",
                LocalDate.parse("01/01/1990", formatter),
                "12345678901",
                "5551234567",
                "olivia.willow@example.com",
                "Password123"
        );

        System.out.println("Testing Client class:");
        System.out.println(oliviaWillow);

        // Teste da classe Account
        Account account = new Account(oliviaWillow, AccountType.SAVINGS);
        System.out.println("\nTesting Account class:");
        System.out.println(account);

        System.out.println("Initial balance: " + account.getBalance());

        System.out.println("");

        // Teste realização de depósitos
        performDeposit(account, new BigDecimal("100.00")); // Depósito válido
        performDeposit(account, new BigDecimal("-100.00")); // Valor inválido (negativo)
        performDeposit(account, BigDecimal.ZERO); // Valor inválido (zero)

        System.out.println("");

        // Teste realização de saques
        performWithdrawal(account, new BigDecimal("50.00")); // Saque válido
        performWithdrawal(account, new BigDecimal("200.00")); // Saldo insuficiente
        performWithdrawal(account, BigDecimal.ZERO); // Valor inválido
    }

    private static void performDeposit(Account account, BigDecimal amount) {
        try {
            account.deposit(amount);
            System.out.println("Balance after deposit: " + account.getBalance());
        } catch (IllegalArgumentException e) {
            System.out.println("Error during deposit: " + e.getMessage());
        }
    }

    private static void performWithdrawal(Account account, BigDecimal amount) {
        try {
            account.withdrawal(amount);
            System.out.println("Balance after withdrawal: " + account.getBalance());
        } catch (IllegalArgumentException e) {
            System.out.println("Error during withdrawal: " + e.getMessage());
        }
    }
}
