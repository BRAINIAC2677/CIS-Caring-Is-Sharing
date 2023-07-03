package exception;

public class UsernameUnavailableException extends Exception {
    private String username;

    public UsernameUnavailableException(String _username) {
        super();
        this.username = _username;
    }

    @Override
    public String toString() {
        return "A user with the username " + this.username + " already exists.";
    }
}