package br.com.compass.repository;

import br.com.compass.model.Client;
import jakarta.persistence.EntityManager;

public class ClientRepository {

    private final EntityManager em;

    // Construtor recebe o EntityManager injetado
    public ClientRepository(EntityManager entityManager) {
        this.em = entityManager;
    }

    // Salva um cliente no banco
    public boolean save(Client client) {
        try {
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

    // Fecha o EntityManager
    public void close() {
        if (em.isOpen()) {
            em.close();
        }
    }
}
