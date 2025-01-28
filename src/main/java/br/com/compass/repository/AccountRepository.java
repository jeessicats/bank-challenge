package br.com.compass.repository;

import br.com.compass.model.Account;
import br.com.compass.model.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class AccountRepository {

    private final EntityManager em;

    // Construtor recebe o EntityManager injetado
    public AccountRepository(EntityManager entityManager) {
        this.em = entityManager;
    }

    // Busca contas pelo CPF associado ao cliente
    public Client findClientFromAccountByCpf(String cpf) {
        String jpql = "SELECT c FROM Client c WHERE c.cpf = :cpf"; // Busca diretamente o cliente pelo CPF
        TypedQuery<Client> query = em.createQuery(jpql, Client.class);
        query.setParameter("cpf", cpf);

        try {
            return query.getSingleResult(); // Retorna o cliente único associado ao CPF
        } catch (NoResultException e) {
            return null; // Retorna null se nenhum cliente for encontrado
        } catch (NonUniqueResultException e) {
            throw new IllegalStateException("Multiple clients found with the same CPF."); // Exceção se houver duplicados
        }
    }

    // Metodo para recuperar as contas associadas ao CPF:
    public List<Account> findAccountsByCpf(String cpf) {
        String jpql = "SELECT a FROM Account a WHERE a.client.cpf = :cpf";  // Consulta para buscar contas pelo CPF
        TypedQuery<Account> query = em.createQuery(jpql, Account.class);
        query.setParameter("cpf", cpf);  // Define o valor do parâmetro CPF na consulta
        return query.getResultList();  // Retorna a lista de contas associadas ao CPF
    }

    // Salva uma conta no banco
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

    // Fecha o EntityManager
    public void close() {
        if (em.isOpen()) {
            em.close();
        }
    }
}
