package exception;

public class IncorrectPasswordException extends Exception {
    private String username;

    public IncorrectPasswordException(String _username) {
        super();
        this.username = _username;
    }

    @Override
    public String toString() {
        return "wrong password for " + this.username;
    }
}