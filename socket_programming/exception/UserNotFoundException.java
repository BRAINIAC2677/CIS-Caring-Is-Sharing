package exception;

public class UserNotFoundException extends Exception {
    private String username;

    public UserNotFoundException(String _username) {
        super();
        this.username = _username;
    }

    @Override
    public String toString() {
        return "No user with the username " + this.username + " exists.";
    }
}