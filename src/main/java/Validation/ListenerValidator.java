package Validation;

import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class ListenerValidator {
    private static final int MINIMUM_DEPOSIT_AMOUNT = 1;
    private static final int MINIMUM_ID_VALUE = 1;
    private static final int INVALID_CHOICE = -1;

    //Проверка входного числа
    public int validInt(Scanner scanner) {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e){
            scanner.nextLine();
            return INVALID_CHOICE;
        }
    }

    //Проверка, что ID не отрицательное
    public boolean validNotNegativeId(int id) {
        return id >= MINIMUM_ID_VALUE;
    }

    //Проверка, что amount не отрицательный
    public boolean validNotNegativeAmount(int amount) {
        return amount >= MINIMUM_DEPOSIT_AMOUNT;
    }

    //Проверка входной строки
    public boolean validString(String string) {
        if(string == null || string.trim().isEmpty()) {
            System.out.println("Invalid username");
            return false;
        }
        return true;
    }
}
