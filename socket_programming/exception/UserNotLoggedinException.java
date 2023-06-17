package exception;

public class UserNotLoggedinException extends Exception {
    private String username;

    public UserNotLoggedinException(String _username) {
        this.username = _username;
    }

    @Override
    public String toString() {
        return this.username + " is not loggedin.";
    }
}