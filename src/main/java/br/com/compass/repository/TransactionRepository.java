package br.com.compass.repository;

import br.com.compass.model.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class TransactionRepository {

    private final EntityManager em;

    // Construtor com injeção de EntityManager
    public TransactionRepository(EntityManager entityManager) {
        this.em = entityManager;
    }

    // Recupera o extrato bancário de uma conta específica
    public List<Transaction> getBankStatement(int accountId) {
        try {
            String jpql = "SELECT t FROM Transaction t WHERE t.sourceAccount.idAccount = :accountId OR t.destinationAccount.idAccount = :accountId ORDER BY t.timestamp DESC";
            TypedQuery<Transaction> query = em.createQuery(jpql, Transaction.class);
            query.setParameter("accountId", accountId);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error retrieving bank statement: " + e.getMessage());
            return null;
        }
    }

    // Salva uma transação no banco de dados
    public void save(Transaction transaction) {
        try {
            em.getTransaction().begin();
            em.persist(transaction);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to save transaction: " + e.getMessage(), e);
        }
    }
}
