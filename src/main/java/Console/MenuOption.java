package Console;

public enum MenuOption {
    USER_CREATE(1, "USER_CREATE"),
    SHOW_ALL_USERS(2, "SHOW_ALL_USERS"),
    ACCOUNT_CREATE(3, "ACCOUNT_CREATE"),
    ACCOUNT_DEPOSIT(4, "ACCOUNT_DEPOSIT"),
    ACCOUNT_WITHDRAW(5, "ACCOUNT_WITHDRAW"),
    ACCOUNT_TRANSFER(6, "ACCOUNT_TRANSFER"),
    ACCOUNT_CLOSE(7, "ACCOUNT_CLOSE"),
    EXIT(0, "EXIT");

    private final int code;
    private final String description;

    MenuOption(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static MenuOption fromCode(int code) {
        for(MenuOption option : MenuOption.values()) {
            if(option.code == code) {
                return option;
            }
        }
        return null;
    }
}
