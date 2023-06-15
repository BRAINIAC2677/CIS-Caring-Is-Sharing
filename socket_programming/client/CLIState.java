package client;

public interface CLIState {
    public void showPrompt();

    public default void login() {
    }

    public default void register() {
    }

    public default void rightCredentials() {
    }

    public default void wrongCredentials() {
    }

    public default void logout() {
    }

    public default void unknownCommand() {
        System.out.println("unknown/invalid command");
    }

}