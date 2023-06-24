package util;

import java.util.HashMap;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import exception.*;

public class RemoteCLI {
    private Path working_directory;
    private User user;

    public RemoteCLI(String _working_directory, User _user) {
        this.working_directory = Paths.get(_working_directory);
        this.user = _user;
    }

    public File mkdir(String _directory_name) throws DirectoryExistsException {
        File directory = this.get_file_from_pathstring(_directory_name, true);
        if (directory.exists()) {
            throw new DirectoryExistsException(_directory_name);
        }
        directory.mkdir();
        return directory;
    }

    public File mkdir(String _directory_name, Boolean _is_public) throws DirectoryExistsException {
        File directory = this.get_file_from_pathstring(_directory_name, _is_public);
        if (directory.exists()) {
            throw new DirectoryExistsException(_directory_name);
        }
        directory.mkdir();
        return directory;
    }

    File get_file_from_pathstring(String _relative_pathstring, Boolean _is_public) {
        if (_is_public) {
            _relative_pathstring += ".publ";
        } else {
            _relative_pathstring += ".priv";
        }
        Path file_path = Paths.get(this.working_directory.toString(), _relative_pathstring);
        File file = file_path.toFile();
        return file;
    }

    public void rmdir(String _directory_name) throws Exception {
        File directory = this.run_rmdir_diagnostics(_directory_name);
        directory.delete();
    }

    File run_rmdir_diagnostics(String _relative_path)
            throws DirectoryDoesNotExistException, DirectoryNotEmptyException {
        File directory = this.get_file_if_exists(_relative_path);
        if (directory.listFiles().length > 0) {
            throw new DirectoryNotEmptyException(directory.getName());
        }
        return directory;
    }

    File get_file_if_exists(String _relative_path) throws DirectoryDoesNotExistException {
        File public_file = this.get_file_from_pathstring(_relative_path, true);
        File private_file = this.get_file_from_pathstring(_relative_path, false);
        if (public_file.exists()) {
            return public_file;
        } else if (private_file.exists()) {
            return private_file;
        } else {
            throw new DirectoryDoesNotExistException(_relative_path);
        }
    }

    public File cd(String _relative_pathstring) throws Exception {
        if (_relative_pathstring.equals(".")) {
            return this.working_directory.toFile();
        }
        if (_relative_pathstring.equals("..") && !(this.is_in_root_directory())) {
            this.working_directory = this.working_directory.getParent();
            this.set_user_working_directory();
            return this.working_directory.toFile();
        }
        File directory = this.run_cd_diagnostics(_relative_pathstring);
        this.working_directory = this.working_directory.resolve(directory.getName());
        this.set_user_working_directory();
        return directory;
    }

    Boolean is_in_root_directory() {
        return this.working_directory.getNameCount() == 3;
    }

    void set_user_working_directory() {
        String root_relative_working_directory = "/";
        int name_count = this.working_directory.getNameCount();
        if (name_count > 3) {
            String[] splitted_names = this.working_directory.subpath(3, name_count).toString().split("/");
            for (String splitted_name : splitted_names) {
                root_relative_working_directory += this.get_filename(splitted_name) + "/";
            }
            root_relative_working_directory = root_relative_working_directory.substring(0,
                    root_relative_working_directory.length() - 1);
        }
        this.user.setWorkingDir(root_relative_working_directory);
    }

    File run_cd_diagnostics(String _relative_pathstring) throws DirectoryDoesNotExistException, NotADirectoryException {
        File directory = this.get_file_if_exists(_relative_pathstring);
        if (!(directory.isDirectory())) {
            throw new NotADirectoryException(directory.getName());
        }
        return directory;
    }

    public HashMap<String, Boolean> ls(String _relative_pathstring) throws Exception {
        File directory;
        if (_relative_pathstring.equals(".")) {
            directory = this.working_directory.toFile();
        } else {
            directory = this.run_ls_diagnostics(_relative_pathstring);
        }
        HashMap<String, Boolean> file_names = new HashMap<String, Boolean>();
        for (File file : directory.listFiles()) {
            file_names.put(this.get_filename(file.getName()), this.is_public(file.getName()));
        }
        return file_names;
    }

    String get_filename(String _filename) {
        if (_filename.endsWith(".publ") || _filename.endsWith(".priv")) {
            return _filename.substring(0, _filename.lastIndexOf("."));
        }
        return _filename;
    }

    File run_ls_diagnostics(String _relative_pathstring) throws DirectoryDoesNotExistException, NotADirectoryException {
        File directory = this.get_file_if_exists(_relative_pathstring);
        if (!directory.isDirectory()) {
            throw new NotADirectoryException(directory.getName());
        }
        return directory;
    }

    public File touch(String _filename, byte[] _filecontent, Boolean _is_public) throws Exception {
        File file = get_file_from_pathstring(_filename, _is_public);
        if (file.exists()) {
            throw new DirectoryExistsException(_filename);
        }
        file.createNewFile();
        this.write_to_file(file, _filecontent);
        return file;
    }

    void write_to_file(File _file, byte[] _filecontent) throws Exception {
        FileOutputStream fos = new FileOutputStream(_file, false);
        fos.write(_filecontent);
        fos.close();
    }

    Boolean is_public(String _relative_path) {
        return _relative_path.endsWith(".publ");
    }

}