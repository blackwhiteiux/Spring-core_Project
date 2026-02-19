package Service;

import Model.Account;
import Model.User;
import Validation.UserValidator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {
    private static final int START_USER_ID = 1;
    private static int userCounter = START_USER_ID;
    private final UserValidator userValidator;
    public Map<Integer, User> map = new HashMap<>();

    public UserService(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

    //1. Создание User
    public void createUser(String login, AccountService accountService) {

        //проверка уникальности имени
        if (!userValidator.validateLoginNotNull(login) ||
                !userValidator.validateUniqueLogin(login, map)) {
            System.out.println("User with name `" + login + "` already exist.");
            return;
        }

        User user = createNewUser(login);
        createDefaultAccount(user, accountService);
        safeUser(user);
    }

    //2. Вывод всех пользователей
    public void showAllUsers() {
        if(userValidator.validateNotEmptyMap(map)) {
            System.out.println("Create user firsts.");
            return;
        }

        for(User user : map.values()) {
            System.out.println(user);
        }
    }

    private User createNewUser(String login) {
        int userId = createUserId();
        List<Account> accounts = new ArrayList<>();
        return new User(userId, login, accounts);
    }

    private int createUserId() {
        return userCounter ++;
    }

    private void createDefaultAccount(User user, AccountService accountService) {
        Account defaultAccount = accountService.createDefaultAccount(user.getId());
        user.getAccountList().add(defaultAccount);
    }

    private void safeUser(User user) {
        map.put(user.getId(), user);
    }
}
