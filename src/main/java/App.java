import Config.AppConfig;
import Console.ConsoleListener;
import Service.AccountService;
import Service.UserService;
import Validation.ListenerValidator;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class App {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        AccountService accountService = context.getBean(AccountService.class);
        UserService userService = context.getBean(UserService.class);

        ListenerValidator listenerValidator = context.getBean(ListenerValidator.class);

        ConsoleListener consoleListener = new ConsoleListener(userService, accountService, listenerValidator);
        consoleListener.start();
    }
}
