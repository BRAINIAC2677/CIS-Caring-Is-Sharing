package server;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import util.*;
import exception.DirectoryDoesNotExistException;
import exception.DirectoryExistsException;
import exception.DirectoryNotEmptyException;
import exception.IncorrectPasswordException;
import exception.NotADirectoryException;
import exception.UserAlreadyLoggedinException;
import exception.UserNotFoundException;
import exception.UsernameUnavailableException;

public class Server {
    private int currentBufferSize;
    private int maxBufferSize;
    private int minChunkSize;
    private int maxChunkSize;
    private ServerSocket serverSocket;
    private HashMap<String, User> allUsers;
    private HashMap<String, User> loggedinUsers;

    Server(int _maxBufferSize, int _minChunkSize, int _maxChunkSize) {
        try {
            this.currentBufferSize = 0;
            this.maxBufferSize = _maxBufferSize;
            this.minChunkSize = _minChunkSize;
            this.maxChunkSize = _maxChunkSize;
            this.serverSocket = new ServerSocket(33333);
            this.allUsers = new HashMap<String, User>();
            this.loggedinUsers = new HashMap<String, User>();

            while (true) {
                Socket clientSocket = this.serverSocket.accept();
                this.serve(clientSocket);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void serve(Socket _clientSocket) throws IOException {
        NetworkUtil networkUtil = new NetworkUtil(_clientSocket);
        new RequestHandler(this, networkUtil);
    }

    HashMap<String, User> getAllUsers() {
        return this.allUsers;
    }

    HashMap<String, User> getLoggedinUsers() {
        return this.loggedinUsers;
    }

    String getUserRootDir(User _user) {
        return "socket_programming/storage/" + _user.getUsername();
    }

    User registerNewUser(String _username, String _password) throws UsernameUnavailableException {
        if (this.allUsers.containsKey(_username)) {
            throw new UsernameUnavailableException(_username);
        }
        User newUser = new User(_username, _password);
        File newDir = new File(this.getUserRootDir(newUser));
        newDir.mkdir();
        this.allUsers.put(_username, newUser);
        this.loggedinUsers.put(_username, newUser);
        return newUser;
    }

    User loginUser(String _username, String _password)
            throws UserNotFoundException, UserAlreadyLoggedinException, IncorrectPasswordException {
        if (!this.allUsers.containsKey(_username)) {
            throw new UserNotFoundException(_username);
        } else if (this.loggedinUsers.containsKey(_username)) {
            throw new UserAlreadyLoggedinException(_username);
        } else if (!this.allUsers.get(_username).getPassword().equals(_password)) {
            throw new IncorrectPasswordException(_username);
        } else {
            User user = this.allUsers.get(_username);
            this.loggedinUsers.put(user.getUsername(), user);
            return user;
        }
    }

    void logoutUser(String _username) {
        if (this.loggedinUsers.containsKey(_username)) {
            this.loggedinUsers.remove(_username);
        }
    }

    void mkdir(User _user, String _dirName) throws DirectoryExistsException {
        String path = this.getUserRootDir(_user) + _user.getWorkingDir();
        if (path.charAt(path.length() - 1) != '/') {
            path += "/";
        }
        path += _dirName;
        File newDir = new File(path);
        if (newDir.exists()) {
            throw new DirectoryExistsException(_dirName);
        }
        newDir.mkdir();
    }

    void rmdir(User _user, String _dirName) throws DirectoryDoesNotExistException, DirectoryNotEmptyException {
        String path = this.getUserRootDir(_user) + _user.getWorkingDir();
        if (path.charAt(path.length() - 1) != '/') {
            path += "/";
        }
        path += _dirName;
        File newDir = new File(path);
        if (!newDir.exists()) {
            throw new DirectoryDoesNotExistException(_dirName);
        }
        if (newDir.list().length > 0) {
            throw new DirectoryNotEmptyException(_dirName);
        }
        newDir.delete();
    }

    void cd(User _user, String _dirName) throws DirectoryDoesNotExistException, NotADirectoryException {
        if (_dirName.equals("..")) {
            String wd = _user.getWorkingDir();
            if (!wd.equals("/")) {
                wd = wd.substring(0, wd.lastIndexOf('/'));
                if (wd.length() == 0) {
                    wd = "/";
                }
                _user.setWorkingDir(wd);
            }
            return;
        }
        String path = this.getUserRootDir(_user) + _user.getWorkingDir();
        if (path.charAt(path.length() - 1) != '/') {
            path += "/";
        }
        path += _dirName;
        File dir = new File(path);
        if (!dir.exists()) {
            throw new DirectoryDoesNotExistException(_dirName);
        }
        if (!dir.isDirectory()) {
            throw new NotADirectoryException(_dirName);
        }
        String changedDir = _user.getWorkingDir() + "/" + _dirName;
        if (_user.getWorkingDir().equals("/")) {
            changedDir = "/" + _dirName;
        }
        _user.setWorkingDir(changedDir);
    }

    ArrayList<String> ls(User _user, String _dirName) throws DirectoryDoesNotExistException, NotADirectoryException {
        String path = this.getUserRootDir(_user) + _user.getWorkingDir();
        if (_dirName != ".") {
            if (path.charAt(path.length() - 1) != '/') {
                path += "/";
            }
            path += _dirName;
        }
        File dir = new File(path);
        if (!dir.exists()) {
            throw new DirectoryDoesNotExistException(_dirName);
        }
        if (!dir.isDirectory()) {
            throw new NotADirectoryException(_dirName);
        }
        ArrayList<String> fileNames = new ArrayList<String>();
        for (String fileName : dir.list()) {
            fileNames.add(fileName);
        }
        return fileNames;
    }

    public boolean deleteDirectory(File _directoryToBeDeleted) {
        File[] allContents = _directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return _directoryToBeDeleted.delete();
    }

    void createFile(User _user, byte[] _fileContent, String _fileName) {
        String path = this.getUserRootDir(_user) + _user.getWorkingDir();
        if (path.charAt(path.length() - 1) != '/') {
            path += "/";
        }
        path += _fileName;
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(_fileContent);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    int allocateBuffer(int _fileSize) {
        if (this.currentBufferSize + _fileSize <= this.maxBufferSize) {
            this.currentBufferSize += _fileSize;
            return _fileSize;
        }
        return -1;
    }

    int releaseBuffer(int _fileSize) {
        if (this.currentBufferSize >= _fileSize) {
            this.currentBufferSize -= _fileSize;
            return _fileSize;
        }
        return -1;
    }

    int getRandomChunkSize() {
        Random random = new Random();
        return minChunkSize + random.nextInt(maxChunkSize - minChunkSize + 1);
    }

    public static void main(String args[]) {
        int maxBufferSize = 2000000000;
        int minChunkSize = 1000000000;
        int maxChunkSize = 2000000000;
        Server server = new Server(maxBufferSize, minChunkSize, maxChunkSize);
        File storageFile = new File("socket_programming/storage");
        server.deleteDirectory(storageFile);
    }
}
