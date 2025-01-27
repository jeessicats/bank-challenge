package br.com.compass.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Geração automática do ID
    @Column(name = "id_account")
    private int idAccount; // idAccount

    @ManyToOne(fetch = FetchType.EAGER)  // Alterar de LAZY para EAGER
    @JoinColumn(name = "id_client", nullable = false)  // Relacionamento com a tabela clients
    private Client client;

    @Enumerated(EnumType.STRING) // Salvar o enum como String no banco de dados
    @Column(name = "account_type", nullable = false)
    private AccountType accountType; // Enum AccountType

    @Column(nullable = false)
    private BigDecimal balance; // BigDecimal para trabalhar com dinheiro

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    // Construtor vazio
    public Account() {
    }

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

    public void setClient(Client client) {
        this.client = client;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    // Métodos de comparação e representação
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return idAccount == account.idAccount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAccount);
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
