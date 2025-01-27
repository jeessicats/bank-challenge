package br.com.compass;

import br.com.compass.config.DatabaseConnection;
import br.com.compass.model.*;
import br.com.compass.repository.AccountRepository;
import br.com.compass.repository.ClientRepository;
import br.com.compass.repository.TransactionRepository;
import br.com.compass.service.AccountService;
import br.com.compass.service.ClientService;
import br.com.compass.util.ClientValidator;
import br.com.compass.util.JpaUtil;
import jakarta.persistence.EntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class App {

    private static Client loggedClient;
    private static TransactionRepository transactionRepository;
    private static AccountRepository accountRepository;

    public static void main(String[] args) {

        // Teste de conexão com o banco de dados
        System.out.println("Testing database connection...");
        DatabaseConnection.testConnection();

        // Inicializar EntityManager
        EntityManager em = JpaUtil.getEntityManager();

        // Inicializar os repositórios e serviços que precisarão do EntityManager
        accountRepository = new AccountRepository(em); // Use a variável estática
        AccountService accountService = new AccountService(em);
        transactionRepository = new TransactionRepository(em); // Use a variável estática

        Scanner scanner = new Scanner(System.in);

        mainMenu(scanner, accountRepository, accountService);

        scanner.close();

        em.close();
        JpaUtil.closeEntityManagerFactory();

        System.out.println("Application closed");
    }

    public static void mainMenu(Scanner scanner, AccountRepository accountRepository, AccountService accountService) {
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
                    loginMenu(scanner, accountRepository, accountService);
                    return;
                case 2:
                    accountOpeningMenu(scanner, accountRepository);
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
    public static void loginMenu(Scanner scanner, AccountRepository accountRepository, AccountService accountService) {
        System.out.println("========= Login Menu =========");

        boolean loginSuccessful = false;

        while (!loginSuccessful) {
            // Valida o CPF usando o método do ClientValidator
            System.out.print("Enter your CPF: ");
            String cpf = scanner.nextLine();

            if (!ClientValidator.isValidCpf(cpf)) {
                System.out.println("Invalid CPF format. Please try again.");
                continue; // Retorna ao início do loop para tentar novamente
            }

            try {
                // Busca o cliente associado ao CPF
                Client client = accountRepository.findClientFromAccountByCpf(cpf);

                if (client == null) {
                    System.out.println("No account found for CPF: " + cpf);
                    continue; // Retorna ao início do loop para tentar novamente
                }

                // CPF válido e cliente encontrado, solicita a senha
                System.out.print("Enter your password: ");
                String password = scanner.nextLine();

                // Valida a senha
                if (client.getPassword().equals(password)) {
                    loggedClient = client; // Define o cliente logado
                    System.out.println("Login successful! Welcome, " + client.getFullName() + "!");
                    bankMenu(scanner, accountRepository, accountService); // Redireciona para o menu bancário
                    loginSuccessful = true; // Finaliza o loop após login bem-sucedido
                } else {
                    System.out.println("Invalid password. Please try again.");
                }

            } catch (Exception e) {
                e.printStackTrace(); // Mostra o rastreamento de pilha completo para depuração
                System.err.println("Error during login: " + e.getMessage());
            }
        }
    }


    public static void bankMenu(Scanner scanner, AccountRepository accountRepository, AccountService accountService) {
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
                    handleDeposit(scanner, accountRepository, accountService);
                    break;
                case 2:
                    // ToDo...
                    System.out.println("Withdraw.");
                    handleWithdraw(scanner, accountRepository, accountService);
                    break;
                case 3:
                    // ToDo...
                    System.out.println("Check Balance.");
                    handleCheckBalance(scanner, accountRepository);
                    break;
                case 4:
                    // ToDo...
                    System.out.println("Transfer.");
                    handleTransfer(scanner, accountRepository, accountService);
                    break;
                case 5:
                    // ToDo...
                    System.out.println("Bank Statement.");
                    handleBankStatement(scanner, transactionRepository, accountRepository);
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
    public static void accountOpeningMenu(Scanner scanner, AccountRepository accountRepository) {
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
                    EntityManager em = JpaUtil.getEntityManager(); // Obtenha o EntityManager
                    ClientRepository clientRepository = new ClientRepository(em);

                    try {
                        System.out.println("========= Account Opening =========");

                        // Pede os dados do cliente usando os métodos da classe ClientService
                        String fullName = clientService.captureValidFullName(scanner);
                        LocalDate birthDate = clientService.captureValidBirthDate(scanner);
                        String cpf = clientService.captureValidCpf(scanner);
                        String phoneNumber = clientService.captureValidPhoneNumber(scanner);
                        String email = clientService.captureValidEmail(scanner);
                        String password = clientService.captureValidPassword(scanner);

                        // Verifica se a idade do cliente é válida (entre 18 e 100 anos)
                        if (!ClientValidator.isValidAge(birthDate)) {
                            System.out.println("Client must be between 18 and 100 years old to open an account.");
                            return; // Impede a criação da conta
                        }

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

    private static void handleDeposit(Scanner scanner, AccountRepository accountRepository, AccountService accountService) {
        if (loggedClient == null) {
            System.out.println("You need to log in first.");
            return;
        }

        try {
            // Buscar todas as contas associadas ao cliente logado
            List<Account> accounts = accountRepository.findAccountsByCpf(loggedClient.getCpf());
            if (accounts.isEmpty()) {
                System.out.println("No accounts found for this client.");
                return;
            }

            // Exibir as contas disponíveis
            for (int i = 0; i < accounts.size(); i++) {
                Account account = accounts.get(i);
                System.out.println((i + 1) + ". " + account.getAccountType() + " - Balance: " + account.getBalance());
            }

            // Solicitar ao usuário para selecionar uma conta
            System.out.print("Select an account: ");
            int accountChoice = scanner.nextInt();
            scanner.nextLine();  // Consome a nova linha pendente após nextInt()

            if (accountChoice < 1 || accountChoice > accounts.size()) {
                System.out.println("Invalid choice. Operation canceled.");
                return;
            }

            // Selecionar a conta escolhida pelo cliente
            Account selectedAccount = accounts.get(accountChoice - 1);

            // Solicitar o valor do depósito
            System.out.print("Enter the deposit amount: ");
            BigDecimal depositAmount = scanner.nextBigDecimal();
            scanner.nextLine(); // Consome a nova linha pendente após nextBigDecimal()

            // Realizar o depósito
            accountService.deposit(selectedAccount, depositAmount);

        } catch (Exception e) {
            System.out.println("An error occurred during the deposit: " + e.getMessage());
        }
    }

    private static void handleWithdraw(Scanner scanner, AccountRepository accountRepository, AccountService accountService) {
        if (loggedClient == null) {
            System.out.println("You need to log in first.");
            return;
        }

        try {
            // Buscar todas as contas associadas ao cliente logado
            List<Account> accounts = accountRepository.findAccountsByCpf(loggedClient.getCpf());
            if (accounts.isEmpty()) {
                System.out.println("No accounts found for this client.");
                return;
            }

            // Exibir as contas disponíveis
            for (int i = 0; i < accounts.size(); i++) {
                Account account = accounts.get(i);
                System.out.println((i + 1) + ". " + account.getAccountType() + " - Balance: " + account.getBalance());
            }

            // Solicitar ao usuário para selecionar uma conta
            System.out.print("Select an account: ");
            int accountChoice = scanner.nextInt();
            scanner.nextLine();  // Consome a nova linha pendente após nextInt()

            if (accountChoice < 1 || accountChoice > accounts.size()) {
                System.out.println("Invalid choice. Operation canceled.");
                return;
            }

            // Selecionar a conta escolhida pelo cliente
            Account selectedAccount = accounts.get(accountChoice - 1);

            // Solicitar o valor do saque
            System.out.print("Enter the withdraw amount: ");
            BigDecimal withdrawAmount = scanner.nextBigDecimal();
            scanner.nextLine(); // Consome a nova linha pendente após nextBigDecimal()

            // Realizar o saque
            accountService.withdraw(selectedAccount, withdrawAmount);

        } catch (Exception e) {
            System.out.println("An error occurred during the deposit: " + e.getMessage());
        }
    }

    private static void handleCheckBalance(Scanner scanner, AccountRepository accountRepository) {
        if (loggedClient == null) {
            System.out.println("You need to log in first.");
            return;
        }

        try {
            // Buscar todas as contas associadas ao cliente logado
            List<Account> accounts = accountRepository.findAccountsByCpf(loggedClient.getCpf());

            if (accounts.isEmpty()) {
                System.out.println("No accounts found for this client.");
                return;
            }

            // Caso o cliente tenha apenas uma conta, exibe o saldo diretamente
            if (accounts.size() == 1) {
                Account singleAccount = accounts.get(0);
                System.out.println("Account Type: " + singleAccount.getAccountType());
                System.out.println("Balance: " + singleAccount.getBalance());
                return;
            }

            // Caso o cliente tenha mais de uma conta, solicita a seleção
            System.out.println("You have multiple accounts. Please select one:");
            for (int i = 0; i < accounts.size(); i++) {
                Account account = accounts.get(i);
                System.out.println((i + 1) + ". " + account.getAccountType() + " - Balance: " + account.getBalance());
            }

            System.out.print("Select an account: ");
            int accountChoice = scanner.nextInt();
            scanner.nextLine();  // Consome a nova linha pendente após nextInt()

            if (accountChoice < 1 || accountChoice > accounts.size()) {
                System.out.println("Invalid choice. Operation canceled.");
                return;
            }

            Account selectedAccount = accounts.get(accountChoice - 1);
            System.out.println("Account Type: " + selectedAccount.getAccountType());
            System.out.println("Balance: " + selectedAccount.getBalance());

        } catch (Exception e) {
            System.out.println("An error occurred during the balance check: " + e.getMessage());
        }
    }

    private static void handleTransfer(Scanner scanner, AccountRepository accountRepository, AccountService accountService) {
        if (loggedClient == null) {
            System.out.println("You need to log in first.");
            return;
        }

        try {
            // Buscar todas as contas associadas ao cliente logado
            List<Account> accounts = accountRepository.findAccountsByCpf(loggedClient.getCpf());
            if (accounts.isEmpty()) {
                System.out.println("No accounts found for this client.");
                return;
            }

            Account sourceAccount;

            // Se o cliente tem apenas uma conta, ela será usada automaticamente
            if (accounts.size() == 1) {
                sourceAccount = accounts.get(0);
                System.out.println("Using your only account for transfer:");
                System.out.println("Account Type: " + sourceAccount.getAccountType());
                System.out.println("Balance: " + sourceAccount.getBalance());
            } else {
                // Caso tenha mais de uma conta, solicita que selecione uma conta de origem
                System.out.println("You have multiple accounts. Please select one:");
                for (int i = 0; i < accounts.size(); i++) {
                    Account account = accounts.get(i);
                    System.out.println((i + 1) + ". " + account.getAccountType() + " - Balance: " + account.getBalance());
                }

                System.out.print("Select an account to transfer from: ");
                int accountChoice = scanner.nextInt();
                scanner.nextLine(); // Consome a nova linha pendente

                if (accountChoice < 1 || accountChoice > accounts.size()) {
                    System.out.println("Invalid choice. Operation canceled.");
                    return;
                }

                sourceAccount = accounts.get(accountChoice - 1);
            }

            // Limpar buffer do scanner antes de capturar o CPF
            System.out.print("Enter recipient's CPF: ");
            scanner.nextLine(); // Consome a linha pendente do buffer
            String recipientCpf = scanner.nextLine().trim(); // Lê o CPF corretamente

            // Verificar se o CPF foi digitado corretamente
            if (recipientCpf.isEmpty()) {
                System.out.println("No CPF entered. Operation canceled.");
                return;
            }

            // Buscar contas associadas ao CPF do destinatário
            List<Account> recipientAccounts = accountRepository.findAccountsByCpf(recipientCpf);
            if (recipientAccounts.isEmpty()) {
                System.out.println("No accounts found for recipient CPF: " + recipientCpf);
                return;
            }

            Account destinationAccount;
            String recipientName; // Nome do destinatário

            // Se o destinatário tem apenas uma conta, ela será usada automaticamente
            if (recipientAccounts.size() == 1) {
                destinationAccount = recipientAccounts.get(0);
                recipientName = destinationAccount.getClient().getFullName(); // Obter nome do cliente associado
                System.out.println("Recipient's account:");
                System.out.println("Account Type: " + destinationAccount.getAccountType());
            } else {
                // Caso tenha mais de uma conta, solicita que selecione uma conta de destino
                System.out.println("Recipient has multiple accounts. Please select one:");
                for (int i = 0; i < recipientAccounts.size(); i++) {
                    Account account = recipientAccounts.get(i);
                    System.out.println((i + 1) + ". " + account.getAccountType());
                }

                System.out.print("Select a recipient's account: ");
                int recipientChoice = scanner.nextInt();
                scanner.nextLine(); // Consome a nova linha pendente

                if (recipientChoice < 1 || recipientChoice > recipientAccounts.size()) {
                    System.out.println("Invalid choice. Operation canceled.");
                    return;
                }

                destinationAccount = recipientAccounts.get(recipientChoice - 1);
                recipientName = destinationAccount.getClient().getFullName(); // Obter nome do cliente associado
            }

            // Solicitar o valor da transferência
            System.out.print("Enter the transfer amount: ");
            BigDecimal transferAmount = scanner.nextBigDecimal();
            scanner.nextLine(); // Consome a nova linha pendente

            // Realizar a transferência
            accountService.transfer(sourceAccount, destinationAccount, transferAmount);

            // Exibir mensagem de sucesso com o nome do destinatário
            System.out.println("Transfer completed successfully!");
            System.out.println("Transferred " + transferAmount + " from " + sourceAccount.getAccountType() +
                    " to " + destinationAccount.getAccountType() + " (Recipient: " + recipientName + ")");
        } catch (Exception e) {
            System.out.println("An error occurred during the transfer: " + e.getMessage());
        }
    }

    private static void handleBankStatement(Scanner scanner, TransactionRepository transactionRepository, AccountRepository accountRepository) {
        if (loggedClient == null) {
            System.out.println("You need to log in first.");
            return;
        }

        // Selecionar conta
        System.out.println("Fetching bank statement...");
        List<Account> accounts = accountRepository.findAccountsByCpf(loggedClient.getCpf());

        if (accounts.isEmpty()) {
            System.out.println("No accounts found for this client.");
            return;
        }

        // Exibir contas para seleção
        Account selectedAccount;
        if (accounts.size() == 1) {
            selectedAccount = accounts.get(0); // Apenas uma conta
            System.out.println("Using your only account:");
            System.out.println("Account Type: " + selectedAccount.getAccountType());
        } else {
            System.out.println("You have multiple accounts. Please select one:");
            for (int i = 0; i < accounts.size(); i++) {
                System.out.println((i + 1) + ". " + accounts.get(i).getAccountType());
            }
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consumir a linha pendente
            if (choice < 1 || choice > accounts.size()) {
                System.out.println("Invalid choice.");
                return;
            }
            selectedAccount = accounts.get(choice - 1);
        }

        // Recuperar e exibir o extrato
        List<Transaction> transactions = transactionRepository.getBankStatement(selectedAccount.getIdAccount());
        if (transactions.isEmpty()) {
            System.out.println("No transactions found for this account.");
        } else {
            // Cabeçalho do extrato
            System.out.println("Bank Statement for " + loggedClient.getFullName() + " - CPF: " + loggedClient.getCpf());
            System.out.println("----------------------------------");

            for (Transaction transaction : transactions) {
                System.out.println("Transaction ID: " + transaction.getIdTransaction());
                System.out.println("Type: " + transaction.getType());
                System.out.println("Amount: " + transaction.getAmount());
                System.out.println("Date: " + transaction.getTimestamp());

                // Exibir nome do destinatário no caso de transferência
                if (transaction.getType() == TransactionType.TRANSFER && transaction.getDestinationAccount() != null) {
                    Client destinationClient = transaction.getDestinationAccount().getClient();
                    if (destinationClient != null) {
                        System.out.println("To: " + destinationClient.getFullName() + " - CPF: " + destinationClient.getCpf());
                    } else {
                        System.out.println("To Account ID: " + transaction.getDestinationAccount().getIdAccount());
                    }
                }
                System.out.println("----------------------------------");
            }
        }
    }
}