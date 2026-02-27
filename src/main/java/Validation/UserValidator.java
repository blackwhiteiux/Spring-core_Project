package Validation;

import Model.User;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserValidator {

    public boolean validateLoginNotNull(String login) {
        return login != null && !login.trim().isEmpty();
    }

    public boolean validateUniqueLogin(String login, Map<Integer, User> map) {
       return map.values().stream()
               .noneMatch(user -> user.getLogin().equals(login));
   }

   public boolean validateNotEmptyMap(Map<Integer, User> map) {
        return map.isEmpty();
   }
}
