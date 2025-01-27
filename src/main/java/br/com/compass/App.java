package br.com.compass;

import br.com.compass.config.DatabaseConnection;
import br.com.compass.model.Account;
import br.com.compass.model.AccountType;
import br.com.compass.model.Client;
import br.com.compass.repository.AccountRepository;
import br.com.compass.repository.ClientRepository;
import br.com.compass.repository.TransactionRepository;
import br.com.compass.service.ClientService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class App {

    private static Client loggedClient;

    public static void main(String[] args) {

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

                        // Valida CPF e senha
                        if (client != null && client.getPassword().equals(password)) {
                            loggedClient = client; // Define o cliente logado
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
                    handleDeposit(scanner);
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

            String input = scanner.nextLine();
            int option;

            try {
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                continue;
            }

            switch (option) {
                case 1:
                    ClientService clientService = new ClientService();
                    ClientRepository clientRepository = new ClientRepository();
                    AccountRepository accountRepository = new AccountRepository();

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

                        // Salvar cliente no banco de dados
                        if (clientRepository.save(client)) {
                            System.out.println("Client saved successfully!");
                        } else {
                            System.err.println("Failed to save client.");
                            break;
                        }

                        // Selecionar tipo de conta
                        System.out.println("Available account types:");
                        for (int i = 0; i < AccountType.values().length; i++) {
                            System.out.println((i + 1) + ". " + AccountType.values()[i]);
                        }

                        System.out.print("Select Account Type: ");
                        int typeOption = scanner.nextInt();
                        scanner.nextLine(); // Limpa o buffer do scanner

                        if (typeOption < 1 || typeOption > AccountType.values().length) {
                            System.out.println("Invalid option. Please try again.");
                            break; // Volta ao menu de abertura de conta
                        }

                        AccountType selectedAccountType = AccountType.values()[typeOption - 1];
                        Account account = new Account(client, selectedAccountType);

                        // Salvar conta no banco de dados
                        if (accountRepository.save(account)) {
                            System.out.println("\nAccount successfully created!");
                            System.out.println(account);
                        } else {
                            System.err.println("Failed to save account.");
                        }

                    } catch (Exception e) {
                        System.out.println("An unexpected error occurred: " + e.getMessage());
                    }
                    break;

                case 0:
                    System.out.println("Returning to Main Menu...");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    private static void handleDeposit(Scanner scanner) {
        if (loggedClient == null) {
            System.out.println("You need to log in first.");
            return;
        }

        // Inicializar os repositórios
        AccountRepository accountRepository = new AccountRepository();
        TransactionRepository transactionRepository = new TransactionRepository();

        // Buscar contas associadas ao cliente logado
        System.out.println("Select an account for deposit:");
        List<Account> accounts = accountRepository.findByCpf(loggedClient.getCpf());

        if (accounts.isEmpty()) {
            System.out.println("No accounts found for this client.");
            return;
        }

        // Exibir as contas disponíveis
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            System.out.println((i + 1) + ". " + account.getAccountType() + " - Balance: " + account.getBalance());
        }

        System.out.print("Select an account: ");
        int accountChoice = scanner.nextInt();
        scanner.nextLine(); // Limpa o buffer do scanner

        if (accountChoice < 1 || accountChoice > accounts.size()) {
            System.out.println("Invalid choice. Operation canceled.");
            return;
        }

        // Selecionar a conta escolhida
        Account selectedAccount = accounts.get(accountChoice - 1);

        // Solicitar o valor do depósito
        System.out.print("Enter the deposit amount: ");
        BigDecimal depositAmount;

        try {
            depositAmount = scanner.nextBigDecimal();
            scanner.nextLine(); // Limpa o buffer
        } catch (Exception e) {
            System.out.println("Invalid amount. Operation canceled.");
            return;
        }

        if (depositAmount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Deposit amount must be greater than zero.");
            return;
        }

        // Realizar o depósito
        try {
            selectedAccount.deposit(depositAmount, accountRepository, transactionRepository);
            System.out.println("Deposit successful. New balance: " + selectedAccount.getBalance());
        } catch (Exception e) {
            System.out.println("An error occurred during the deposit: " + e.getMessage());
        }
    }

}