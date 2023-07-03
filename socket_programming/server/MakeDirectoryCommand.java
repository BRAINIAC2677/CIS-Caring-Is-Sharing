package server;

import util.*;
import exception.*;

class MakeDirectoryCommand implements Command {
    private ControlConnection request_handler;

    public MakeDirectoryCommand(ControlConnection _request_handler) {
        this.request_handler = _request_handler;
    }

    @Override
    public Response execute(Request _request) {
        Response response;
        String directory_name = _request.get_parameters()[0];
        try {
            ControlConnectionListener.get_instance().get_user_base()
                    .get_remote_cli(this.request_handler.current_user.getUsername())
                    .mkdir(directory_name);
            response = new Response(ResponseCode.SUCCESSFUL_MKDIR);
        } catch (DirectoryExistsException exception) {
            ServerLoader.debug(exception);
            response = new Response(ResponseCode.DIRECTORY_ALREADY_EXISTS);
        }
        return response;
    }

}