package br.com.compass.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Account {

    private int id;
    private Person person; // Relacionamento com Person
    private String accountType;
    private BigDecimal balance;
    private String status;
    private LocalDateTime creationDate;

    // Construtor padrão
    public Account() {
    }

    // Construtor simplificado (campos obrigatórios)
    public Account(Person person, String accountType) {
        setPerson(person);
        setAccountType(accountType);
        this.balance = BigDecimal.ZERO;
        this.status = "ACTIVE";
        this.creationDate = LocalDateTime.now();
    }

    // Construtor completo
    public Account(int id, Person person, String accountType, BigDecimal balance, String status, LocalDateTime creationDate) {
        this.id = id;
        this.person = person;
        this.accountType = accountType;
        this.balance = balance;
        this.status = status;
        this.creationDate = creationDate;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        if (accountType == null || accountType.isEmpty()) {
            throw new IllegalArgumentException("Account type cannot be null or empty");
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (!status.equals("ACTIVE") && !status.equals("BLOCKED") && !status.equals("CLOSED")) {
            throw new IllegalArgumentException("Invalid status");
        }
        this.status = status;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
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
