package util;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.Serializable;

public class User implements Serializable {
    private boolean loginStatus;
    private String username;
    private String password;
    private String workingDir;
    private ArrayList<FileRequest> unread_file_requests;
    private ArrayList<FileResponse> unread_file_responses;
    private HashMap<String, Boolean> fileVisibility;

    public User(String _username, String _password) {
        this.username = _username;
        this.password = _password;
        this.unread_file_requests = new ArrayList<FileRequest>();
        this.unread_file_responses = new ArrayList<FileResponse>();
        this.fileVisibility = new HashMap<String, Boolean>();
        this.loginStatus = false;
        this.workingDir = "/";
    }

    public ArrayList<FileRequest> get_unread_file_requests() {
        return this.unread_file_requests;
    }

    public User add_unread_file_requests(FileRequest _file_request) {
        this.unread_file_requests.add(_file_request);
        return this;
    }

    public User clear_unread_file_requests() {
        this.unread_file_requests.clear();
        return this;
    }

    public ArrayList<FileResponse> get_unread_file_responses() {
        return this.unread_file_responses;
    }

    public User add_unread_file_response(FileResponse _file_response) {
        this.unread_file_responses.add(_file_response);
        return this;
    }

    public User clear_unread_file_responses() {
        this.unread_file_responses.clear();
        return this;
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