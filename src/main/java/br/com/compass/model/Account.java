package br.com.compass.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Account {

    private int id;
    private final Client client; // Relacionamento com Cliente
    private final AccountType accountType; // Enum AccountType
    private BigDecimal balance; // BigDecimal para trabalhar com dinheiro
    private final LocalDateTime creationDate;

    // Construtor com campos obrigatórios
    public Account(Client client, AccountType accountType) {
        if (client == null) throw new IllegalArgumentException("Client cannot be null");
        if (accountType == null) throw new IllegalArgumentException("Account type cannot be null");

        this.client = client;
        this.accountType = accountType;
        this.balance = BigDecimal.ZERO;
        this.creationDate = LocalDateTime.now();
    }

    // Construtor completo
    public Account(int id, Client client, AccountType accountType, BigDecimal balance, LocalDateTime creationDate) {
        if (client == null) throw new IllegalArgumentException("Client cannot be null");
        if (accountType == null) throw new IllegalArgumentException("Account type cannot be null");
        if (balance == null || balance.compareTo(BigDecimal.ZERO) < 0) throw new IllegalArgumentException("Balance cannot be null or negative");

        this.id = id;
        this.client = client;
        this.accountType = accountType;
        this.balance = balance;
        this.creationDate = creationDate != null ? creationDate : LocalDateTime.now();
    }

    public int getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", clientName=" + client.getFullName() +
                ", accountType=" + accountType +
                ", balance=" + balance +
                ", creationDate=" + creationDate +
                '}';
    }
}
