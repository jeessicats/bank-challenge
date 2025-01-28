package br.com.compass.service;

import br.com.compass.model.Client;
import br.com.compass.repository.ClientRepository;
import br.com.compass.util.ClientValidator;
import br.com.compass.util.JpaUtil;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ClientService {

    private final ClientRepository clientRepository;

    // Alteração: Inicializando o ClientRepository com o EntityManager
    public ClientService() {
        EntityManager em = JpaUtil.getEntityManager();  // Obter o EntityManager
        this.clientRepository = new ClientRepository(em); // Passar o EntityManager para o ClientRepository
    }

    // Os outros métodos permanecem inalterados
    public String captureValidFullName(Scanner scanner) {
        while (true) {
            System.out.print("Enter your full name: ");
            String fullName = scanner.nextLine();
            if (ClientValidator.isNotNullOrEmpty(fullName)) {
                return fullName;
            }
            System.out.println("Full name cannot be null or empty. Please try again.");
        }
    }

    public LocalDate captureValidBirthDate(Scanner scanner) {
        while (true) {
            System.out.print("Enter your birth date (yyyy-MM-dd): ");
            try {
                String birthDateInput = scanner.nextLine();
                LocalDate birthDate = LocalDate.parse(birthDateInput);

                // Validação: Data não pode ser nula
                if (!ClientValidator.isValidDate(birthDate)) {
                    System.out.println("Birth date cannot be null.");
                    continue;
                }

                // Validação: Data não pode estar no futuro
                if (!ClientValidator.isNotInFuture(birthDate)) {
                    System.out.println("Birth date cannot be in the future.");
                    continue;
                }

                // Verifica se a idade do cliente é válida (entre 18 e 100 anos)
                if (!ClientValidator.isValidAge(birthDate)) {
                    System.out.println("Client must be between 18 and 100 years old to open an account.");
                    continue;
                }

                return birthDate;

            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Please enter the date in yyyy-MM-dd format.");
            }
        }
    }

    public String captureValidPhoneNumber(Scanner scanner) {
        while (true) {
            System.out.print("Enter your phone number with the local code (10 or 11 digits, numbers only): ");
            String phoneNumber = scanner.nextLine();
            if (ClientValidator.isValidPhoneNumber(phoneNumber)) {
                return phoneNumber;
            }
            System.out.println("Invalid phone number. Please try again.");
        }
    }

    public String captureValidEmail(Scanner scanner) {
        while (true) {
            System.out.print("Enter your email: ");
            String email = scanner.nextLine();
            if (ClientValidator.isValidEmail(email)) {
                return email;
            }
            System.out.println("Invalid email format. Please try again.");
        }
    }

    public String captureValidPassword(Scanner scanner) {
        while (true) {
            System.out.print("Enter your password (at least 8 characters, including 1 uppercase letter and 1 number): ");
            String password = scanner.nextLine();
            if (ClientValidator.isValidPassword(password)) {
                return password;
            }
            System.out.println("Password does not meet the required criteria. Please try again.");
        }
    }

    public Client captureClientDetails(Scanner scanner) {
        String fullName = captureValidFullName(scanner);
        LocalDate birthDate = captureValidBirthDate(scanner);
        String cpf = ClientValidator.promptForValidCpf(scanner);
        String phoneNumber = captureValidPhoneNumber(scanner);
        String email = captureValidEmail(scanner);
        String password = captureValidPassword(scanner);

        return new Client(fullName, birthDate, cpf, phoneNumber, email, password);
    }

}
