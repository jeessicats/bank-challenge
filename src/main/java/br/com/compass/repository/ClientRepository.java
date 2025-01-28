package br.com.compass.repository;

import br.com.compass.model.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class ClientRepository {

    private final EntityManager em;

    // Construtor com injeção de EntityManager
    public ClientRepository(EntityManager entityManager) {
        this.em = entityManager;
    }

    // Salva um cliente no banco de dados
    public boolean save(Client client) {
        try {
            // Verifica se o CPF já existe em um loop
            while (isCpfAlreadyExists(client.getCpf())) {
                System.out.println("A client with this CPF already exists. Please enter a unique CPF.");
                return false; // Retorna false caso o CPF já exista
            }

            // Inicia transação e persiste o cliente
            em.getTransaction().begin();
            em.persist(client);
            em.getTransaction().commit();
            return true;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error saving client: " + e.getMessage());
            return false;
        }
    }

    // Verifica se o CPF já existe
    private boolean isCpfAlreadyExists(String cpf) {
        String jpql = "SELECT COUNT(c) FROM Client c WHERE c.cpf = :cpf";
        TypedQuery<Long> query = em.createQuery(jpql, Long.class);
        query.setParameter("cpf", cpf);
        Long count = query.getSingleResult();
        return count > 0;
    }
}
