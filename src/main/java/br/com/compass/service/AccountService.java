package br.com.compass.service;

import br.com.compass.model.Account;
import br.com.compass.model.AccountType;
import br.com.compass.model.Client;
import br.com.compass.repository.AccountRepository;

public class AccountService {

    private final AccountRepository accountRepository;

    // Injeção do repositório no serviço
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public boolean openAccount(Client client, String accountType) {
        try {
            // Validações
            if (client == null) {
                throw new IllegalArgumentException("Client cannot be null");
            }
            if (accountType == null || accountType.isEmpty()) {
                throw new IllegalArgumentException("Account type cannot be null or empty");
            }

            // Criar a conta
            Account account = new Account(client, AccountType.valueOf(accountType.toUpperCase()));

            // Persistir a conta no banco de dados
            return accountRepository.save(account);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid data provided: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error while opening account: " + e.getMessage());
            return false;
        }
    }
}
