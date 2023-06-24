package server;

import java.util.HashMap;
import java.util.ArrayList;
import org.json.simple.JSONObject;

import exception.DirectoryDoesNotExistException;
import exception.DirectoryExistsException;
import exception.UserAlreadyLoggedinException;
import exception.UserNotFoundException;
import util.*;

class RequestHandler implements Runnable {
    User current_user;
    private NetworkUtil networkUtil;
    private Thread thread;
    private HashMap<String, Command> commands;

    RequestHandler(NetworkUtil _networkUtil) {
        commands = new HashMap<String, Command>();
        commands.put("regi", new RegistrationCommand(this));
        commands.put("logi", new LoginCommand(this));
        commands.put("logo", new LogoutCommand(this));
        commands.put("lsau", new ListUserCommand());
        commands.put("lslu", new ListUserCommand());
        commands.put("ls", new ListDirectoryCommand(this));
        commands.put("mkdir", new MakeDirectoryCommand(this));
        commands.put("rmdir", new RemoveDirectoryCommand(this));
        commands.put("cd", new ChangeDirectoryCommand(this));
        commands.put("upmeta", new UploadCommand(this));

        this.current_user = null;
        this.networkUtil = _networkUtil;
        this.thread = new Thread(this);
        this.thread.start();
    }

    void send_response(Response _response) {
        try {
            this.networkUtil.write(_response);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    Request get_request() throws Exception {
        Request request = (Request) this.networkUtil.read();
        return request;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Request request = this.get_request();
                String requestVerb = request.getVerb();

                switch (requestVerb) {
                    case "upmeta":
                        this.commands.get(requestVerb).execute(request);
                        break;
                    default:
                        Response response = this.commands.get(requestVerb).execute(request);
                        this.send_response(response);
                }

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                this.networkUtil.closeConnection();
                if (this.current_user != null) {
                    Server.get_instance().get_user_base().logout_user(this.current_user.getUsername());
                    this.current_user = null;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}