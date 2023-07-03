package client;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import util.*;

class LoggedinSession {
    private ControlConnection request_sender;

    LoggedinSession(ControlConnection _request_sender) {
        this.request_sender = _request_sender;
    }

    void lsum(String[] _splitted_input) {
        if (!this.run_lsvariant_diagnostics(_splitted_input)) {
            return;
        }
        Response response = this.request_sender.get_response(new Request("lsum"));
        ArrayList<FileRequest> unread_file_requests = (ArrayList<FileRequest>) response.get_body()
                .get("unread_file_requests");
        ArrayList<FileResponse> unread_file_responses = (ArrayList<FileResponse>) response.get_body()
                .get("unread_file_responses");
        this.out_lsum(unread_file_requests, unread_file_responses);
        this.request_sender.get_cli().update();
    }

    boolean run_lsvariant_diagnostics(String[] _splitted_input) {
        if (_splitted_input.length > 1) {
            this.request_sender.get_cli().update("extra arguments given.");
            return false;
        }
        return true;
    }

    void out_lsum(ArrayList<FileRequest> _unread_file_requests, ArrayList<FileResponse> _unread_file_responses) {
        for (FileResponse file_response : _unread_file_responses) {
            System.out.println("- " + file_response.get_public_file().get_owner_name()
                    + " has uploaded file in response to your request.\n"
                    + "\tFILE NAME: " + file_response.get_public_file().get_filename() + "\n"
                    + "\tFILE ID: " + file_response.get_public_file().get_fileid() + "\n\n");
        }
        for (FileRequest file_request : _unread_file_requests) {
            System.out.println("- " + file_request.get_requestee() + " requested a file.\n"
                    + "\tREQUEST ID: " + file_request.get_id() + "\n"
                    + "\tDESCRIPTION: " + file_request.get_description() + "\n\n");
        }
    }

    void lsau(String[] _splitted_input) {
        if (!this.run_lsvariant_diagnostics(_splitted_input)) {
            return;
        }
        Response response = this.request_sender.get_response(new Request("lsau"));
        ArrayList<String> usernames = (ArrayList<String>) response.get_body().get("user_list");
        this.out_lsu(usernames);
        this.request_sender.get_cli().update();
    }

    void out_lsu(ArrayList<String> _usernames) {
        for (String username : _usernames) {
            System.out.println("- " + username);
        }
    }

    void lslu(String[] _splitted_input) {
        if (!this.run_lsvariant_diagnostics(_splitted_input)) {
            return;
        }
        Response response = this.request_sender.get_response(new Request("lslu"));
        ArrayList<String> usernames = (ArrayList<String>) response.get_body().get("user_list");
        this.out_lsu(usernames);
        this.request_sender.get_cli().update();
    }

    void lspf(String[] _splitted_input) {
        if (!this.run_lsvariant_diagnostics(_splitted_input)) {
            return;
        }
        Response response = this.request_sender.get_response(new Request("lspf"));
        ArrayList<PublicFile> public_files = (ArrayList<PublicFile>) response.get_body().get("public_file_list");
        this.out_lspf(public_files);
        this.request_sender.get_cli().update();
    }

    void out_lspf(ArrayList<PublicFile> _public_files) {
        for (PublicFile public_file : _public_files) {
            System.out.println("FILE ID: " + public_file.get_fileid() + " | FILE NAME: " + public_file.get_filename()
                    + " | OWNER: " + public_file.get_owner_name());
        }
    }

    void lsfr(String[] _splitted_input) {
        if (!this.run_lsvariant_diagnostics(_splitted_input)) {
            return;
        }
        Response response = this.request_sender.get_response(new Request("lsfr"));
        ArrayList<FileRequest> file_requests = (ArrayList<FileRequest>) response.get_body().get("file_request_list");
        this.out_lsfr(file_requests);
        this.request_sender.get_cli().update();
    }

    void out_lsfr(ArrayList<FileRequest> _file_requests) {
        for (FileRequest file_request : _file_requests) {
            System.out.println(
                    "REQUEST ID: " + file_request.get_id() + " | REQUESTEE NAME: " + file_request.get_requestee()
                            + " | RESPONSE COUNT: " + file_request.get_response_count() + " | DESCRIPTION: "
                            + file_request.get_description() + "\n");
        }
    }

    void logout(String[] _splitted_input) {
        if (!this.run_logout_diagnostics(_splitted_input)) {
            return;
        }
        Response response = this.request_sender.get_response(new Request("logo"));
        this.request_sender.get_cli().logout();
    }

    boolean run_logout_diagnostics(String[] _splitted_input) {
        if (_splitted_input.length > 1) {
            this.request_sender.get_cli().update("extra arguments given.");
            return false;
        }
        return true;
    }

    void mkdir(String[] _splitted_input) {
        if (!this.run_dir_diagnostics(_splitted_input)) {
            return;
        }
        String[] parameters = { _splitted_input[1] };
        Response response = this.request_sender.get_response(new Request("mkdir", parameters));
        this.out_mkdir(response.get_code());
        this.request_sender.get_cli().update();
    }

    boolean run_dir_diagnostics(String[] _splitted_input) {
        if (_splitted_input.length != 2) {
            out_error_msg("incorrect format.");
            this.request_sender.get_cli().update();
            return false;
        }
        return true;
    }

    void out_mkdir(ResponseCode _response_code) {
        if (_response_code == ResponseCode.DIRECTORY_ALREADY_EXISTS) {
            out_error_msg("directory with this name already exists.");
        }
    }

    void rmdir(String[] _splitted_input) {
        if (!this.run_dir_diagnostics(_splitted_input)) {
            return;
        }
        String[] parameters = { _splitted_input[1] };
        Response response = this.request_sender.get_response(new Request("rmdir", parameters));
        this.out_rmdir(response.get_code());
        this.request_sender.get_cli().update();
    }

    void out_rmdir(ResponseCode _response_code) {
        switch (_response_code) {
            case DIRECTORY_DOES_NOT_EXIST:
                out_error_msg("directory with this name does not exist.");
                break;
            case DIRECTORY_NOT_EMPTY:
                out_error_msg("directory is not empty.");
                break;
            default:
        }
    }

    void cd(String[] _splitted_input) {
        if (!this.run_dir_diagnostics(_splitted_input)) {
            return;
        }
        String[] parameters = { _splitted_input[1] };
        Response response = this.request_sender.get_response(new Request("cd", parameters));
        if (response.get_code() == ResponseCode.SUCCESSFUL_CD) {
            User current_user = (User) response.get_body().get("user");
            this.request_sender.get_cli().setCurrentUser(current_user);
        }
        this.out_cd(response.get_code());
        this.request_sender.get_cli().update();
    }

    void out_cd(ResponseCode _response_code) {
        switch (_response_code) {
            case DIRECTORY_DOES_NOT_EXIST:
                out_error_msg("directory with this name does not exist.");
                break;
            case NOT_A_DIRECTORY:
                out_error_msg("given argument is not a directory.");
                break;
            default:
        }
    }

    void ls(String[] _splitted_input) {
        if (!this.run_ls_diagnostics(_splitted_input)) {
            return;
        }
        Response response;
        if (_splitted_input.length == 1) {
            String[] parameters = { "." };
            response = this.request_sender.get_response(new Request("ls", parameters));
        } else {
            String[] parameters = { _splitted_input[1] };
            response = this.request_sender.get_response(new Request("ls", parameters));
        }
        this.out_ls(response);
        this.request_sender.get_cli().update();
    }

    boolean run_ls_diagnostics(String[] _splitted_input) {
        if (_splitted_input.length > 2) {
            out_error_msg("incorrect format.");
            this.request_sender.get_cli().update();
            return false;
        }
        return true;
    }

    void out_ls(Response _response) {
        switch (_response.get_code()) {
            case SUCCESSFUL_LS:
                HashMap<String, Boolean> files = (HashMap<String, Boolean>) _response.get_body().get("file_list");
                for (String file_name : files.keySet()) {
                    if (files.get(file_name)) {
                        System.out.println(ClientLoader.GREEN_ANSI + "- " + file_name + ClientLoader.RESET_ANSI);
                    } else {
                        System.out.println(ClientLoader.RED_ANSI + "- " + file_name + ClientLoader.RESET_ANSI);
                    }
                }
                break;
            case DIRECTORY_DOES_NOT_EXIST:
                out_error_msg("directory with this name does not exist.");
                break;
            case NOT_A_DIRECTORY:
                out_error_msg("given argument is not a directory.");
                break;
            default:
        }
    }

    void rf(String[] _splitted_input) {
        String description = this.get_rf_description(_splitted_input);
        String[] parameters = { description };
        Response response = this.request_sender.get_response(new Request("rf", parameters));
        this.out_rf(response.get_code());
        this.request_sender.get_cli().update();
    }

    String get_rf_description(String[] _splitted_input) {
        String[] description_words = new String[_splitted_input.length - 1];
        for (int i = 0; i < description_words.length; i++) {
            description_words[i] = _splitted_input[i + 1];
        }
        String description = String.join(" ", description_words);
        return description;
    }

    void out_rf(ResponseCode _response_code) {
        switch (_response_code) {
            case SUCCESSFUL_OPERATION:
                out_success_msg("successful file request.");
                break;
            default:
                out_error_msg("file request failed.");
        }
    }

    void up(String[] _splitted_input) {
        if (!this.run_up_diagnostics(_splitted_input)) {
            return;
        }
        File file = new File(_splitted_input[1]);
        String[] parameters = { _splitted_input[2], Integer.toString((int) file.length()), _splitted_input[3] };
        Request request = new Request("upmeta", parameters);
        Response response = this.request_sender.get_response(request);
        switch (response.get_code()) {
            case SUCCESSFUL_BUFFER_ALLOCATION:
                new DataConnection(response.get_body(), file);
                this.request_sender.get_cli().update();
                break;
            case FAILED_BUFFER_ALLOCATION:
                out_error_msg("file was not uploaded due to peak traffic.");
                this.request_sender.get_cli().update();
                break;
            default:
                out_error_msg("failed upload. you may want to check the file request id.");
                this.request_sender.get_cli().update();
        }
    }

    boolean run_up_diagnostics(String[] _splitted_input) {
        if (_splitted_input.length != 4) {
            out_error_msg("incorrect format. check the number of argument.");
            this.request_sender.get_cli().update();
            return false;
        }
        File file = new File(_splitted_input[1]);
        if ((!file.exists()) || (!file.isFile())) {
            out_error_msg("file does not exist.");
            this.request_sender.get_cli().update();
            return false;
        }
        if (!(_splitted_input[3].equalsIgnoreCase("public") || _splitted_input[3].equalsIgnoreCase("private"))
                && (!is_stoi(_splitted_input[3]))) {
            out_error_msg("incorrect format. check the 4th argument.");
            this.request_sender.get_cli().update();
            return false;
        }
        return true;
    }

    boolean is_stoi(String _s) {
        try {
            Integer.parseInt(_s);
            return true;
        } catch (Exception exception) {
            ClientLoader.debug(exception);
            return false;
        }
    }

    void down(String[] _splitted_input) {
        if (!this.run_down_diagnostics(_splitted_input)) {
            return;
        }
        if (_splitted_input[0].equalsIgnoreCase("down")) {
            File file = new File(_splitted_input[1]);
            if (!file.exists()) {
                this.request_sender.get_cli().update(_splitted_input[1] + " file does not exist.");
                return;
            }
            if (!file.isDirectory()) {
                this.request_sender.get_cli().update(_splitted_input[1] + " is not a directory.");
                return;
            }
            if (!(_splitted_input[4].equalsIgnoreCase("-o") || _splitted_input[4].equalsIgnoreCase("-p"))) {
                this.request_sender.get_cli().update("wrong switch.");
                return;
            }
            String[] temp_parameters = { _splitted_input[4], _splitted_input[3] };
            Request request = new Request("down", temp_parameters);
            Response response = this.request_sender.get_response(request);
            if (response.get_code() == ResponseCode.SUCCESSFUL_DOWNLOAD) {
                byte[] filecontent = (byte[]) response.get_body().get("filecontent");
                Path filepath = Paths.get(_splitted_input[1], _splitted_input[2]);
                File download_destination_file = filepath.toFile();
                try {
                    this.write_to_file(download_destination_file, filecontent);
                } catch (Exception exception) {
                    ClientLoader.debug(exception);
                } finally {
                    this.request_sender.get_cli().update("successful download.");
                }
            } else if (response.get_code() == ResponseCode.DIRECTORY_DOES_NOT_EXIST) {

                this.request_sender.get_cli().update(_splitted_input[3] + " does not exist.");
            } else {
                this.request_sender.get_cli().update("download failed.");
            }
        }
    }

    boolean run_down_diagnostics(String[] _splitted_input) {
        if (_splitted_input.length != 5) {
            out_error_msg("incorrect format. check the number of argument.");
            this.request_sender.get_cli().update();
            return false;
        }
        return true;
    }

    void write_to_file(File _file, byte[] _filecontent) throws Exception {
        FileOutputStream fos = new FileOutputStream(_file, false);
        fos.write(_filecontent);
        fos.close();
    }

    public static void out_error_msg(String _msg) {
        System.out.println(ClientLoader.RED_ANSI + _msg + ClientLoader.RESET_ANSI);
    }

    public static void out_success_msg(String _msg) {
        System.out.println(ClientLoader.GREEN_ANSI + _msg + ClientLoader.RESET_ANSI);
    }

}