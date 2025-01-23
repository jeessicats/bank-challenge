package br.com.compass;

import br.com.compass.model.Account;
import br.com.compass.model.AccountType;
import br.com.compass.model.Person;
import br.com.compass.util.DatabaseUtil;

import java.math.BigDecimal;

public class TestApplication {
    public static void main(String[] args) {

        // Limpar a tabela antes de começar os testes
        DatabaseUtil.clearAccountsTable();

        // Teste da classe Person
        Person oliviaWillow = new Person(
                "Olivia Willow",
                "1990-01-01",
                "12345678901",
                "5551234567",
                "olivia.willow@example.com",
                "123 Main St"
        );

        System.out.println("Testing Person class:");
        System.out.println(oliviaWillow);

        // Teste da classe Account
        Account account = new Account(oliviaWillow, AccountType.SAVINGS);
        System.out.println("\nTesting Account class:");
        System.out.println(account);

        System.out.println("Initial balance: " + account.getBalance());

        System.out.println("");

        // Teste realização de depositos
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
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void performWithdrawal(Account account, BigDecimal amount) {
        try {
            account.withdrawal(amount);
            System.out.println("Balance after withdrawal: " + account.getBalance());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}