package br.com.compass.repository;

import br.com.compass.model.Transaction;
import br.com.compass.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class TransactionRepository {

    // Recupera o extrato bancário de uma conta específica
    public List<Transaction> getBankStatement(int accountId) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String jpql = "SELECT t FROM Transaction t WHERE t.sourceAccount.idAccount = :accountId OR t.destinationAccount.idAccount = :accountId ORDER BY t.timestamp DESC";
            TypedQuery<Transaction> query = em.createQuery(jpql, Transaction.class);
            query.setParameter("accountId", accountId);
            return query.getResultList();
        } finally {
            em.close(); // Garante o fechamento do EntityManager
        }
    }

    // Salva uma transação no banco de dados
    public void save(Transaction transaction) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(transaction);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Failed to save transaction: " + e.getMessage(), e);
        } finally {
            em.close(); // Garante o fechamento do EntityManager
        }
    }
}
