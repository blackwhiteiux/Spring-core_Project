package Config;

import Service.AccountService;
import Service.UserService;
import Validation.AccountValidator;
import Validation.ListenerValidator;
import Validation.UserValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
public class AppConfig {

    @Bean
    public ListenerValidator listenerValidator() {
        return new ListenerValidator();
    }

    @Bean
    public UserValidator userValidator() {
        return new UserValidator();
    }

    @Bean
    public AccountValidator accountValidator() {
        return new AccountValidator();
    }

    @Bean
    public AccountService accountService() {
        return new AccountService(accountValidator());
    }

    @Bean
    public UserService userService() {
        return new UserService(userValidator());
    }
}
