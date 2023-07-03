package util;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;
    private String working_directory;
    private HashMap<String, Boolean> file_visibility;
    private ArrayList<FileRequest> unread_file_requests;
    private ArrayList<FileResponse> unread_file_responses;

    public User(String _username, String _password) {
        this.username = _username;
        this.password = _password;
        this.working_directory = "/";
        this.file_visibility = new HashMap<String, Boolean>();
        this.unread_file_requests = new ArrayList<FileRequest>();
        this.unread_file_responses = new ArrayList<FileResponse>();
    }

    public User set_username(String _username) {
        this.username = _username;
        return this;
    }

    public User set_password(String _password) {
        this.password = _password;
        return this;
    }

    public User set_working_directory(String _working_directory) {
        this.working_directory = _working_directory;
        return this;
    }

    public String get_username() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getWorking_directory() {
        return this.working_directory;
    }

    public User add_file_visibility(String _filepath, Boolean _visibility) {
        this.file_visibility.put(_filepath, _visibility);
        return this;
    }

    public User remove_file_visibility(String _filepath) {
        if (this.file_visibility.containsKey(_filepath)) {
            this.file_visibility.remove(_filepath);
        }
        return this;
    }

    public Boolean check_file_visibility(String _filepath) {
        if (this.file_visibility.containsKey(_filepath)) {
            return this.file_visibility.get(_filepath);
        }
        return false;
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

}