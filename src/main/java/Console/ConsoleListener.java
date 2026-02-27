package Console;

import Service.AccountService;
import Service.UserService;
import Validation.ListenerValidator;

import java.util.Scanner;

public class ConsoleListener {
    private final UserService userService;
    private final AccountService accountService;
    private final ListenerValidator listenerValidator;
    private final Scanner scanner;
    private boolean isRunning;

    public ConsoleListener(UserService userService, AccountService accountService, ListenerValidator listenerValidator) {
        this.accountService = accountService;
        this.userService = userService;
        this.listenerValidator = listenerValidator;
        this.isRunning = true;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (isRunning) {
            printMenu();
            int choice = listenerValidator.validInt(scanner);
            scanner.nextLine();
            processChoice(choice);
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println(ConsoleMessages.CHOICE_OPERATION.getMessage());
        for(MenuOption option : MenuOption.values()) {
            System.out.println(option.getCode() + ". " + option.getDescription());
        }
    }

    private void processChoice(int choice) {
        MenuOption option = MenuOption.fromCode(choice);

        switch (option) {
            case USER_CREATE:
                createUser();
                break;
            case SHOW_ALL_USERS:
                userService.showAllUsers();
                break;
            case ACCOUNT_CREATE:
                createAccount();
                break;
            case ACCOUNT_DEPOSIT:
                depositAccount();
                break;
            case ACCOUNT_WITHDRAW:
                withdrawAccount();
                break;
            case ACCOUNT_TRANSFER:
                transferAccount();
                break;
            case ACCOUNT_CLOSE:
                closeAccount();
                break;
            case EXIT:
                exit();
                break;
            case null, default:
                System.out.println(ConsoleMessages.INCORRECT_INPUT.getMessage());
                break;
        }
    }

    //1. Создание User
    private void createUser() {
        System.out.println("Enter your username: ");
        String login = scanner.nextLine();

        if(!listenerValidator.validString(login)) {
            return;
        }

        userService.createUser(login, accountService);
        System.out.println("The user with the username `" + login + "` has been successfully created.");
    }

    //3. Создание аккаунта
    private void createAccount() {
        System.out.println("Enter the user's ID: ");
        int userId = listenerValidator.validInt(scanner);

        if(!listenerValidator.validNotNegativeId(userId)) {
            System.out.println(ConsoleMessages.INVALID_ID.getMessage());
            return;
        }

        accountService.createAccount(userId, userService);
    }

    //4. Пополнение счета
    private void depositAccount() {
        System.out.println(ConsoleMessages.ENTER_ACC_ID.getMessage());

        int accountId = listenerValidator.validInt(scanner);

        if(!listenerValidator.validNotNegativeId(accountId)) {
            System.out.println(ConsoleMessages.INVALID_ID.getMessage());
            return;
        }

        System.out.println("Enter the amount for the deposit: ");

        int depositAmount = listenerValidator.validInt(scanner);

        if(!listenerValidator.validNotNegativeAmount(depositAmount)) {
            System.out.println("Incorrect amount.");
            return;
        }

        accountService.depositAccount(accountId, depositAmount, userService);
    }

    //5. Снятие со счета
    private void withdrawAccount() {
        System.out.println(ConsoleMessages.ENTER_ACC_ID.getMessage());
        int accountId = listenerValidator.validInt(scanner);

        if(!listenerValidator.validNotNegativeId(accountId)) {
            System.out.println(ConsoleMessages.INVALID_ID.getMessage());
            return;
        }

        System.out.println("Enter the amount to withdraw from the account: ");
        int depositAmount = listenerValidator.validInt(scanner);

        if(!listenerValidator.validNotNegativeAmount(depositAmount)) {
            System.out.println("Incorrect amount.");
            return;
        }

        accountService.withdrawAccount(accountId, depositAmount, userService);
    }

    //6. Перевод между счетами
    private void transferAccount() {
        System.out.println("Specify the sender's account ID: ");
        int senderId = listenerValidator.validInt(scanner);

        System.out.println("Specify the recipient's account ID");
        int recipientId = listenerValidator.validInt(scanner);

        if(!listenerValidator.validNotNegativeId(senderId)
        || !listenerValidator.validNotNegativeId(recipientId)) {
            System.out.println(ConsoleMessages.INVALID_ID.getMessage());
            return;
        }

        System.out.println("Enter the amount to withdraw from the account: ");
        int depositAmount = listenerValidator.validInt(scanner);

        if(!listenerValidator.validNotNegativeAmount(depositAmount)) {
            System.out.println("Incorrect amount.");
            return;
        }

        accountService.transferAccount(senderId, recipientId, depositAmount, userService);
    }

    //7. закрытие аккаунта
    private void closeAccount() {
        System.out.println(ConsoleMessages.ENTER_ACC_ID.getMessage());
        int accountId = listenerValidator.validInt(scanner);

        if(!listenerValidator.validNotNegativeId(accountId)) {
            System.out.println(ConsoleMessages.INVALID_ID.getMessage());
            return;
        }

        accountService.closeAccount(accountId, userService);
    }

    //Выход
    private void exit() {
        System.out.println(ConsoleMessages.EXIT_MESSAGE.getMessage());
        isRunning = false;
    }
}
