package Service;

import Model.Account;
import Model.User;
import Validation.AccountValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class AccountService {
    private static final int START_ACCOUNT_ID = 1;
    private static final double BASE_MULTIPLIER = 1.0;
    private static int accountCounter = START_ACCOUNT_ID;
    private final AccountValidator accountValidator;



    @Value("${account.default-amount}")
    private int defaultAmount;

    @Value("${account.transfer-commission}")
    private double transferCommission;

    public AccountService(AccountValidator accountValidator) {
        this.accountValidator = accountValidator;
    }

    //1. Создание аккаунта
    public void createAccount(int userId, UserService userService) {
        if(!accountValidator.validateCreateAccount(userId, userService)) {
            return;
        }

        User user = userService.map.get(userId);
        Account account = createDefaultAccount(userId);
        user.getAccountList().add(account);

        System.out.println("New account for " + user.getLogin() + " has been successfully created.");
    }

    //2. Пополнение счета
    public void depositAccount (int accountId, int depositAmount, UserService userService) {
        if(!accountValidator.validateDepositAccount(accountId, depositAmount, userService)) {
            return;
        }

        Account account = findAccountById(accountId, userService);
        assert account != null;
        int totalAmount = account.getMoneyAmount() + depositAmount;
        account.setMoneyAmount(totalAmount);

        System.out.println("The account replenishment was successfully completed. " +
                "Account balance: " + totalAmount);
    }

    //3. Снятие со счета
    public void withdrawAccount(int accountId, int withdrawAmount, UserService userService) {

        if(!accountValidator.validateWithdrawAccount(accountId, withdrawAmount, userService)) {
            return;
        }

        Account account = findAccountById(accountId, userService);
        assert account != null;
        int totalAmount = account.getMoneyAmount() - withdrawAmount;
        account.setMoneyAmount(totalAmount);

        System.out.println("Withdrawal from the account was successfully " +
                "completed. Account balance: " + totalAmount);
    }

    //4. Перевод между счетами
    public void transferAccount(int senderId, int recipientId, int depositAmount, UserService userService) {

        Account senderAccount = findAccountById(senderId, userService);
        Account recipientAccount = findAccountById(recipientId, userService);

        if(!accountValidator.validateTransferAccount(senderAccount, recipientAccount, depositAmount, userService)) {
            return;
        }

        assert senderAccount != null;
        assert recipientAccount != null;

        executeTransfer(senderAccount, recipientAccount, depositAmount);
    }

    //5. закрытие аккаунта
    public void closeAccount(int accountId, UserService userService) {

        Account targetAccount = findAccountById(accountId, userService);
        User targetUser = findUserByAccountId(accountId, userService);

        if(!accountValidator.validateCloseAccount(accountId, targetUser, targetAccount, userService)) {
            return;
        }

        transferResidualFunds(targetAccount, targetUser);
        assert targetUser != null;
        targetUser.getAccountList().remove(targetAccount);
        decrementCounter();

        System.out.println("Account with ID: " + accountId + " successfully closed.");
    }

    //Создание дефолтного аккаунта
    public Account createDefaultAccount(int userId) {
        int id = accountCounter;
        incrementCounter();
        return new Account(id, userId, defaultAmount);
    }

    //Поиск аккаунта по id
    private Account findAccountById(int accountId, UserService userService) {
        for (User user : userService.map.values()) {
            for(Account account : user.getAccountList()) {
                if(account.getId() == accountId) {
                    return account;
                }
            }
        }
        return null;
    }

    //Поиск пользователя по id аккаунта
    private User findUserByAccountId(int accountId, UserService userService) {
        for(User user : userService.map.values()) {
            for(Account account : user.getAccountList()) {
                if(account.getId() == accountId) {
                    return user;
                }
            }
        }
        return null;
    }

    //Перевод средств
    private void executeTransfer(Account sender, Account recipient, int amount) {
        if(sender.getUserId() == recipient.getUserId()) {
            sender.setMoneyAmount(sender.getMoneyAmount() - amount);
            recipient.setMoneyAmount(recipient.getMoneyAmount() + amount);

            System.out.println("The transfer was successful. The sender's balance: " +
                    sender.getMoneyAmount() + ". The recipient's balance: " + recipient.getMoneyAmount());
        } else {

            double commissionAmount = BASE_MULTIPLIER + transferCommission;
            int amountWithCommission = (int) (amount * commissionAmount);

            sender.setMoneyAmount(sender.getMoneyAmount() - amountWithCommission);
            recipient.setMoneyAmount(recipient.getMoneyAmount() + amount);

            System.out.println("The transfer was successfully completed. " +
                    "The sender's balance: " + sender.getMoneyAmount() +
                    ". The recipient's balance: " + recipient.getMoneyAmount() +
                    ". The commission amount was: " + transferCommission);
        }
    }

    //Перенос остатка при закрытии
    private void transferResidualFunds(Account closedAccount, User user) {
        Account account = findAlternativeAccount(closedAccount, user);
        if(account != null) {
            int residualAmount = closedAccount.getMoneyAmount();
            account.setMoneyAmount(account.getMoneyAmount() + residualAmount);
        }

    }

    //Поиск аккаунта для переноса средств с удаленного
    private Account findAlternativeAccount(Account closedAccount, User user) {
        return user.getAccountList().stream()
                .filter(account -> !account.equals(closedAccount))
                .findFirst()
                .orElse(null);
    }

    private void incrementCounter() {
        accountCounter ++;
    }

    private void decrementCounter() {
        accountCounter --;
    }
}
