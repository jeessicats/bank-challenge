package br.com.compass.repository;

import br.com.compass.model.Account;
import br.com.compass.model.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;

import java.util.Collections;
import java.util.List;

public class AccountRepository {

    private EntityManager em;

    // Construtor com injeção de EntityManager
    public AccountRepository(EntityManager em) {
        this.em = em;
    }

    // Busca cliente pelo CPF associado à conta
    public Client findClientFromAccountByCpf(String cpf) {
        try {
            String jpql = "SELECT c FROM Client c WHERE c.cpf = :cpf";
            TypedQuery<Client> query = em.createQuery(jpql, Client.class);
            query.setParameter("cpf", cpf);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (NonUniqueResultException e) {
            throw new IllegalStateException("Multiple clients found with the same CPF.");
        }
    }

    // Recupera todas as contas associadas ao CPF
    public List<Account> findAccountsByCpf(String cpf) {
        try {
            String jpql = "SELECT a FROM Account a WHERE a.client.cpf = :cpf";
            TypedQuery<Account> query = em.createQuery(jpql, Account.class);
            query.setParameter("cpf", cpf);
            return query.getResultList();
        } catch (Exception e) {
            System.err.println("Error fetching accounts: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Salva uma conta no banco de dados
    public boolean save(Account account) {
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
        }
    }
}
