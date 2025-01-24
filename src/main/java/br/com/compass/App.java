package br.com.compass;

import br.com.compass.model.Account;
import br.com.compass.model.AccountType;
import br.com.compass.model.Client;

import java.time.LocalDate;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        mainMenu(scanner);

        scanner.close();
        System.out.println("Application closed");
    }

    public static void mainMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("========= Main Menu =========");
            System.out.println("|| 1. Login                ||");
            System.out.println("|| 2. Account Opening      ||");
            System.out.println("|| 0. Exit                 ||");
            System.out.println("=============================");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    bankMenu(scanner);
                    return;
                case 2:
                    // ToDo...
                    accountOpeningMenu(scanner);
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    public static void bankMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("========= Bank Menu =========");
            System.out.println("|| 1. Deposit              ||");
            System.out.println("|| 2. Withdraw             ||");
            System.out.println("|| 3. Check Balance        ||");
            System.out.println("|| 4. Transfer             ||");
            System.out.println("|| 5. Bank Statement       ||");
            System.out.println("|| 0. Exit                 ||");
            System.out.println("=============================");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    // ToDo...
                    System.out.println("Deposit.");
                    break;
                case 2:
                    // ToDo...
                    System.out.println("Withdraw.");
                    break;
                case 3:
                    // ToDo...
                    System.out.println("Check Balance.");
                    break;
                case 4:
                    // ToDo...
                    System.out.println("Transfer.");
                    break;
                case 5:
                    // ToDo...
                    System.out.println("Bank Statement.");
                    break;
                case 0:
                    // ToDo...
                    System.out.println("Exiting...");
                    running = false;
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    public static void accountOpeningMenu(Scanner scanner) {
        System.out.println("========= Account Opening =========");

        try {
            // Solicitação dos dados do cliente
            System.out.print("Enter your full name: ");
            scanner.nextLine(); // Consome quebra de linha pendente
            String fullName = scanner.nextLine();

            System.out.print("Enter your birth date (yyyy-MM-dd): ");
            LocalDate birthDate = LocalDate.parse(scanner.nextLine());

            System.out.print("Enter your CPF (11 digits): ");
            String cpf = scanner.nextLine();

            System.out.print("Enter your phone number: ");
            String phoneNumber = scanner.nextLine();

            System.out.print("Enter your email: ");
            String email = scanner.nextLine();

            // Solicitação do endereço
            System.out.println("Address");
            System.out.print("Enter your street name: ");
            String streetName = scanner.nextLine();

            System.out.print("Enter your street number: ");
            int streetNumber = scanner.nextInt();
            scanner.nextLine(); // Consome quebra de linha pendente

            System.out.print("Enter your neighborhood: ");
            String neighborhood = scanner.nextLine();

            System.out.print("Enter your postal code (DDDDD-DDD): ");
            String postalCode = scanner.nextLine();

            System.out.print("Enter your city: ");
            String city = scanner.nextLine();

            System.out.print("Enter your state: ");
            String state = scanner.nextLine();

            System.out.print("Enter your country: ");
            String country = scanner.nextLine();

            // Criar o cliente com os novos atributos
            Client client = new Client(fullName, birthDate, cpf, phoneNumber, email,
                    streetName, streetNumber, neighborhood, postalCode, city, state, country);

            // Solicitar o tipo de conta
            System.out.println("Available account types: ");
            for (int i = 0; i < AccountType.values().length; i++) {
                System.out.println((i + 1) + ". " + AccountType.values()[i]);
            }

            System.out.print("Select Account Type: ");
            int typeOption = scanner.nextInt();

            if (typeOption < 1 || typeOption > AccountType.values().length) {
                System.out.println("Invalid option. Please try again.");
                return;
            }

            AccountType selectedAccountType = AccountType.values()[typeOption - 1];
            System.out.println("You selected: " + selectedAccountType);

            // Criar a conta
            Account account = new Account(client, selectedAccountType);

            // Exibir os dados da conta criada
            System.out.println("\nAccount successfully created!");
            System.out.println(account);

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred. Please try again.");
        }
    }
}
