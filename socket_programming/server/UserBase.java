package server;

import java.util.HashMap;
import java.util.ArrayList;

import util.*;
import exception.*;

class UserBase {
    private HashMap<String, User> all_users;
    private HashMap<String, User> loggedin_users;
    private HashMap<String, RemoteCLI> remote_clis;

    UserBase() {
        this.all_users = new HashMap<String, User>();
        this.loggedin_users = new HashMap<String, User>();
        this.remote_clis = new HashMap<String, RemoteCLI>();
    }

    public RemoteCLI get_remote_cli(String _username) {
        return this.remote_clis.get(_username);
    }

    public User register_user(String _username, String _password) throws Exception {
        this.run_register_diagnostics(_username);
        User new_user = new User(_username, _password);
        this.create_user_root_directory(_username);
        this.all_users.put(_username, new_user);
        this.loggedin_users.put(_username, new_user);
        this.remote_clis.put(_username, new RemoteCLI("socket_programming/storage/" + _username + ".publ", new_user));
        return new_user;
    }

    void run_register_diagnostics(String _username) throws UsernameUnavailableException {
        if (this.all_users.containsKey(_username)) {
            throw new UsernameUnavailableException(_username);
        }
    }

    void create_user_root_directory(String _username) {
        try {
            String root_directory = "socket_programming/storage/" + _username + ".publ";
            FileUtil.create_directory(root_directory);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public User login_user(String _username, String _password) throws Exception {
        this.run_login_diagnostics(_username, _password);
        User user = this.all_users.get(_username);
        this.loggedin_users.put(user.getUsername(), user);
        this.remote_clis.put(_username, new RemoteCLI("socket_programming/storage/" + _username + ".publ", user));
        return user;
    }

    void run_login_diagnostics(String _username, String _password)
            throws UserNotFoundException, UserAlreadyLoggedinException, IncorrectPasswordException {
        if (!this.all_users.containsKey(_username)) {
            throw new UserNotFoundException(_username);
        } else if (this.loggedin_users.containsKey(_username)) {
            throw new UserAlreadyLoggedinException(_username);
        } else if (!this.all_users.get(_username).getPassword().equals(_password)) {
            throw new IncorrectPasswordException(_username);
        }
    }

    public void logout_user(String _username) throws Exception {
        this.run_logout_diagnostics(_username);
        this.loggedin_users.remove(_username);
        this.remote_clis.remove(_username);
    }

    void run_logout_diagnostics(String _username) throws UserNotLoggedinException {
        if (!this.loggedin_users.containsKey(_username)) {
            throw new UserNotLoggedinException(_username);
        }
    }

    public ArrayList<String> get_all_usernames() {
        ArrayList<String> all_usernames = new ArrayList<String>();
        for (String username : this.all_users.keySet()) {
            all_usernames.add(username);
        }
        return all_usernames;
    }

    public ArrayList<String> get_loggedin_usernames() {
        ArrayList<String> loggedin_usernames = new ArrayList<String>();
        for (String username : this.loggedin_users.keySet()) {
            loggedin_usernames.add(username);
        }
        return loggedin_usernames;
    }
}