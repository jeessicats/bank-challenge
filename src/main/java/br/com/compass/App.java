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
import java.util.*;

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
            System.out.println();
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
        System.out.println();
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
            System.out.println();
            System.out.println("========= Bank Menu =========");
            System.out.println("|| 1. Deposit              ||");
            System.out.println("|| 2. Withdraw             ||");
            System.out.println("|| 3. Check Balance        ||");
            System.out.println("|| 4. Transfer             ||");
            System.out.println("|| 5. Bank Statement       ||");
            System.out.println("|| 6. Create new account   ||");
            System.out.println("|| 0. Back to Main Menu    ||");
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
                case 6:
                    System.out.println("Create new account.");
                    handleAccountCreation(scanner, accountRepository);
                    break;
                case 0:
                    // ToDo...
                    System.out.println("Returning to Main Menu...");
                    mainMenu(scanner, accountRepository, accountService);
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    // Menu de abertura de conta
    public static void accountOpeningMenu(Scanner scanner, AccountRepository accountRepository) {
        System.out.println();
        System.out.println("========= Account Opening Menu =========");

        boolean running = true;

        while (running) {
            try {
                ClientService clientService = new ClientService();
                EntityManager em = JpaUtil.getEntityManager(); // Obtenha o EntityManager
                ClientRepository clientRepository = new ClientRepository(em);

                System.out.println("Please enter the following details to open a new account:");

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
                    continue;
                }

                // Criar o objeto Cliente com os dados validados
                Client client = new Client(fullName, birthDate, cpf, phoneNumber, email, password);

                // Salvar cliente no banco de dados
                if (clientRepository.save(client)) {
                    System.out.println("Client saved successfully!");
                } else {
                    System.err.println("Failed to save client.");
                    continue;
                }

                // Usar o método `selectAccountType` para selecionar o tipo de conta
                List<AccountType> allAccountTypes = Arrays.asList(AccountType.values());
                AccountType selectedAccountType = selectAccountType(scanner, allAccountTypes);

                // Criar a conta
                Account account = new Account(client, selectedAccountType);

                // Salvar conta no banco de dados
                if (accountRepository.save(account)) {
                    System.out.println("\nAccount successfully created!");
                    System.out.println("Account Details:");
                    System.out.println("Client Name: " + client.getFullName());
                    System.out.println("Account Type: " + selectedAccountType);
                    System.out.println("Balance: " + account.getBalance());
                    System.out.println("Creation Date: " + account.getCreationDate());
                    running = false; // Finaliza o loop após criar a conta
                } else {
                    System.err.println("Failed to save account.");
                }

            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private static void handleDeposit(Scanner scanner, AccountRepository accountRepository, AccountService accountService) {
        if (loggedClient == null) {
            System.out.println("You need to log in first.");
            return;
        }

        try {
            Account selectedAccount = selectAccount(scanner, accountRepository);
            if (selectedAccount == null) return;

            BigDecimal depositAmount;
            while (true) {
                System.out.print("Enter the deposit amount: ");
                depositAmount = scanner.nextBigDecimal();
                scanner.nextLine();

                if (depositAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Deposit amount must be greater than zero.");
                } else {
                    break;
                }
            }

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
            // Utilizar o método refatorado para selecionar a conta
            Account selectedAccount = selectAccount(scanner, accountRepository);
            if (selectedAccount == null) return;

            BigDecimal withdrawAmount;
            while (true) {
                // Solicitar o valor do saque
                System.out.print("Enter the withdraw amount: ");
                withdrawAmount = scanner.nextBigDecimal();
                scanner.nextLine(); // Consome a nova linha pendente após nextBigDecimal()

                // Validar o valor do saque
                if (withdrawAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Invalid withdraw amount. Please enter a positive value.");
                } else if (withdrawAmount.compareTo(selectedAccount.getBalance()) > 0) {
                    System.out.println("Insufficient balance. Please enter an amount less than or equal to your balance.");
                } else {
                    break; // Valor válido, sai do loop
                }
            }

            // Realizar o saque
            accountService.withdraw(selectedAccount, withdrawAmount);

        } catch (Exception e) {
            System.out.println("An error occurred during the withdrawal: " + e.getMessage());
        }
    }

    private static void handleCheckBalance(Scanner scanner, AccountRepository accountRepository) {
        if (loggedClient == null) {
            System.out.println("You need to log in first.");
            return;
        }

        try {
            // Utilizar o método refatorado para selecionar a conta
            Account selectedAccount = selectAccount(scanner, accountRepository);
            if (selectedAccount == null) return;

            // Exibir o saldo da conta selecionada
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
            // Selecionar a conta de origem usando o método refatorado
            Account sourceAccount = selectAccount(scanner, accountRepository);
            if (sourceAccount == null) return;

            // Solicitar o CPF do destinatário
            String recipientCpf;
            while (true) {
                System.out.print("Enter recipient's CPF: ");
                recipientCpf = scanner.nextLine().trim();

                if (!recipientCpf.isEmpty()) {
                    break; // CPF válido, sai do loop
                }
                System.out.println("CPF cannot be empty. Please try again.");
            }

            // Buscar contas associadas ao CPF do destinatário
            List<Account> recipientAccounts = accountRepository.findAccountsByCpf(recipientCpf);
            if (recipientAccounts.isEmpty()) {
                System.out.println("No accounts found for recipient CPF: " + recipientCpf);
                return;
            }

            // Selecionar a conta de destino
            Account destinationAccount;
            String recipientName;

            if (recipientAccounts.size() == 1) {
                destinationAccount = recipientAccounts.get(0);
                recipientName = destinationAccount.getClient().getFullName();
                System.out.println("Recipient's account:");
                System.out.println("Account Type: " + destinationAccount.getAccountType());
            } else {
                System.out.println("Recipient has multiple accounts. Please select one:");
                for (int i = 0; i < recipientAccounts.size(); i++) {
                    System.out.println((i + 1) + ". " + recipientAccounts.get(i).getAccountType());
                }

                System.out.print("Select a recipient's account: ");
                int recipientChoice = scanner.nextInt();
                scanner.nextLine();

                if (recipientChoice < 1 || recipientChoice > recipientAccounts.size()) {
                    System.out.println("Invalid choice. Operation canceled.");
                    return;
                }

                destinationAccount = recipientAccounts.get(recipientChoice - 1);
                recipientName = destinationAccount.getClient().getFullName();
            }

            // Solicitar o valor da transferência
            BigDecimal transferAmount;
            while (true) {
                System.out.print("Enter the transfer amount: ");
                transferAmount = scanner.nextBigDecimal();
                scanner.nextLine();

                if (transferAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Transfer amount must be greater than zero. Please try again.");
                } else if (transferAmount.compareTo(sourceAccount.getBalance()) > 0) {
                    System.out.println("Insufficient balance. Please enter an amount less than or equal to your balance.");
                } else {
                    break; // Valor válido, sai do loop
                }
            }

            // Realizar a transferência
            accountService.transfer(sourceAccount, destinationAccount, transferAmount);

            // Exibir mensagem de sucesso
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

        try {
            // Utilizar o método refatorado para selecionar a conta
            Account selectedAccount = selectAccount(scanner, accountRepository);
            if (selectedAccount == null) return;

            // Recuperar e exibir o extrato
            List<Transaction> transactions = transactionRepository.getBankStatement(selectedAccount.getIdAccount());
            if (transactions.isEmpty()) {
                System.out.println("No transactions found for this account.");
            } else {
                // Cabeçalho do extrato
                System.out.println("Bank Statement for " + loggedClient.getFullName() + " - CPF: " + loggedClient.getCpf());
                System.out.println("----------------------------------");

                for (Transaction transaction : transactions) {
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
        } catch (Exception e) {
            System.out.println("An error occurred while fetching the bank statement: " + e.getMessage());
        }
    }

    private static void handleAccountCreation(Scanner scanner, AccountRepository accountRepository) {
        if (loggedClient == null) {
            System.out.println("You need to log in first to create additional accounts.");
            return;
        }

        try {
            // Buscar todas as contas associadas ao cliente logado
            List<Account> accounts = accountRepository.findAccountsByCpf(loggedClient.getCpf());
            Set<AccountType> existingAccountTypes = new HashSet<>();
            for (Account account : accounts) {
                existingAccountTypes.add(account.getAccountType());
            }

            // Verificar quais tipos de conta ainda podem ser criados
            List<AccountType> availableAccountTypes = new ArrayList<>();
            for (AccountType accountType : AccountType.values()) {
                if (!existingAccountTypes.contains(accountType)) {
                    availableAccountTypes.add(accountType);
                }
            }

            if (availableAccountTypes.isEmpty()) {
                System.out.println("You have already created all available account types.");
                return;
            }

            // Usar o método auxiliar para selecionar o tipo de conta
            AccountType selectedAccountType = selectAccountType(scanner, availableAccountTypes);

            // Criar a conta
            Account newAccount = new Account(loggedClient, selectedAccountType);
            if (accountRepository.save(newAccount)) {
                System.out.println("Account created successfully!");
                System.out.println("Account Type: " + selectedAccountType + " - Balance: " + newAccount.getBalance());
            } else {
                System.out.println("Failed to create the account. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred during account creation: " + e.getMessage());
        }
    }

    private static Account selectAccount(Scanner scanner, AccountRepository accountRepository) {
        List<Account> accounts = accountRepository.findAccountsByCpf(loggedClient.getCpf());
        if (accounts.isEmpty()) {
            System.out.println("No accounts found for this client.");
            return null;
        }

        if (accounts.size() == 1) {
            Account selectedAccount = accounts.get(0);
            System.out.println("Using your only account:");
            System.out.println("Account Type: " + selectedAccount.getAccountType());
            return selectedAccount;
        }

        System.out.println("You have multiple accounts. Please select one:");
        for (int i = 0; i < accounts.size(); i++) {
            System.out.println((i + 1) + ". " + accounts.get(i).getAccountType());
        }

        System.out.print("Select an account: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        if (choice < 1 || choice > accounts.size()) {
            System.out.println("Invalid choice. Operation canceled.");
            return null;
        }

        return accounts.get(choice - 1);
    }

    private static AccountType selectAccountType(Scanner scanner, List<AccountType> availableAccountTypes) {
        System.out.println("You can create the following account types:");
        for (int i = 0; i < availableAccountTypes.size(); i++) {
            System.out.println((i + 1) + ". " + availableAccountTypes.get(i));
        }

        while (true) {
            System.out.print("Select an account type to create: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consome a nova linha pendente após nextInt()

            if (choice >= 1 && choice <= availableAccountTypes.size()) {
                return availableAccountTypes.get(choice - 1);
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}