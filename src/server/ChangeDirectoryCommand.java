package server;

import util.*;
import exception.*;

class ChangeDirectoryCommand implements Command {
    private ControlConnection request_handler;

    public ChangeDirectoryCommand(ControlConnection _request_handler) {
        this.request_handler = _request_handler;
    }

    @Override
    public Response execute(Request _request) {
        Response response;
        String directory_name = _request.get_parameters()[0];
        try {
            ControlConnectionListener.get_instance().get_user_base()
                    .get_remote_cli(this.request_handler.current_user.get_username())
                    .cd(directory_name);
            response = (new Response(ResponseCode.SUCCESSFUL_CD)).add_user(this.request_handler.current_user);
        } catch (Exception exception) {
            ServerLoader.debug(exception);
            if (exception instanceof DirectoryDoesNotExistException) {
                response = new Response(ResponseCode.DIRECTORY_DOES_NOT_EXIST);
            } else {
                response = new Response(ResponseCode.NOT_A_DIRECTORY);
            }
        }
        return response;
    }

}