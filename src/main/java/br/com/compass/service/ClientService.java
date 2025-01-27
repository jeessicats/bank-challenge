package br.com.compass.service;

import br.com.compass.repository.ClientRepository;
import br.com.compass.util.ClientValidator;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService() {
        this.clientRepository = new ClientRepository();
    }

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

                // Validação: Cliente deve ter pelo menos 18 anos
                if (!ClientValidator.isAtLeastAge(birthDate, 18)) {
                    System.out.println("Client must be at least 18 years old.");
                    continue;
                }

                return birthDate;

            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Please enter the date in yyyy-MM-dd format.");
            }
        }
    }

    public String captureValidCpf(Scanner scanner) {
        while (true) {
            System.out.print("Enter your CPF (11 digits, numbers only): ");
            String cpf = scanner.nextLine();
            if (ClientValidator.isValidCpf(cpf)) {
                return cpf;
            }
            System.out.println("Invalid CPF format. Please try again.");
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
}