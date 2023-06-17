package server;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import exception.*;

class UserCLI {
    private Path working_directory;

    UserCLI(String _working_directory) {
        this.working_directory = Paths.get(_working_directory);
    }

    public void mkdir(String _directory_name) throws DirectoryExistsException {
        File directory = this.get_file_from_pathstring(_directory_name);
        if (directory.exists()) {
            throw new DirectoryExistsException(_directory_name);
        }
        directory.mkdir();
    }

    File get_file_from_pathstring(String _relative_pathstring) {
        Path file_path = Paths.get(this.working_directory.toString(), _relative_pathstring);
        File file = file_path.toFile();
        return file;
    }

    public void rmdir(String _directory_name) throws DirectoryDoesNotExistException, DirectoryNotEmptyException {
        File directory = this.get_file_from_pathstring(_directory_name);
        if (!directory.exists()) {
            throw new DirectoryDoesNotExistException(_directory_name);
        }
        if (directory.listFiles().length > 0) {
            throw new DirectoryNotEmptyException(_directory_name);
        }
        directory.delete();
    }

    public void cd(String _relative_pathstring) {
        if (_relative_pathstring.equals("..") && !(this.is_in_root_directory())) {
            this.working_directory = this.working_directory.getParent();
            return;
        }

    }

    Boolean is_in_root_directory() {
        return this.working_directory.getNameCount() == 3;
    }

    public static void main(String[] args) {
        UserCLI user_cli = new UserCLI("socket_programming/storage/admin");
        try {
            user_cli.rmdir("docs");

        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

}