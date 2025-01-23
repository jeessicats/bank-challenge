package br.com.compass;

import br.com.compass.model.Account;
import br.com.compass.model.AccountType;
import br.com.compass.model.Person;
import br.com.compass.util.DatabaseUtil;

import java.math.BigDecimal;

public class TestApplication {
    public static void main(String[] args) {

        // Limpar a tabela antes dos testes
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

        // Alteração do saldo para valor negativo para testar validação
        try {
            account.setBalance(new BigDecimal("-10.00"));
        } catch (IllegalArgumentException e) {
            System.out.println("\nValidation Test: " + e.getMessage());
        }

        System.out.println("\nAccount creation test completed.");
    }
}