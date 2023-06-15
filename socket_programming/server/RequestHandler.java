package server;

import java.util.HashMap;
import java.util.ArrayList;
import org.json.simple.JSONObject;

import exception.DirectoryDoesNotExistException;
import exception.DirectoryExistsException;
import exception.DirectoryNotEmptyException;
import exception.IncorrectPasswordException;
import exception.NotADirectoryException;
import exception.UserAlreadyLoggedinException;
import exception.UserNotFoundException;
import util.*;

class RequestHandler implements Runnable {
    private User currentUser;
    private Server server;
    private NetworkUtil networkUtil;
    private Thread thread;

    RequestHandler(Server _server, NetworkUtil _networkUtil) {
        this.currentUser = null;
        this.server = _server;
        this.networkUtil = _networkUtil;
        this.thread = new Thread(this);
        this.thread.start();
    }

    void handleRegistration(Request _request) {
        Response response;
        try {
            String username = _request.getParameters()[0];
            String password = _request.getParameters()[1];
            User user = this.server.registerNewUser(username, password);
            this.currentUser = user;
            JSONObject body = new JSONObject();
            body.put("user", user);
            response = new Response(200, body);
        } catch (Exception exception) {
            exception.printStackTrace();
            response = new Response(501);
        }

        try {
            this.networkUtil.write(response);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    void handleLogin(Request _request) {
        Response response;
        try {
            // what if parameters number is not 2
            String username = _request.getParameters()[0];
            String password = _request.getParameters()[1];
            User user = this.server.loginUser(username, password);
            this.currentUser = user;
            JSONObject body = new JSONObject();
            body.put("user", user);
            response = new Response(201, body);
        } catch (UserNotFoundException exception) {
            response = new Response(502);
        } catch (UserAlreadyLoggedinException exception) {
            response = new Response(503);
        } catch (IncorrectPasswordException exception) {
            response = new Response(504);
        }

        try {
            this.networkUtil.write(response);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    void handleLogout(Request _request) {
        Response response = new Response(505);
        if (this.currentUser != null) {
            this.server.logoutUser(this.currentUser.getUsername());
            this.currentUser = null;
            response = new Response(202);
        }

        try {
            this.networkUtil.write(response);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    void handleListUsers(Request _request) {
        HashMap<String, User> users;
        if (_request.getVerb().equalsIgnoreCase("lsau")) {
            users = this.server.getAllUsers();
        } else {
            users = this.server.getLoggedinUsers();
        }
        ArrayList<String> usernames = new ArrayList<String>();
        for (String username : users.keySet()) {
            usernames.add(username);
        }
        JSONObject body = new JSONObject();
        body.put("user", this.currentUser);
        body.put("user_list", usernames);
        Response response = new Response(202, body);

        try {
            this.networkUtil.write(response);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    void handleMakeDir(Request _request) {
        Response response;
        try {
            String dirName = _request.getParameters()[0];
            this.server.mkdir(this.currentUser, dirName);
            response = new Response(203);
        } catch (DirectoryExistsException exception) {
            response = new Response(506);
        }

        try {
            this.networkUtil.write(response);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    void handleRemoveDir(Request _request) {
        Response response;
        try {
            String dirName = _request.getParameters()[0];
            this.server.rmdir(this.currentUser, dirName);
            response = new Response(204);
        } catch (DirectoryDoesNotExistException exception) {
            response = new Response(507);
        } catch (DirectoryNotEmptyException exception) {
            response = new Response(508);
        }

        try {
            this.networkUtil.write(response);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    void handleChangeDir(Request _request) {
        Response response;
        try {
            String dirName = _request.getParameters()[0];
            this.server.cd(this.currentUser, dirName);
            JSONObject body = new JSONObject();
            body.put("user", this.currentUser);
            response = new Response(205, body);
        } catch (DirectoryDoesNotExistException exception) {
            response = new Response(509);
        } catch (NotADirectoryException exception) {
            response = new Response(510);
        }

        try {
            this.networkUtil.write(response);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    void handleListDir(Request _request) {
        Response response;
        try {
            String dirName = _request.getParameters()[0];
            ArrayList<String> fileNames = this.server.ls(currentUser, dirName);
            JSONObject body = new JSONObject();
            body.put("user", this.currentUser);
            body.put("file_list", fileNames);
            response = new Response(206, body);
        } catch (DirectoryDoesNotExistException exception) {
            response = new Response(511);
        } catch (NotADirectoryException exception) {
            response = new Response(512);
        }

        try {
            this.networkUtil.write(response);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Request request = (Request) this.networkUtil.read();
                String requestVerb = request.getVerb();

                switch (requestVerb) {
                    case "regi":
                        this.handleRegistration(request);
                        break;
                    case "logi":
                        this.handleLogin(request);
                        break;
                    case "logo":
                        this.handleLogout(request);
                        break;
                    case "lsau":
                        this.handleListUsers(request);
                        break;
                    case "lslu":
                        this.handleListUsers(request);
                        break;
                    case "ls":
                        this.handleListDir(request);
                        break;
                    case "mkdir":
                        this.handleMakeDir(request);
                        break;
                    case "rmdir":
                        this.handleRemoveDir(request);
                        break;
                    case "cd":
                        this.handleChangeDir(request);
                        break;
                    default:
                }

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                this.networkUtil.closeConnection();
                if (this.currentUser != null) {
                    this.server.logoutUser(this.currentUser.getUsername());
                    this.currentUser = null;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}