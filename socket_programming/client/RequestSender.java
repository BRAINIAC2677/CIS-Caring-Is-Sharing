package client;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.json.simple.JSONObject;

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
        } else if (_input.equalsIgnoreCase("q")) {
            try {
                this.networkUtil.closeConnection();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            System.exit(0);
        } else {
            this.cli.unknownCommand();
        }
    }

    void handleLoginState(String _input) {
        String[] parameters = _input.split(" ");
        if (parameters.length == 2) {
            Request request = new Request("logi", parameters);
            Response response = this.getResponse(request);
            if (response.getCode() == 201) {
                JSONObject body = (JSONObject) response.getBody();
                User user = (User) body.get("user");
                this.cli.setCurrentUser(user);
                this.cli.succeed();
            } else if (response.getCode() == 502) {
                this.cli.failed("user not found.register first.");
            } else if (response.getCode() == 503) {
                this.cli.failed("user already logged in.");
            } else if (response.getCode() == 504) {
                this.cli.failed("incorrect password.");
            }
        } else {
            this.cli.unknownCommand();
        }
    }

    void handleRegisterState(String _input) {
        String[] parameters = _input.split(" ");
        if (parameters.length == 2) {
            Request request = new Request("regi", parameters);
            Response response = this.getResponse(request);
            if (response.getCode() == 200) {
                JSONObject body = (JSONObject) response.getBody();
                User user = (User) body.get("user");
                this.cli.setCurrentUser(user);
                this.cli.succeed();
            } else {
                this.cli.failed("username already taken. try a different one.");
            }
        } else {
            this.cli.unknownCommand();
        }
    }

    void handleLoggedinState(String _input) {
        String[] parameters = _input.split(" ");
        if (parameters.length == 1) {
            if (parameters[0].equalsIgnoreCase("")) {
                this.cli.update();
            } else if (parameters[0].equalsIgnoreCase("lsau")) {
                Request request = new Request("lsau");
                Response response = this.getResponse(request);
                ArrayList<String> usernames = (ArrayList<String>) response.getBody().get("user_list");
                for (String username : usernames) {
                    System.out.println("- " + username);
                }
                this.cli.update();
            } else if (parameters[0].equalsIgnoreCase("lslu")) {
                Request request = new Request("lslu");
                Response response = this.getResponse(request);
                ArrayList<String> usernames = (ArrayList<String>) response.getBody().get("user_list");
                for (String username : usernames) {
                    System.out.println("- " + username);
                }
                this.cli.update();

            } else if (parameters[0].equalsIgnoreCase("logout")) {
                Request request = new Request("logo");
                Response response = this.getResponse(request);
                this.cli.logout();
            } else {
                this.cli.unknownCommand();
            }
        } else if (parameters.length == 2) {
            if (parameters[0].equalsIgnoreCase("mkdir")) {
                String[] tempParameters = { parameters[1] };
                Request request = new Request("mkdir", tempParameters);
                Response response = this.getResponse(request);
                if (response.getCode() == 203) {
                    this.cli.update();
                } else {
                    this.cli.update(parameters[1] + " directory already exists.");
                }
            } else if (parameters[0].equalsIgnoreCase("rmdir")) {
                String[] tempParameters = { parameters[1] };
                Request request = new Request("rmdir", tempParameters);
                Response response = this.getResponse(request);
                switch (response.getCode()) {
                    case 204:
                        this.cli.update();
                        break;
                    case 507:
                        this.cli.update(parameters[1] + " does not exist.");
                        break;
                    case 508:
                        this.cli.update(parameters[1] + " is not empty.");
                        break;
                    default:
                }

            } else if (parameters[0].equalsIgnoreCase("cd")) {
                String[] tempParameters = { parameters[1] };
                Request request = new Request("cd", tempParameters);
                Response response = this.getResponse(request);
                switch (response.getCode()) {
                    case 205:
                        User currentUser = (User) response.getBody().get("user");
                        this.cli.setCurrentUser(currentUser);
                        this.cli.update();
                        break;
                    case 509:
                        this.cli.update(parameters[1] + " does not exist.");
                        break;
                    case 510:
                        this.cli.update(parameters[1] + " is not a directory.");
                        break;
                    default:
                }

            } else if (parameters[0].equalsIgnoreCase("ls")) {
                String[] tempParameters = { parameters[1] };
                Request request = new Request("ls", tempParameters);
                Response response = this.getResponse(request);
                switch (response.getCode()) {
                    case 206:
                        ArrayList<String> fileNames = (ArrayList<String>) response.getBody().get("file_list");
                        for (String fileName : fileNames) {
                            System.out.println("- " + fileName);
                        }
                        this.cli.update();
                        break;
                    case 511:
                        this.cli.update(parameters[1] + " does not exist.");
                        break;
                    case 512:
                        this.cli.update(parameters[1] + " is not a directory.");
                        break;
                    default:
                }

            } else {
                this.cli.unknownCommand();
            }
        } else if (parameters.length == 3) {
            if (parameters[0].equalsIgnoreCase("up")) {
                File file = new File(parameters[1]);
                if (!file.exists()) {
                    this.cli.update(parameters[1] + " file does not exist.");
                    return;
                }
                String[] tempParameters = { parameters[2], Integer.toString((int) file.length()) };
                Request request = new Request("upmeta", tempParameters);
                Response response = this.getResponse(request);
                if (response.getCode() == 207) {
                    FileInputStream fis;
                    int chunkSize = (int) response.getBody().get("chunk_size");
                    int remainingFileSize = (int) file.length();

                    try {
                        fis = new FileInputStream(file);
                        while (remainingFileSize > 0) {

                            System.out.println("remainingFileSize: " + remainingFileSize + " chunkSize: " + chunkSize);

                            byte[] chunk = new byte[Math.min(remainingFileSize, chunkSize)];
                            fis.read(chunk);
                            remainingFileSize -= chunk.length;
                            JSONObject body = new JSONObject();
                            body.put("chunk", chunk);
                            request = (new Request("updata")).setBody(body);
                            response = this.getResponse(request);
                            if (response.getCode() == 207) {
                                fis.read(chunk);
                            } else {
                                this.cli.update("unsuccessful upload.");
                                return;
                            }
                        }
                        request = new Request("upcomp");
                        response = this.getResponse(request);
                        if (response.getCode() == 208) {
                            this.cli.update("successful upload.");
                        } else {
                            this.cli.update("unsuccessful upload.");
                        }
                        fis.close();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                } else {
                    this.cli.update(parameters[1] + " was not uploaded due to peak traffic.");
                }
            } else {
                this.cli.unknownCommand();
            }
        } else {
            this.cli.unknownCommand();
        }

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