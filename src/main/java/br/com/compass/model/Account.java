package br.com.compass.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Account {

    private int idAccount; // idAccount
    private final Client client; // Relacionamento com Cliente
    private final AccountType accountType; // Enum AccountType
    private BigDecimal balance; // BigDecimal para trabalhar com dinheiro
    private final LocalDateTime creationDate;

    // Construtor com campos obrigatórios
    public Account(Client client, AccountType accountType) {
        this.client = client;
        this.accountType = accountType;
        this.balance = BigDecimal.ZERO; // Saldo inicial padrão
        this.creationDate = LocalDateTime.now(); // Data de criação padrão
    }

    // Construtor completo
    public Account(int idAccount, Client client, AccountType accountType, BigDecimal balance, LocalDateTime creationDate) {
        this.idAccount = idAccount;
        this.client = client;
        this.accountType = accountType;
        this.balance = balance;
        this.creationDate = creationDate;
    }

    // Getters e setters
    public int getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    public Client getClient() {
        return client;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    // Métodos de operação
    public void deposit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }
        this.balance = this.balance.add(amount);
    }

    public void withdrawal(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }
        if (this.balance.subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient balance for withdrawal");
        }
        this.balance = this.balance.subtract(amount);
    }

    public void transfer(Account destinationAccount, BigDecimal transferAmount) {
        if (destinationAccount == null) {
            throw new IllegalArgumentException("Destination account cannot be null");
        }
        if (this.equals(destinationAccount)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        if (transferAmount == null || transferAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }
        if (this.balance.subtract(transferAmount).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient balance for transfer");
        }

        // Realizar a transferência
        this.withdrawal(transferAmount);
        destinationAccount.deposit(transferAmount);
    }

    // Métodos de comparação e representação
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return idAccount == account.idAccount; // Comparação com idAccount
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAccount); // Hash baseado em idAccount
    }

    @Override
    public String toString() {
        return "Account{" +
                "idAccount=" + idAccount +
                ", clientName=" + client.getFullName() +
                ", accountType=" + accountType +
                ", balance=" + balance +
                ", creationDate=" + creationDate +
                '}';
    }
}
