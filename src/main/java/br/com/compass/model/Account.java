package br.com.compass.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Account {

    private int id;
    private Person person; // Relacionamento com Person
    private AccountType accountType;
    private BigDecimal balance;
    private AccountStatus status;
    private LocalDateTime creationDate;

    // Construtor padrão
    public Account() {
    }

    // Construtor simplificado (campos obrigatórios)
    public Account(Person person, AccountType accountType) {
        setPerson(person);
        setAccountType(accountType);
        this.balance = BigDecimal.ZERO;
        this.status = AccountStatus.ACTIVE;
        this.creationDate = LocalDateTime.now();
    }

    // Construtor completo
    public Account(int id, Person person, AccountType accountType, BigDecimal balance, AccountStatus status, LocalDateTime creationDate) {
        this.id = id;
        setPerson(person);
        setAccountType(accountType);
        setBalance(balance);
        setStatus(status);
        this.creationDate = creationDate != null ? creationDate : LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }
        this.person = person;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        if (accountType == null) {
            throw new IllegalArgumentException("Account type cannot be null");
        }
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        this.balance = balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Account status cannot be null");
        }
        this.status = status;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void deposit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }
        this.balance = this.balance.add(amount);
        System.out.println("Successfully deposited: " + amount);
    }

    public void withdrawal(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        } else if (this.balance.subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient balance for withdrawal");
        }
        this.balance = this.balance.subtract(amount);
        System.out.println("Successfully withdrawn: " + amount);
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
        this.withdrawal(transferAmount); // Saque da conta de origem
        destinationAccount.deposit(transferAmount); // Depósito na conta de destino

        // Exibir mensagem de transferência com nome e CPF
        System.out.println("Successfully transferred: " + transferAmount +
                "\nFrom: " + this.person.getName() + " (CPF: " + this.person.getCpf() + ")" +
                "\nTo: " + destinationAccount.getPerson().getName() + " (CPF: " + destinationAccount.getPerson().getCpf() + ")");
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", personName=" + person.getName() +
                ", accountType='" + accountType + '\'' +
                ", balance=" + balance +
                ", status='" + status + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
