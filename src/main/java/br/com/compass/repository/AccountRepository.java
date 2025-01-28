package br.com.compass.repository;

import br.com.compass.model.Account;
import br.com.compass.model.Client;
import br.com.compass.util.JpaUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class AccountRepository {

    // Busca contas pelo CPF associado ao cliente
    public Client findClientFromAccountByCpf(String cpf) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String jpql = "SELECT c FROM Client c WHERE c.cpf = :cpf";
            TypedQuery<Client> query = em.createQuery(jpql, Client.class);
            query.setParameter("cpf", cpf);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new IllegalStateException("Multiple clients found with the same CPF.");
        } finally {
            em.close(); // Garante o fechamento do EntityManager
        }
    }

    // Recupera contas associadas ao CPF
    public List<Account> findAccountsByCpf(String cpf) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            String jpql = "SELECT a FROM Account a WHERE a.client.cpf = :cpf";
            TypedQuery<Account> query = em.createQuery(jpql, Account.class);
            query.setParameter("cpf", cpf);
            return query.getResultList();
        } finally {
            em.close(); // Garante o fechamento do EntityManager
        }
    }

    // Salva uma conta no banco
    public boolean save(Account account) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(account);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error saving account: " + e.getMessage());
            return false;
        } finally {
            em.close(); // Garante o fechamento do EntityManager
        }
    }
}
