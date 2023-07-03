package exception;

public class NotADirectoryException extends Exception {

    private String dirName;

    public NotADirectoryException(String _dirName) {
        super();
        this.dirName = _dirName;
    }

    @Override
    public String toString() {
        return this.dirName + " is not a directory.";
    }
}