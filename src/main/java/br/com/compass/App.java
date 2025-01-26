package br.com.compass;

import br.com.compass.config.DatabaseConnection;
import br.com.compass.model.Account;
import br.com.compass.model.AccountType;
import br.com.compass.model.Client;
import br.com.compass.repository.ClientRepository;
import br.com.compass.service.ClientService;
import br.com.compass.util.DatabaseUtil;

import java.time.LocalDate;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        // Limpar tabelas antes do teste
        DatabaseUtil.clearAllTables();

        // Teste de conexão com o banco de dados
        System.out.println("Testing database connection...");
        DatabaseConnection.testConnection();

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
            scanner.nextLine(); // Consome a nova linha pendente após nextInt()

            switch (option) {
                case 1:
                    loginMenu(scanner);
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

    // Menu login
    public static void loginMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("========= Login Menu =========");
            System.out.println("|| 1. Enter CPF and Password ||");
            System.out.println("|| 0. Back to Main Menu      ||");
            System.out.println("=============================");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // Consome a nova linha pendente após nextInt()

            switch (option) {
                case 1:
                    System.out.print("Enter your CPF: ");
                    String cpf = scanner.nextLine();

                    System.out.print("Enter your password: ");
                    String password = scanner.nextLine();

                    try {
                        ClientRepository clientRepository = new ClientRepository();
                        Client client = clientRepository.findByCpf(cpf);

                        if (client != null && client.getPassword().equals(password)) {
                            System.out.println("Login successful! Welcome, " + client.getFullName() + "!");
                            bankMenu(scanner); // Redireciona para o menu bancário
                            return; // Sai do menu de login
                        } else {
                            System.out.println("Invalid CPF or password. Please try again.");
                        }
                    } catch (Exception e) {
                        System.err.println("Error during login: " + e.getMessage());
                    }
                    break;

                case 0:
                    System.out.println("Returning to Main Menu...");
                    running = false; // Sai do menu de login e volta ao menu principal
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

    // Menu de abertura de conta
    public static void accountOpeningMenu(Scanner scanner) {
        boolean running = true;

        while (running) {
            System.out.println("========= Account Opening Menu =========");
            System.out.println("|| 1. Open a New Account              ||");
            System.out.println("|| 0. Back to Main Menu               ||");
            System.out.println("========================================");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // Consome a nova linha pendente após nextInt()

            switch (option) {
                case 1:
                    ClientService clientService = new ClientService();

                    try {
                        System.out.println("========= Account Opening =========");

                        // Pede os dados do cliente usando os métodos da classe ClientService
                        String fullName = clientService.captureValidFullName(scanner);
                        LocalDate birthDate = clientService.captureValidBirthDate(scanner);
                        String cpf = clientService.captureValidCpf(scanner);
                        String phoneNumber = clientService.captureValidPhoneNumber(scanner);
                        String email = clientService.captureValidEmail(scanner);
                        String password = clientService.captureValidPassword(scanner);

                        // Criar o objeto Cliente com os dados validados
                        Client client = new Client(fullName, birthDate, cpf, phoneNumber, email, password);

                        // Selecionar tipo de conta
                        System.out.println("Available account types:");
                        for (int i = 0; i < AccountType.values().length; i++) {
                            System.out.println((i + 1) + ". " + AccountType.values()[i]);
                        }

                        System.out.print("Select Account Type: ");
                        int typeOption = scanner.nextInt();

                        if (typeOption < 1 || typeOption > AccountType.values().length) {
                            System.out.println("Invalid option. Please try again.");
                            break; // Volta ao menu de abertura de conta
                        }

                        AccountType selectedAccountType = AccountType.values()[typeOption - 1];
                        Account account = new Account(client, selectedAccountType);

                        // Exibir dados da conta criada
                        System.out.println("\nAccount successfully created!");
                        System.out.println(account);

                    } catch (Exception e) {
                        System.out.println("An unexpected error occurred: " + e.getMessage());
                    }
                    break;

                case 0:
                    System.out.println("Returning to Main Menu...");
                    running = false; // Sai do menu de abertura de conta
                    break;

                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }
}
