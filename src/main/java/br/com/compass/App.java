package br.com.compass;

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
import java.util.*;

public class App {

    private static Client loggedClient;

    public static void main(String[] args) {

        // Iniciar EntityManager
        EntityManager em = JpaUtil.getEntityManager();

        // Inicializar os repositórios e serviços que precisarão do JpaUtil
        AccountRepository accountRepository = new AccountRepository(em);
        TransactionRepository transactionRepository = new TransactionRepository();
        AccountService accountService = new AccountService();
        ClientRepository clientRepository = new ClientRepository(em);

        Scanner scanner = new Scanner(System.in);

        // Iniciar o menu principal
        mainMenu(scanner, accountRepository, accountService, transactionRepository);

        scanner.close();

        em.close();
        JpaUtil.closeEntityManagerFactory();

        System.out.println("Application closed");
    }

    public static void mainMenu(Scanner scanner, AccountRepository accountRepository, AccountService accountService, TransactionRepository transactionRepository) {
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
                    loginMenu(scanner, accountRepository, accountService, transactionRepository);
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
    public static void loginMenu(Scanner scanner, AccountRepository accountRepository, AccountService accountService, TransactionRepository transactionRepository) {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("========= Login Menu =========");

            String cpf = ClientValidator.promptForValidCpf(scanner);

            try {
                Client client = accountRepository.findClientFromAccountByCpf(cpf);

                if (client == null) {
                    System.out.println("No account found for CPF: " + cpf);
                    continue;
                }

                System.out.print("Enter your password: ");
                String password = scanner.nextLine();

                if (client.getPassword().equals(password)) {
                    loggedClient = client;
                    System.out.println("Login successful! Welcome, " + client.getFullName() + "!");
                    System.out.println();
                    bankMenu(scanner, accountRepository, accountService, transactionRepository);
                    running = false; // Finaliza o loop após o login bem-sucedido
                } else {
                    System.out.println("Invalid password. Please try again.");
                }

            } catch (Exception e) {
                System.err.println("Error during login: " + e.getMessage());
            }
        }
    }

    public static void bankMenu(Scanner scanner, AccountRepository accountRepository, AccountService accountService, TransactionRepository transactionRepository) {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("========= Bank Menu ================");
            System.out.println("|| 1. Deposit                     ||");
            System.out.println("|| 2. Withdraw                    ||");
            System.out.println("|| 3. Check Balance               ||");
            System.out.println("|| 4. Transfer                    ||");
            System.out.println("|| 5. Bank Statement              ||");
            System.out.println("|| 6. Create Additional Account   ||");
            System.out.println("|| 0. Back to Main Menu           ||");
            System.out.println("====================================");
            System.out.print("Choose an option: ");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    handleDeposit(scanner, accountRepository, accountService);
                    break;
                case 2:
                    handleWithdraw(scanner, accountRepository, accountService);
                    break;
                case 3:
                    handleCheckBalance(scanner, accountRepository);
                    break;
                case 4:
                    handleTransfer(scanner, accountRepository, accountService);
                    break;
                case 5:
                    handleBankStatement(scanner, transactionRepository, accountRepository);
                    break;
                case 6:
                    System.out.println("Create new account.");
                    handleAccountCreation(scanner, accountRepository);
                    break;
                case 0:
                    // ToDo...
                    System.out.println("Returning to Main Menu...");
                    mainMenu(scanner, accountRepository, accountService, transactionRepository);
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

        EntityManager em = JpaUtil.getEntityManager(); // Obter o EntityManager
        ClientService clientService = new ClientService(); // Inicializar o ClientService fora do loop
        ClientRepository clientRepository = new ClientRepository(em); // Repositório de cliente

        boolean running = true;

        while (running) {
            try {
                System.out.println("Please enter the following details to open a new account:");

                // Captura todos os dados do cliente diretamente do ClientService
                Client client = clientService.captureClientDetails(scanner);

                // Salvar cliente no banco de dados
                if (!clientRepository.save(client)) {
                    System.err.println("Failed to save client.");
                    continue;
                }
                System.out.println("Client saved successfully!");

                // Selecionar o tipo de conta
                AccountType selectedAccountType = selectAccountType(scanner, Arrays.asList(AccountType.values()));

                // Criar a conta
                Account account = new Account(client, selectedAccountType);

                // Salvar conta no banco de dados
                if (!accountRepository.save(account)) {
                    System.err.println("Failed to save account.");
                    continue;
                }

                // Exibir detalhes da conta criada
                System.out.println("\nAccount successfully created!");
                System.out.println("Account Details:");
                System.out.println("Client Name: " + client.getFullName());
                System.out.println("Account Type: " + selectedAccountType);
                System.out.println("Balance: " + account.getBalance());
                System.out.println("Creation Date: " + account.getCreationDate());

                running = false; // Finalizar o loop após criar a conta
            } catch (Exception e) {
                System.out.println("An unexpected error occurred: " + e.getMessage());
            }
        }
    }

    private static void handleDeposit(Scanner scanner, AccountRepository accountRepository, AccountService accountService) {
        try {
            // Selecionar a conta
            Account selectedAccount = selectAccount(scanner, accountRepository);
            if (selectedAccount == null) return;

            // Exibir o saldo atual da conta
            String formattedBalance = AccountService.formatCurrency(selectedAccount.getBalance().doubleValue());
            System.out.println("Your current balance: " + formattedBalance);

            // Solicitar o valor do depósito
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

            // Formatar o valor do depósito para exibição
            String formattedDepositAmount = AccountService.formatCurrency(depositAmount.doubleValue());

            // Realizar o depósito
            accountService.deposit(selectedAccount, depositAmount);

            // Exibir mensagem de sucesso e o saldo atualizado
            String formattedUpdatedBalance = AccountService.formatCurrency(selectedAccount.getBalance().doubleValue());
            System.out.println("Deposit completed successfully!");
            System.out.println("Deposited amount: " + formattedDepositAmount);
            System.out.println("Your updated balance: " + formattedUpdatedBalance);

        } catch (Exception e) {
            System.out.println("An error occurred during the deposit: " + e.getMessage());
        }
    }

    private static void handleWithdraw(Scanner scanner, AccountRepository accountRepository, AccountService accountService) {
        try {
            // Utilizar o método refatorado para selecionar a conta
            Account selectedAccount = selectAccount(scanner, accountRepository);
            if (selectedAccount == null) return;

            // Exibir o saldo atual da conta
            String formattedBalance = AccountService.formatCurrency(selectedAccount.getBalance().doubleValue());
            System.out.println("Your current balance: " + formattedBalance);

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

            // Formatar o valor do saque para exibição
            String formattedWithdrawAmount = AccountService.formatCurrency(withdrawAmount.doubleValue());

            // Realizar o saque
            accountService.withdraw(selectedAccount, withdrawAmount);

            // Exibir mensagem de sucesso e o saldo atualizado
            String formattedUpdatedBalance = AccountService.formatCurrency(selectedAccount.getBalance().doubleValue());
            System.out.println("Withdrawal completed successfully!");
            System.out.println("Withdrawn amount: " + formattedWithdrawAmount);
            System.out.println("Your updated balance: " + formattedUpdatedBalance);

        } catch (Exception e) {
            System.out.println("An error occurred during the withdrawal: " + e.getMessage());
        }
    }

    private static void handleCheckBalance(Scanner scanner, AccountRepository accountRepository) {
        try {
            // Utilizar o método refatorado para selecionar a conta
            Account selectedAccount = selectAccount(scanner, accountRepository);
            if (selectedAccount == null) return;

            // Formatar o saldo da conta selecionada
            String formattedBalance = AccountService.formatCurrency(selectedAccount.getBalance().doubleValue());

            // Exibir o tipo de conta e o saldo formatado
            System.out.println("Account Type: " + selectedAccount.getAccountType());
            System.out.println("Balance: " + formattedBalance);

        } catch (Exception e) {
            System.out.println("An error occurred during the balance check: " + e.getMessage());
        }
    }

    private static void handleTransfer(Scanner scanner, AccountRepository accountRepository, AccountService accountService) {
        try {
            // Selecionar a conta de origem usando o método refatorado
            Account sourceAccount = selectAccount(scanner, accountRepository);
            if (sourceAccount == null) return;

            // Mostrar o saldo da conta de origem antes da transferência
            String formattedSourceBalance = AccountService.formatCurrency(sourceAccount.getBalance().doubleValue());
            System.out.println("Your current balance: " + formattedSourceBalance);

            // Solicitar o CPF do destinatário
            String recipientCpf = ClientValidator.promptForValidCpf(scanner);

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

            // Formatar o valor da transferência para exibição
            String formattedTransferAmount = AccountService.formatCurrency(transferAmount.doubleValue());

            // Realizar a transferência
            accountService.transfer(sourceAccount, destinationAccount, transferAmount);

            // Formatar o saldo atualizado da conta de origem
            String formattedUpdatedBalance = AccountService.formatCurrency(sourceAccount.getBalance().doubleValue());

            // Exibir mensagem de sucesso
            System.out.println("Transfer completed successfully!");
            System.out.println("Transferred " + formattedTransferAmount + " from " + sourceAccount.getAccountType() +
                    " to " + destinationAccount.getAccountType() + " (Recipient: " + recipientName + ")");
            System.out.println("Your updated balance: " + formattedUpdatedBalance);
        } catch (Exception e) {
            System.out.println("An error occurred during the transfer: " + e.getMessage());
        }
    }

    private static void handleBankStatement(Scanner scanner, TransactionRepository transactionRepository, AccountRepository accountRepository) {
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
                    // Formatar o valor da transação
                    String formattedAmount = AccountService.formatCurrency(transaction.getAmount().doubleValue());

                    System.out.println("Type: " + transaction.getType());
                    System.out.println("Amount: " + formattedAmount); // Valor formatado
                    System.out.println("Date: " + transaction.getTimestamp());

                    if (transaction.getType() == TransactionType.TRANSFER) {
                        Optional<Account> destinationAccountOptional = transaction.getDestinationAccount();

                        // Verificar se existe uma conta de destino
                        if (destinationAccountOptional.isPresent()) {
                            Account destinationAccount = destinationAccountOptional.get(); // Pega o valor do Optional
                            Client destinationClient = destinationAccount.getClient();

                            if (destinationClient != null) {
                                System.out.println("To: " + destinationClient.getFullName() + " - CPF: " + destinationClient.getCpf());
                            } else {
                                System.out.println("To Account ID: " + destinationAccount.getIdAccount());
                            }
                        } else {
                            System.out.println("No destination account available.");
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

            // Método para selecionar o tipo de conta
            AccountType selectedAccountType = selectAccountType(scanner, availableAccountTypes);

            // Criar a conta
            Account newAccount = new Account(loggedClient, selectedAccountType);
            if (accountRepository.save(newAccount)) {
                // Usar o método formatCurrency para formatar o saldo
                String formattedBalance = AccountService.formatCurrency(newAccount.getBalance().doubleValue());

                System.out.println("Account created successfully!");
                System.out.println("Account Type: " + selectedAccountType + " - Balance: " + formattedBalance);
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
            System.out.println("Account Type: " + selectedAccount.getAccountType());
            return selectedAccount;
        }

        while (true) {
            System.out.println("You have multiple accounts. Please select one: ");
            for (int i = 0; i < accounts.size(); i++) {
                System.out.println((i + 1) + ". " + accounts.get(i).getAccountType());
            }
            System.out.println("0. Cancel");

            System.out.print("Select an account (or 0 to cancel): ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 0) {
                System.out.println("Operation canceled.");
                return null; // Cancela a operação se o cliente escolher 0
            }

            if (choice >= 1 && choice <= accounts.size()) {
                return accounts.get(choice - 1); // Retorna a conta selecionada
            }

            System.out.println("Invalid choice. Please try again.");
        }
    }

    private static AccountType selectAccountType(Scanner scanner, List<AccountType> availableAccountTypes) {
        System.out.println("You can create the following account types:");
        for (int i = 0; i < availableAccountTypes.size(); i++) {
            System.out.println((i + 1) + ". " + availableAccountTypes.get(i));
        }
        System.out.println("0. Cancel");

        while (true) {
            System.out.print("Select an account type to create (or 0 to cancel): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consome a nova linha pendente após nextInt()

            if (choice == 0) {
                System.out.println("Operation canceled.");
                return null; // Retorna null se o usuário cancelar
            }

            if (choice >= 1 && choice <= availableAccountTypes.size()) {
                return availableAccountTypes.get(choice - 1); // Retorna o tipo de conta selecionado
            }

            System.out.println("Invalid choice. Please try again.");
        }
    }
}