package br.com.compass.model;

import br.com.compass.repository.AccountRepository;
import br.com.compass.repository.TransactionRepository;

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
    public void deposit(BigDecimal amount, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }
        this.balance = this.balance.add(amount);

        // Atualizar saldo no banco de dados
        if (!accountRepository.updateBalance(this.idAccount, this.balance)) {
            throw new RuntimeException("Failed to update balance in the database");
        }

        // Registrar a transação no banco de dados
        Transaction depositTransaction = new Transaction(
                this.idAccount, // Conta de origem (a própria conta)
                null,           // Conta de destino (nula para depósito)
                TransactionType.DEPOSIT,
                amount,
                LocalDateTime.now()
        );
        if (!transactionRepository.save(depositTransaction)) {
            throw new RuntimeException("Failed to save deposit transaction");
        }

        System.out.println("Deposit successful. New balance: " + this.balance);
    }

    public void withdrawal(BigDecimal amount, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }
        if (this.balance.subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient balance for withdrawal");
        }
        this.balance = this.balance.subtract(amount);

        // Atualizar saldo no banco de dados
        if (!accountRepository.updateBalance(this.idAccount, this.balance)) {
            throw new RuntimeException("Failed to update balance in the database");
        }

        // Registrar a transação no banco de dados
        Transaction withdrawalTransaction = new Transaction(
                this.idAccount, // Conta de origem
                null,           // Conta de destino (nula para saque)
                TransactionType.WITHDRAWAL,
                amount,
                LocalDateTime.now()
        );
        if (!transactionRepository.save(withdrawalTransaction)) {
            throw new RuntimeException("Failed to save withdrawal transaction");
        }

        System.out.println("Withdrawal successful. New balance: " + this.balance);
    }

    public void transfer(Account destinationAccount, BigDecimal transferAmount, AccountRepository accountRepository, TransactionRepository transactionRepository) {
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

        // Realizar transferência
        this.withdrawal(transferAmount, accountRepository, transactionRepository);
        destinationAccount.deposit(transferAmount, accountRepository, transactionRepository);

        // Registrar a transação no banco
        Transaction outgoingTransaction = new Transaction(
                this.idAccount,                         // Conta de origem
                destinationAccount.getIdAccount(),      // Conta de destino
                TransactionType.TRANSFER,
                transferAmount,
                LocalDateTime.now()
        );
        if (!transactionRepository.save(outgoingTransaction)) {
            throw new RuntimeException("Failed to save transaction");
        }

        System.out.println("Transfer successful. New balance: " + this.balance);
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
