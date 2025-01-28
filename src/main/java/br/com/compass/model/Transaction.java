package br.com.compass.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "transactionlogs")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Geração automática do ID
    @Column(name = "id_transaction")
    private int idTransaction;

    @ManyToOne(fetch = FetchType.EAGER, optional = false) // Relacionamento com a conta de origem
    @JoinColumn(name = "source_account_id", nullable = false) // Chave estrangeira
    private Account sourceAccount;

    @ManyToOne(fetch = FetchType.EAGER) // Relacionamento com a conta de destino (pode ser null)
    @JoinColumn(name = "destination_account_id")
    private Account destinationAccount;

    @Enumerated(EnumType.STRING) // Salvar o enum como String no banco de dados
    @Column(name = "transaction_type", nullable = false)
    private TransactionType type;

    @Column(name = "amount", nullable = false, scale = 2, precision = 20) // BigDecimal configurado
    private BigDecimal amount;

    @Column(nullable = false, name = "transaction_date")
    private LocalDateTime timestamp;

    // Construtor vazio
    public Transaction() {
    }

    // Construtor sem ID
    public Transaction(Account sourceAccount, Account destinationAccount, TransactionType type, BigDecimal amount, LocalDateTime timestamp) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    // Getters e setters
    public int getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(int idTransaction) {
        this.idTransaction = idTransaction;
    }

    public Account getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(Account sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public Optional<Account> getDestinationAccount() {
        return Optional.ofNullable(destinationAccount); // Usando Optional para tratar destino nulo
    }

    public void setDestinationAccount(Account destinationAccount) {
        this.destinationAccount = destinationAccount;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "idTransaction=" + idTransaction +
                ", sourceAccount=" + (sourceAccount != null ? sourceAccount.getIdAccount() : "null") +
                ", destinationAccount=" + (destinationAccount != null ? destinationAccount.getIdAccount() : "null") +
                ", type=" + type +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return idTransaction == that.idTransaction;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTransaction);
    }
}
