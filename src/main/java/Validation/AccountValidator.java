package Validation;

import Model.Account;
import Model.User;
import Service.UserService;
import org.springframework.stereotype.Component;

@Component
public class AccountValidator {
    private static final int MINIMUM_ACCOUNT_BALANCE = 0;
    private static final int MINIMUM_OPERATION_AMOUNT = 0;
    private static final int MINIMUM_ACCOUNTS_PER_USER = 1;

    //Модульные публичные методы
    //1. Валидация для создания аккаунта
    public boolean validateCreateAccount(int userId, UserService userService) {
        if(!validateUserMapNoEmpty(userService)) {
            return false;
        }

        return validateUserExist(userId, userService);
    }

    //2. Валидация для пополнения
    public boolean validateDepositAccount(int accountId, int depositAmount, UserService userService) {

        if(!validateUserMapNoEmpty(userService)) {
            return false;
        }

        if(!validatePositiveAmount(depositAmount)) {
            return false;
        }

        return validateAccountExist(accountId, userService);
    }

    //3. Валидация для снятия
    public boolean validateWithdrawAccount(int accountId, int withdrawAmount, UserService userService) {
        if(!validateUserMapNoEmpty(userService)) {
            return false;
        }

        if(!validatePositiveAmount(withdrawAmount)) {
            return false;
        }

        Account account = findAccountById(accountId, userService);
        if(!validateAccountFound(account, accountId)) {
            return false;
        }

        assert account != null;
        return validateEnoughFunds(account, withdrawAmount);
    }

    //4. Валидация для перевода
    public boolean validateTransferAccount(Account sender, Account recipient, int amount, UserService userService) {
        if(!validateUserMapNoEmpty(userService)) {
            return false;
        }

        if(!validateAccountsNotNull(sender, recipient)) {
            return false;
        }

        if(!validatePositiveAmount(amount)) {
            return false;
        }

        if(!validateEnoughFunds(sender, amount)) {
            return false;
        }

        return true;
    }

    //5. Валидация для закрытия
    public boolean validateCloseAccount(int accountId, User user, Account account, UserService userService) {
        if(!validateUserMapNoEmpty(userService)) {
            return false;
        }

        if(!validateAccountFound(account, accountId)) {
            return false;
        }

        if(!validateAccountExist(accountId, userService)) {
            return false;
        }

        if(!validateAccountFound(account, accountId)) {
            return false;
        }

        if(!validateUserFound(user, account.getUserId())) {
            return false;
        }

        return validateNotLastAccount(user);
    }

    //Проверка наличия пользователей
    private boolean validateUserMapNoEmpty(UserService userService) {
        if(userService.map.isEmpty()) {
            System.out.println("Create users first.");
            return false;
        }

        return true;
    }

    //Поиск пользователя по id
    private boolean validateUserExist(int userId, UserService userService) {
        if(!userService.map.containsKey(userId)) {
            System.out.println("User with ID: " + userId + " not found.");
            return false;
        }
        return true;
    }

    //Проверка на то, что пользователь найден
    private  boolean validateUserFound(User user, int userId) {
        if(user == null) {
            System.out.println("User with ID " + userId + " not found.");
            return false;
        }
        return true;
    }

    //Поиск аккаунта по id
    private Account findAccountById(int accountId, UserService userService) {
        for(User user : userService.map.values()) {
            for(Account account : user.getAccountList()) {
                if(accountId == account.getId()) {
                    return account;
                }
            }
        }
        return null;
    }

    private boolean validateAccountExist(int accountId, UserService userService) {
        Account account = findAccountById(accountId, userService);
        return validateAccountFound(account, accountId);
    }

    //Проверка на то, что аккаунт найден
    private boolean validateAccountFound(Account account, int accountId) {
        if(account == null) {
            System.out.println("Account with ID " + accountId + " not found.");
            return false;
        }
        return true;
    }

    //бизнес логика
    //Проверка на последний аккаунт
    private boolean validateNotLastAccount(User user) {
        if(user.getAccountList().size() <= MINIMUM_ACCOUNTS_PER_USER) {
            System.out.println("Cannot delete the only account. User must have at least one account.");
            return false;
        }

        return true;
    }

    //Проверка положительной суммы операции
    private boolean validatePositiveAmount(int amount) {
        if(amount < MINIMUM_OPERATION_AMOUNT) {
            System.out.println("Amount must be positive.");
            return false;
        }
        return true;
    }

    //Проверка, что оба аккаунта не пусты
    private boolean validateAccountsNotNull(Account sender, Account recipient) {
        if(sender == null) {
            System.out.println("Sender account not found.");
            return false;
        }

        if(recipient == null) {
            System.out.println("Recipient account not found.");
            return false;
        }
        return true;
    }

    //Проверка достаточности баланса
    private boolean validateEnoughFunds(Account account, int withdrawAmount) {
        if(account.getMoneyAmount() - withdrawAmount < MINIMUM_ACCOUNT_BALANCE) {
            System.out.println("There are not enough funds in the account to withdraw this amount: " +
                    withdrawAmount + ". Account balance: " +
                    account.getMoneyAmount() + ".");
            return false;
        }
        return true;
    }
}
