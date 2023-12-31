package client;

public interface CLIState {
    public void showPrompt();

    public default void login() {
    }

    public default void register() {
    }

    public default void succeed() {
    }

    public default void failed(String _cause) {
    }

    public default void logout() {
    }

    public default void unknownCommand() {
        LoggedinSession.out_error_msg("unknown/invalid command.");
    }

}