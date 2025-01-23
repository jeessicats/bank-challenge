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

        // Realizar um depósito válido
        account.deposit(new BigDecimal("100.00"));
        System.out.println("Balance after deposit: " + account.getBalance());

        // Tentar um depósito inválido (valor negativo)
        try {
            account.deposit(new BigDecimal("-50.00"));
        } catch (IllegalArgumentException e) {
            System.out.println("Validation test: " + e.getMessage());
        }

        // Tentar um depósito inválido (valor zero)
        try {
            account.deposit(BigDecimal.ZERO);
        } catch (IllegalArgumentException e) {
            System.out.println("Validation test: " + e.getMessage());
        }

        // Tentar um depósito inválido (letras)
        try {
            account.deposit(new BigDecimal("cinquenta reais"));
        } catch (IllegalArgumentException e) {
            System.out.println("Validation test: " + e.getMessage());
        }

        System.out.println("\nAccount creation test completed.");
    }
}