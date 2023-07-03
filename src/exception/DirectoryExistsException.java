package exception;

public class DirectoryExistsException extends Exception {

    private String dirName;

    public DirectoryExistsException(String _dirName) {
        super();
        this.dirName = _dirName;
    }

    @Override
    public String toString() {
        return this.dirName + " directory exists";
    }
}