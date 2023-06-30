package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.simple.JSONObject;

import util.*;

class RequestSender implements Runnable {
    public static final String RED_ANSI = "\u001B[31m";
    public static final String GREEN_ANSI = "\u001B[32m";
    public static final String RESET_ANSI = "\u001B[0m";
    private NetworkUtil network_util;
    private CLI cli;
    private Thread thread;

    RequestSender(NetworkUtil _network_util) {
        this.network_util = _network_util;
        this.cli = new CLI();
        this.thread = new Thread(this);
        this.thread.start();
    }

    Response getResponse(Request _request) {
        Response response;
        while (true) {
            try {
                this.network_util.write(_request);
                response = (Response) this.network_util.read();
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
                this.network_util.closeConnection();
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
            if (response.getCode() == ResponseCode.SUCCESSFUL_LOGIN) {
                JSONObject body = (JSONObject) response.getBody();
                User user = (User) body.get("user");
                this.cli.setCurrentUser(user);
                this.cli.succeed();
            } else if (response.getCode() == ResponseCode.USER_NOT_FOUND) {
                this.cli.failed("user not found.register first.");
            } else if (response.getCode() == ResponseCode.USER_ALREADY_LOGGED_IN) {
                this.cli.failed("user already logged in.");
            } else if (response.getCode() == ResponseCode.INCORRECT_PASSWORD) {
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
            if (response.getCode() == ResponseCode.SUCCESSFUL_REGISTRATION) {
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
            } else if (parameters[0].equalsIgnoreCase("lsum")) {
                Request request = new Request("lsum");
                Response response = this.getResponse(request);
                ArrayList<FileRequest> unread_file_requests = (ArrayList<FileRequest>) response.getBody()
                        .get("unread_file_requests");
                ArrayList<FileResponse> unread_file_responses = (ArrayList<FileResponse>) response.getBody()
                        .get("unread_file_responses");
                if (unread_file_responses.size() > 0) {
                    System.out.println("File Responses");
                }
                for (FileResponse file_response : unread_file_responses) {
                    System.out.println("- " + file_response.get_public_file().get_filename() + " having file id: "
                            + file_response.get_public_file().get_fileid()
                            + "have been uploaded in response to you request.");
                }
                if (unread_file_requests.size() > 0) {
                    System.out.println("File Requests");
                }
                for (FileRequest file_request : unread_file_requests) {
                    System.out.println("- " + file_request.get_id() + " | " + file_request.get_requestee() + " | "
                            + file_request.get_description());
                }
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

            } else if (parameters[0].equalsIgnoreCase("lspf")) {
                Request request = new Request("lspf");
                Response response = this.getResponse(request);
                ArrayList<PublicFile> public_files = (ArrayList<PublicFile>) response.getBody().get("public_file_list");
                for (PublicFile public_file : public_files) {
                    System.out.println(public_file.get_fileid() + "----" + public_file.get_filename() + "----"
                            + public_file.get_owner_name());
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
                if (response.getCode() == ResponseCode.SUCCESSFUL_MKDIR) {
                    this.cli.update();
                } else {
                    this.cli.update(parameters[1] + " directory already exists.");
                }
            } else if (parameters[0].equalsIgnoreCase("rmdir")) {
                String[] tempParameters = { parameters[1] };
                Request request = new Request("rmdir", tempParameters);
                Response response = this.getResponse(request);
                switch (response.getCode()) {
                    case SUCCESSFUL_RMDIR:
                        this.cli.update();
                        break;
                    case DIRECTORY_DOES_NOT_EXIST:
                        this.cli.update(parameters[1] + " does not exist.");
                        break;
                    case DIRECTORY_NOT_EMPTY:
                        this.cli.update(parameters[1] + " is not empty.");
                        break;
                    default:
                }

            } else if (parameters[0].equalsIgnoreCase("cd")) {
                String[] tempParameters = { parameters[1] };
                Request request = new Request("cd", tempParameters);
                Response response = this.getResponse(request);
                switch (response.getCode()) {
                    case SUCCESSFUL_CD:
                        User currentUser = (User) response.getBody().get("user");
                        this.cli.setCurrentUser(currentUser);
                        this.cli.update();
                        break;
                    case DIRECTORY_DOES_NOT_EXIST:
                        this.cli.update(parameters[1] + " does not exist.");
                        break;
                    case NOT_A_DIRECTORY:
                        this.cli.update(parameters[1] + " is not a directory.");
                        break;
                    default:
                }

            } else if (parameters[0].equalsIgnoreCase("ls")) {
                String[] tempParameters = { parameters[1] };
                Request request = new Request("ls", tempParameters);
                Response response = this.getResponse(request);
                switch (response.getCode()) {
                    case SUCCESSFUL_LS:
                        HashMap<String, Boolean> files = (HashMap<String, Boolean>) response.getBody().get("file_list");
                        for (String fileName : files.keySet()) {
                            if (files.get(fileName)) {
                                System.out.println(GREEN_ANSI + "- " + fileName + RESET_ANSI);
                            } else {

                                System.out.println(RED_ANSI + "- " + fileName + RESET_ANSI);
                            }
                        }
                        this.cli.update();
                        break;
                    case DIRECTORY_DOES_NOT_EXIST:
                        this.cli.update(parameters[1] + " does not exist.");
                        break;
                    case NOT_A_DIRECTORY:
                        this.cli.update(parameters[1] + " is not a directory.");
                        break;
                    default:
                }

            } else if (parameters[0].equalsIgnoreCase("rf")) {
                String[] temp_parameters = { parameters[1] };
                Request request = new Request("rf", temp_parameters);
                Response response = this.getResponse(request);
                if (response.getCode() == ResponseCode.SUCCESSFUL_OPERATION) {
                    this.cli.update("successful file request.");
                } else {
                    this.cli.update("file request failed.");
                }
            } else {
                this.cli.unknownCommand();
            }
        } else if (parameters.length == 4) {
            if (parameters[0].equalsIgnoreCase("up")) {
                File file = new File(parameters[1]);
                if (!file.exists()) {
                    this.cli.update(parameters[1] + " file does not exist.");
                    return;
                }
                Integer file_request_id = null;
                try {
                    file_request_id = Integer.parseInt(parameters[3]);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                if (!(parameters[3].equalsIgnoreCase("public") || parameters[3].equalsIgnoreCase("private"))
                        && file_request_id == null) {
                    this.cli.unknownCommand();
                    return;
                }
                Request request;
                if (file_request_id == null) {
                    String[] tempParameters = { parameters[2], Integer.toString((int) file.length()), parameters[3] };
                    request = new Request("upmeta", tempParameters);
                } else {
                    String[] tempParameters = { parameters[2], Integer.toString((int) file.length()), parameters[3],
                            file_request_id.toString() };
                    request = new Request("upmeta", tempParameters);
                }
                Response response = this.getResponse(request);
                if (response.getCode() == ResponseCode.SUCCESSFUL_BUFFER_ALLOCATION) {
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
                            if (response.getCode() == ResponseCode.SUCCESSFUL_BUFFER_ALLOCATION) {
                                fis.read(chunk);
                            } else {
                                this.cli.update("unsuccessful upload.");
                                return;
                            }
                        }
                        request = new Request("upcomp");
                        response = this.getResponse(request);
                        if (response.getCode() == ResponseCode.SUCCESSFUL_UPLOAD) {
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
        } else if (parameters.length == 5) {
            if (parameters[0].equalsIgnoreCase("down")) {
                File file = new File(parameters[1]);
                if (!file.exists()) {
                    this.cli.update(parameters[1] + " file does not exist.");
                    return;
                }
                if (!file.isDirectory()) {
                    this.cli.update(parameters[1] + " is not a directory.");
                    return;
                }
                if (!(parameters[4].equalsIgnoreCase("-o") || parameters[4].equalsIgnoreCase("-p"))) {
                    this.cli.update("wrong switch.");
                    return;
                }
                String[] temp_parameters = { parameters[4], parameters[3] };
                Request request = new Request("down", temp_parameters);
                Response response = this.getResponse(request);
                if (response.getCode() == ResponseCode.SUCCESSFUL_DOWNLOAD) {
                    byte[] filecontent = (byte[]) response.getBody().get("filecontent");
                    Path filepath = Paths.get(parameters[1], parameters[2]);
                    File download_destination_file = filepath.toFile();
                    try {
                        this.write_to_file(download_destination_file, filecontent);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    } finally {
                        this.cli.update("successful download.");
                    }
                } else if (response.getCode() == ResponseCode.DIRECTORY_DOES_NOT_EXIST) {

                    this.cli.update(parameters[3] + " does not exist.");
                } else {
                    this.cli.update("download failed.");
                }
            } else {
                this.cli.unknownCommand();
            }
        } else {
            this.cli.unknownCommand();
        }

    }

    void write_to_file(File _file, byte[] _filecontent) throws Exception {
        FileOutputStream fos = new FileOutputStream(_file, false);
        fos.write(_filecontent);
        fos.close();
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
                this.network_util.closeConnection();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}