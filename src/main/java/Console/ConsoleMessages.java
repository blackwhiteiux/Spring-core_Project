package Console;

public enum ConsoleMessages {

    CHOICE_OPERATION("Choose operation:"),
    INCORRECT_INPUT("Choose correct operation`s number"),
    EXIT_MESSAGE("Thanks for using app!"),
    INVALID_ID("Invalid ID"),
    ENTER_ACC_ID("Enter the account ID: ");
    private final String message;

    ConsoleMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
