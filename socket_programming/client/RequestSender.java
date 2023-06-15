package client;

import java.util.Scanner;

import util.*;

class RequestSender implements Runnable {
    private NetworkUtil networkUtil;
    private CLI cli;
    private Thread thread;

    RequestSender(NetworkUtil _networkUtil) {
        this.networkUtil = _networkUtil;
        this.cli = new CLI();
        this.thread = new Thread(this);
        this.thread.start();
    }

    Response getResponse(Request _request) {
        Response response;
        while (true) {
            try {
                this.networkUtil.write(_request);
                response = (Response) this.networkUtil.read();
                break;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return response;
    }

    void handleInitialState(String _input) {
        if (_input.equalsIgnoreCase("l")) {
            this.cli.login();
        } else if (_input.equalsIgnoreCase("r")) {
            this.cli.register();
        } else {
            this.cli.unknownCommand();
        }
    }

    void handleLoginState(String _input) {

    }

    void handleRegisterState(String _input) {
        String[] parameters = _input.split(" ");
        if (parameters.length == 2) {
            Request request = new Request("regi", parameters);
            Response response = this.getResponse(request);
            if (response.getCode() == 200) {
                this.cli.rightCredentials();
            } else {
                this.cli.wrongCredentials();
            }
        } else {
            this.cli.unknownCommand();
        }
    }

    void handleLoggedinState(String _input) {
        this.cli.logout();
    }

    @Override
    public void run() {
        try {
            Scanner console = new Scanner(System.in);
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
            exception.printStackTrace();
        } finally {
            try {
                this.networkUtil.closeConnection();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}