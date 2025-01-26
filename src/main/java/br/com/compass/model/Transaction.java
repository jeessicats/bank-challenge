package br.com.compass.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private int idTransaction; // idTransaction
    private int sourceAccountId; // ID da conta de origem
    private Integer destinationAccountId; // Conta de destino (somente para transferências, pode ser null)
    private TransactionType type; // Tipo da transação (DEPOSIT, WITHDRAWAL, TRANSFER)
    private BigDecimal amount; // Valor da transação
    private LocalDateTime timestamp; // Data e hora da transação

    // Construtor sem id
    public Transaction(int sourceAccountId, Integer destinationAccountId, TransactionType type, BigDecimal amount, LocalDateTime timestamp) {
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    // Construtor completo
    public Transaction(int idTransaction, int sourceAccountId, Integer destinationAccountId, TransactionType type, BigDecimal amount, LocalDateTime timestamp) {
        this.idTransaction = idTransaction;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
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

    public int getSourceAccountId() {
        return sourceAccountId;
    }

    public void setSourceAccountId(int sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public Integer getDestinationAccountId() {
        return destinationAccountId;
    }

    public void setDestinationAccountId(Integer destinationAccountId) {
        this.destinationAccountId = destinationAccountId;
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
                ", sourceAccountId=" + sourceAccountId +
                ", destinationAccountId=" + destinationAccountId +
                ", type=" + type +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}
