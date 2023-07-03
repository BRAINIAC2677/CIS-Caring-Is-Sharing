package server;

import java.util.HashMap;

import util.*;

class ControlConnection implements Runnable {
    User current_user;
    private NetworkUtil network_util;
    private Thread thread;
    private HashMap<String, Command> commands;

    ControlConnection(NetworkUtil _network_util) {
        this.commands = new HashMap<String, Command>();
        this.commands.put("regi", new RegistrationCommand(this));
        this.commands.put("logi", new LoginCommand(this));
        this.commands.put("logo", new LogoutCommand(this));
        this.commands.put("lsau", new ListUserCommand());
        this.commands.put("lslu", new ListUserCommand());
        this.commands.put("lspf", new ListPublicFileCommand());
        this.commands.put("lsfr", new ListFileRequestCommand());
        this.commands.put("ls", new ListDirectoryCommand(this));
        this.commands.put("mkdir", new MakeDirectoryCommand(this));
        this.commands.put("rmdir", new RemoveDirectoryCommand(this));
        this.commands.put("cd", new ChangeDirectoryCommand(this));
        this.commands.put("upmeta", new UploadCommand(this));
        this.commands.put("down", new DownloadCommand(this));
        this.commands.put("rf", new FileRequestCommand(this));
        this.commands.put("lsum", new ListUnreadMessagesCommand(this));
        this.current_user = null;
        this.network_util = _network_util;
        this.thread = new Thread(this);
        this.thread.start();
    }

    void send_response(Response _response) {
        try {
            this.network_util.write(_response);
        } catch (Exception exception) {
            ServerLoader.debug(exception);
        }
    }

    Request get_request() throws Exception {
        Request request = (Request) this.network_util.read();
        return request;
    }

    NetworkUtil get_network_util() {
        return this.network_util;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Request request = this.get_request();
                String request_verb = request.get_verb();
                Response response = this.commands.get(request_verb).execute(request);
                this.send_response(response);
            }
        } catch (Exception exception) {
            ServerLoader.debug(exception);
        } finally {
            try {
                this.network_util.closeConnection();
                if (this.current_user != null) {
                    ControlConnectionListener.get_instance().get_user_base()
                            .logout_user(this.current_user.getUsername());
                    this.current_user = null;
                }
            } catch (Exception exception) {
                ServerLoader.debug(exception);
            }
        }
    }
}