package exception;

public class DirectoryDoesNotExistException extends Exception {

    private String dirName;

    public DirectoryDoesNotExistException(String _dirName) {
        super();
        this.dirName = _dirName;
    }

    @Override
    public String toString() {
        return this.dirName + " directory does not exist";
    }
}