package br.com.compass.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private int transactionId; // ID da transação
    private int sourceAccountId; // ID da conta de origem
    private Integer destinationAccountId; // Conta de destino (somente para transferências, pode ser null)
    private TransactionType type; // Tipo da transação (DEPOSIT, WITHDRAWAL, TRANSFER)
    private BigDecimal amount; // Valor da transação
    private LocalDateTime timestamp; // Data e hora da transação

    public Transaction() {
    }

    // Construtor completo
    public Transaction(int transactionId, int sourceAccountId, Integer destinationAccountId, TransactionType type, BigDecimal amount, LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
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
                "transactionId=" + transactionId +
                ", sourceAccountId=" + sourceAccountId +
                ", destinationAccountId=" + destinationAccountId +
                ", type=" + type +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}
