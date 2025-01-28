package br.com.compass.service;

import br.com.compass.model.Account;
import br.com.compass.model.Transaction;
import br.com.compass.model.TransactionType;
import br.com.compass.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.Locale;

public class AccountService {

    // Método para abrir conta
    public boolean openAccount(Account account) {
        EntityManager em = JpaUtil.getEntityManager(); // Obter o EntityManager do JpaUtil
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            em.persist(account); // Salvar a conta no banco de dados
            transaction.commit();
            System.out.println("Account successfully created for client: " + account.getClient().getFullName());
            return true;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Error while opening account: " + e.getMessage());
            return false;
        } finally {
            em.close(); // Certifique-se de fechar o EntityManager
        }
    }

    // Método para depósito
    public void deposit(Account account, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero.");
        }

        EntityManager em = JpaUtil.getEntityManager(); // Obter o EntityManager do JpaUtil
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            // Atualizar saldo da conta
            account.setBalance(account.getBalance().add(amount));
            em.merge(account); // Atualizar a conta no banco de dados

            // Criar e salvar a transação de depósito
            Transaction depositTransaction = new Transaction(
                    account, // Conta de origem
                    null,    // Sem conta de destino para depósitos
                    TransactionType.DEPOSIT,
                    amount,
                    LocalDateTime.now() // Timestamp da transação
            );
            em.persist(depositTransaction);

            transaction.commit();
            System.out.println("Deposit successful. New balance: " + account.getBalance());
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to perform deposit: " + e.getMessage(), e);
        } finally {
            em.close(); // Certifique-se de fechar o EntityManager
        }
    }

    // Método para saque
    public void withdraw(Account account, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }
        if (account.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient balance for withdrawal");
        }

        EntityManager em = JpaUtil.getEntityManager(); // Obter o EntityManager do JpaUtil
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            // Atualizar saldo
            account.setBalance(account.getBalance().subtract(amount));
            em.merge(account); // Atualizar a entidade no banco de dados

            // Registrar transação
            Transaction withdrawalTransaction = new Transaction(
                    account,
                    null, // Conta de destino nula para saques
                    TransactionType.WITHDRAWAL,
                    amount,
                    LocalDateTime.now()
            );
            em.persist(withdrawalTransaction);

            transaction.commit();
            System.out.println("Withdrawal successful. New balance: " + account.getBalance());
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to perform withdrawal: " + e.getMessage(), e);
        } finally {
            em.close(); // Certifique-se de fechar o EntityManager
        }
    }

    // Método para transferência
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

        EntityManager em = JpaUtil.getEntityManager(); // Obter o EntityManager do JpaUtil
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            // Atualizar saldos
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
            destinationAccount.setBalance(destinationAccount.getBalance().add(amount));
            em.merge(sourceAccount);
            em.merge(destinationAccount);

            // Registrar transação
            Transaction transferTransaction = new Transaction(
                    sourceAccount,
                    destinationAccount,
                    TransactionType.TRANSFER,
                    amount,
                    LocalDateTime.now()
            );
            em.persist(transferTransaction);

            transaction.commit();
            System.out.println("Transfer successful. New balance: " + sourceAccount.getBalance());
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Failed to perform transfer: " + e.getMessage(), e);
        } finally {
            em.close(); // Certifique-se de fechar o EntityManager
        }
    }

    public static String formatCurrency(double amount) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return currencyFormatter.format(amount);
    }
}