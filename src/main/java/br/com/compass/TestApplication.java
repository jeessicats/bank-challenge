package br.com.compass;

import br.com.compass.model.Account;
import br.com.compass.model.Person;
import br.com.compass.util.DatabaseUtil;

import java.math.BigDecimal;

public class TestApplication {
    public static void main(String[] args) {

        // Limpar a tabela antes dos testes
        DatabaseUtil.clearAccountsTable();

        // Testando a classe Person
        Person johnDoe = new Person(
                "John Doe",
                "1985-05-15",
                "98765432100",
                "5559876543",
                "john.doe@example.com",
                "456 Elm St"
        );
        System.out.println("Testing Person class:");
        System.out.println(johnDoe);

        // Testando a classe Account
        Account account = new Account(johnDoe, "Savings");
        System.out.println("\nTesting Account class:");
        System.out.println(account);

        // Alterando o saldo para testar validação
        try {
            account.setBalance(new BigDecimal("-10.00")); // Deve lançar exceção
        } catch (IllegalArgumentException e) {
            System.out.println("\nValidation Test: " + e.getMessage());
        }

        System.out.println("\nAccount creation test completed.");
    }
}