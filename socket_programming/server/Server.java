package server;

import java.util.HashMap;
import java.io.IOException;
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

import util.*;
import exception.UsernameUnavailableException;

public class Server {
    private ServerSocket serverSocket;
    private HashMap<String, User> allUsers;
    private HashMap<String, User> loggedinUsers;

    Server() {
        try {
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

    public static void main(String args[]) {
        Server server = new Server();
    }
}
