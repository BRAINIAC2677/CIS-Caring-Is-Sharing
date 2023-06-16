package util;

import java.util.HashMap;
import java.io.Serializable;

public class User implements Serializable {
    private boolean loginStatus;
    private String username;
    private String password;
    private String workingDir;
    private HashMap<String, Boolean> fileVisibility;

    public User(String _username, String _password) {
        this.username = _username;
        this.password = _password;
        this.fileVisibility = new HashMap<String, Boolean>();
        this.loginStatus = false;
        this.workingDir = "/";
    }

    public User addFileVisibility(String _filepath, Boolean _visibility) {
        this.fileVisibility.put(_filepath, _visibility);
        return this;
    }

    public User removeFileVisibility(String _filepath) {
        if (this.fileVisibility.containsKey(_filepath)) {
            this.fileVisibility.remove(_filepath);
        }
        return this;
    }

    public Boolean checkFileVisibility(String _filepath) {
        if (this.fileVisibility.containsKey(_filepath)) {
            return this.fileVisibility.get(_filepath);
        }
        return false;
    }

    public User setLoginStatus(boolean _loginStatus) {
        this.loginStatus = _loginStatus;
        return this;
    }

    public User setUsername(String _username) {
        this.username = _username;
        return this;
    }

    public User setPassword(String _password) {
        this.password = _password;
        return this;
    }

    public User setWorkingDir(String _workingDir) {
        this.workingDir = _workingDir;
        return this;
    }

    public boolean getLoginStatus() {
        return this.loginStatus;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getWorkingDir() {
        return this.workingDir;
    }

}