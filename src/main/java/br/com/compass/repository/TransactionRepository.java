package br.com.compass.repository;

import br.com.compass.model.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class TransactionRepository {

    private final EntityManager em;

    public TransactionRepository(EntityManager em) {
        this.em = em;
    }

    public List<Transaction> getBankStatement(int accountId) {
        String jpql = "SELECT t FROM Transaction t WHERE t.sourceAccount.idAccount = :accountId OR t.destinationAccount.idAccount = :accountId ORDER BY t.timestamp DESC";
        TypedQuery<Transaction> query = em.createQuery(jpql, Transaction.class);
        query.setParameter("accountId", accountId);
        return query.getResultList();
    }

    public void save(Transaction transaction) {
        em.getTransaction().begin();
        em.persist(transaction);
        em.getTransaction().commit();
    }
}
