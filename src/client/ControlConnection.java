package client;

import java.util.Scanner;
import org.json.simple.JSONObject;

import util.*;

class ControlConnection implements Runnable {
    private NetworkUtil network_util;
    private CLI cli;
    private LoggedinSession loggedin_session;
    private Thread thread;

    ControlConnection(NetworkUtil _network_util) {
        this.network_util = _network_util;
        this.cli = new CLI();
        this.loggedin_session = new LoggedinSession(this);
        this.thread = new Thread(this);
        this.thread.start();
    }

    CLI get_cli() {
        return this.cli;
    }

    Response get_response(Request _request) {
        Response response;
        while (true) {
            try {
                this.network_util.write(_request);
                response = (Response) this.network_util.read();
                break;
            } catch (Exception exception) {
                ClientLoader.debug(exception);
            }
        }
        return response;
    }

    void handleInitialState(String _input) {
        String[] splitted_input = _input.split(" ");
        if (splitted_input[0].equalsIgnoreCase("man")) {
            Manual.show_man(splitted_input);
            this.cli.update();
        } else if (splitted_input.length == 1 && splitted_input[0].equalsIgnoreCase("login")) {
            this.cli.login();
        } else if (splitted_input.length == 1 && splitted_input[0].equalsIgnoreCase("reg")) {
            this.cli.register();
        } else if (splitted_input.length == 1 && splitted_input[0].equalsIgnoreCase("q")) {
            try {
                this.network_util.closeConnection();
            } catch (Exception exception) {
                ClientLoader.debug(exception);
            }
            System.exit(0);
        } else {
            this.cli.unknownCommand();
            this.cli.update();
        }
    }

    void handleLoginState(String _input) {
        String[] splitted_input = _input.split(" ");
        if (splitted_input[0].equalsIgnoreCase("man")) {
            Manual.show_man(splitted_input);
            this.cli.update();
        } else if (splitted_input.length == 2) {
            Request request = new Request("logi", splitted_input);
            Response response = this.get_response(request);
            if (response.get_code() == ResponseCode.SUCCESSFUL_LOGIN) {
                JSONObject body = (JSONObject) response.get_body();
                User user = (User) body.get("user");
                this.cli.setCurrentUser(user);
                this.cli.succeed();
            } else if (response.get_code() == ResponseCode.USER_NOT_FOUND) {
                this.cli.failed("user not found.register first.");
            } else if (response.get_code() == ResponseCode.USER_ALREADY_LOGGED_IN) {
                this.cli.failed("user already logged in.");
            } else if (response.get_code() == ResponseCode.INCORRECT_PASSWORD) {
                this.cli.failed("incorrect password.");
            }
        } else {
            this.cli.unknownCommand();
            this.cli.update();
        }
    }

    void handleRegisterState(String _input) {
        String[] splitted_input = _input.split(" ");
        if (splitted_input[0].equalsIgnoreCase("man")) {
            Manual.show_man(splitted_input);
            this.cli.update();
        } else if (splitted_input.length == 2) {
            Request request = new Request("regi", splitted_input);
            Response response = this.get_response(request);
            if (response.get_code() == ResponseCode.SUCCESSFUL_REGISTRATION) {
                JSONObject body = (JSONObject) response.get_body();
                User user = (User) body.get("user");
                this.cli.setCurrentUser(user);
                this.cli.succeed();
            } else {
                this.cli.failed("username already taken. try a different one.");
            }
        } else {
            this.cli.unknownCommand();
            this.cli.update();
        }
    }

    void handleLoggedinState(String _input) {
        String[] splitted_input = _input.split(" ");
        String command = splitted_input[0];
        switch (command) {
            case "":
                this.cli.update();
                break;
            case "lsum":
                this.loggedin_session.lsum(splitted_input);
                break;
            case "lsfr":
                this.loggedin_session.lsfr(splitted_input);
                break;
            case "lsau":
                this.loggedin_session.lsau(splitted_input);
                break;
            case "lslu":
                this.loggedin_session.lslu(splitted_input);
                break;
            case "lspf":
                this.loggedin_session.lspf(splitted_input);
                break;
            case "logout":
                this.loggedin_session.logout(splitted_input);
                break;
            case "mkdir":
                this.loggedin_session.mkdir(splitted_input);
                break;
            case "rmdir":
                this.loggedin_session.rmdir(splitted_input);
                break;
            case "cd":
                this.loggedin_session.cd(splitted_input);
                break;
            case "ls":
                this.loggedin_session.ls(splitted_input);
                break;
            case "rf":
                this.loggedin_session.rf(splitted_input);
                break;
            case "up":
                this.loggedin_session.up(splitted_input);
                break;
            case "down":
                this.loggedin_session.down(splitted_input);
                break;
            case "man":
                Manual.show_man(splitted_input);
                this.cli.update();
                break;
            default:
                this.cli.unknownCommand();
                this.cli.update();
        }
    }

    @Override
    public void run() {
        Scanner console = new Scanner(System.in);
        try {
            while (true) {
                String input = console.nextLine();
                if (this.cli.getState() instanceof InitialState) {
                    this.handleInitialState(input);
                } else if (this.cli.getState() instanceof LoginState) {
                    this.handleLoginState(input);
                } else if (this.cli.getState() instanceof RegisterState) {
                    this.handleRegisterState(input);
                } else if (this.cli.getState() instanceof LoggedinState) {
                    this.handleLoggedinState(input);
                }
            }
        } catch (Exception exception) {
            ClientLoader.debug(exception);
        } finally {
            try {
                this.network_util.closeConnection();
            } catch (Exception exception) {
                ClientLoader.debug(exception);
            }
            console.close();
        }
    }
}