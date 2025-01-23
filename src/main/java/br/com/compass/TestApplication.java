package br.com.compass;

import br.com.compass.model.Account;
import br.com.compass.model.Person;
import br.com.compass.service.AccountService;

import java.math.BigDecimal;

public class TestApplication {
    public static void main(String[] args) {
        // Testando a classe Person
        Person person = new Person(
                "Olivia Willow",
                "1990-01-01",
                "12345678901",
                "5551234567",
                "olivia.willow@example.com",
                "123 Main St"
        );
        System.out.println("Testing Person class:");
        System.out.println(person);

        // Testando a classe Account
        Account account = new Account(person, "Savings");
        System.out.println("\nTesting Account class:");
        System.out.println(account);

        // Alterando o saldo para testar validação
        try {
            account.setBalance(new BigDecimal("-10.00")); // Deve lançar exceção
        } catch (IllegalArgumentException e) {
            System.out.println("\nValidation Test: " + e.getMessage());
        }

        // Testando o metodo openAccount da classe AccountService
        AccountService accountService = new AccountService();
        System.out.println("\nTesting AccountService:");
        accountService.openAccount(
                person.getName(),
                person.getBirthDate(),
                person.getCpf(),
                person.getPhone(),
                person.getEmail(),
                person.getAddress(),
                "Savings"
        );

        System.out.println("\nAccount creation test completed.");
    }
}