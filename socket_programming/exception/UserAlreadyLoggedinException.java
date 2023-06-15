package exception;

public class UserAlreadyLoggedinException extends Exception {
    private String username;

    public UserAlreadyLoggedinException(String _username) {
        super();
        this.username = _username;
    }

    @Override
    public String toString() {
        return this.username + " is already logged in.";
    }
}