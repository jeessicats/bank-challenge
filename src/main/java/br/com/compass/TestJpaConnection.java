package br.com.compass;

import br.com.compass.model.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class TestJpaConnection {
    public static void main(String[] args) {
        // Criar EntityManagerFactory e EntityManager
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
        EntityManager em = emf.createEntityManager();

        try {
            System.out.println("Testing JPA connection...");

            // Teste simples: criar e salvar um cliente
            em.getTransaction().begin();

            Client client = new Client();
            client.setFullName("John Doe");
            client.setCpf("12345678901");
            client.setEmail("john.doe@example.com");
            client.setPhoneNumber("5551234567");
            client.setPassword("Password123");
            client.setBirthDate(java.time.LocalDate.of(1990, 1, 1));

            em.persist(client);

            em.getTransaction().commit();

            System.out.println("Client saved successfully!");
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("Error: " + e.getMessage());
        } finally {
            em.close();
            emf.close();
        }
    }
}