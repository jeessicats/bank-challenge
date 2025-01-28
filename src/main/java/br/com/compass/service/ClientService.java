package br.com.compass.service;

import br.com.compass.model.Client;
import br.com.compass.repository.ClientRepository;
import br.com.compass.util.ClientValidator;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ClientService {

    private final ClientRepository clientRepository;
    private final EntityManager em;

    // Construtor com injeção do EntityManager
    public ClientService(EntityManager em) {
        this.em = em;
        this.clientRepository = new ClientRepository(em);
    }

    // Captura um nome válido do usuário
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

    // Captura e valida a data de nascimento do cliente
    public LocalDate captureValidBirthDate(Scanner scanner) {
        while (true) {
            System.out.print("Enter your birth date (yyyy-MM-dd): ");
            try {
                String birthDateInput = scanner.nextLine();
                LocalDate birthDate = LocalDate.parse(birthDateInput);

                if (!ClientValidator.isValidDate(birthDate)) {
                    System.out.println("Birth date cannot be null.");
                    continue;
                }

                if (!ClientValidator.isNotInFuture(birthDate)) {
                    System.out.println("Birth date cannot be in the future.");
                    continue;
                }

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

    // Captura e valida o número de telefone
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

    // Captura e valida o email do usuário
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

    // Captura e valida a senha do usuário
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

    // Captura os dados do cliente e retorna um objeto Client
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
