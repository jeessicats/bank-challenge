package br.com.compass.service;

import br.com.compass.model.Account;
import br.com.compass.model.Client;
import br.com.compass.model.Transaction;
import br.com.compass.model.TransactionType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.mindrot.jbcrypt.BCrypt;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;

public class AccountService {

    private final EntityManager em;

    // Construtor com injeção do EntityManager
    public AccountService(EntityManager em) {
        this.em = em;
    }

    // Abrir uma nova conta bancária
    public boolean openAccount(Account account) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(account); // Salva a conta no banco de dados
            transaction.commit();
            System.out.println("Account successfully created for client: " + account.getClient().getFullName());
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error while opening account: " + e.getMessage());
            return false;
        }
    }

    // Realizar um depósito em uma conta
    public void deposit(Account account, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }

        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            // Atualiza o saldo da conta
            account.setBalance(account.getBalance().add(amount));
            em.merge(account); // Atualiza no banco

            // Registra a transação do depósito
            Transaction depositTransaction = new Transaction(
                    account, null, TransactionType.DEPOSIT, amount, LocalDateTime.now()
            );
            em.persist(depositTransaction);

            transaction.commit();
            System.out.println("Deposit successful. New balance: " + account.getBalance());
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to perform deposit: " + e.getMessage(), e);
        }
    }

    // Realizar um saque de uma conta
    public void withdraw(Account account, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }
        if (account.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient balance for withdrawal");
        }

        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            account.setBalance(account.getBalance().subtract(amount));
            em.merge(account);

            // Registra a transação de saque
            Transaction withdrawalTransaction = new Transaction(
                    account, null, TransactionType.WITHDRAWAL, amount, LocalDateTime.now()
            );
            em.persist(withdrawalTransaction);

            transaction.commit();
            System.out.println("Withdrawal successful. New balance: " + account.getBalance());
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to perform withdrawal: " + e.getMessage(), e);
        }
    }

    // Realizar uma transferência entre contas
    public void transfer(Account sourceAccount, Account destinationAccount, BigDecimal amount) {
        if (destinationAccount == null) {
            throw new IllegalArgumentException("Destination account cannot be null");
        }
        if (sourceAccount.equals(destinationAccount)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }
        if (sourceAccount.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient balance for transfer");
        }

        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            // Atualiza saldos das contas
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
            destinationAccount.setBalance(destinationAccount.getBalance().add(amount));
            em.merge(sourceAccount);
            em.merge(destinationAccount);

            // Registra a transação de transferência
            Transaction transferTransaction = new Transaction(
                    sourceAccount, destinationAccount, TransactionType.TRANSFER, amount, LocalDateTime.now()
            );
            em.persist(transferTransaction);

            transaction.commit();
            System.out.println("Transfer successful. New balance: " + sourceAccount.getBalance());
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to perform transfer: " + e.getMessage(), e);
        }
    }

    // Formatação do valor em moeda brasileira (R$)
    public static String formatCurrency(double amount) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return currencyFormatter.format(amount);
    }

    // Validação de senha do cliente
    public boolean validatePassword(Client client, String rawPassword) {
        return BCrypt.checkpw(rawPassword, client.getPassword());
    }
}
