package util;

import java.io.Serializable;

public class User implements Serializable {
    private boolean loginStatus;
    private String username;
    private String password;
    private String workingDir;

    public User(String _username, String _password) {
        this.username = _username;
        this.password = _password;
        this.loginStatus = false;
        this.workingDir = "/";
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