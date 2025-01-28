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
            return null;  // Retorna null se não encontrar
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
            return query.getResultList();  // Retorna lista, pode ser vazia
        } catch (Exception e) {
            System.err.println("Error fetching accounts: " + e.getMessage());
            return Collections.emptyList();  // Retorna lista vazia em caso de erro
        }
    }

    // Salva uma conta no banco de dados
    public boolean save(Account account) {
        try {
            em.getTransaction().begin();
            em.persist(account);
            em.getTransaction().commit();
            return true;  // Sucesso na operação
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();  // Realiza rollback em caso de erro
            }
            System.err.println("Error saving account: " + e.getMessage());
            return false;  // Retorna false se falhar
        }
    }
}
