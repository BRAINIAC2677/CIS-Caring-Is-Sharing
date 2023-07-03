package exception;

public class DirectoryNotEmptyException extends Exception {

    private String dirName;

    public DirectoryNotEmptyException(String _dirName) {
        super();
        this.dirName = _dirName;
    }

    @Override
    public String toString() {
        return this.dirName + " directory is not empty";
    }
}