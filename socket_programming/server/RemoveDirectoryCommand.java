package server;

import util.*;
import exception.*;

class RemoveDirectoryCommand implements Command {
    private RequestHandler request_handler;

    public RemoveDirectoryCommand(RequestHandler _request_handler) {
        this.request_handler = _request_handler;
    }

    @Override
    public Response execute(Request _request) {
        Response response;
        String directory_name = _request.getParameters()[0];
        try {
            Server.get_instance().get_user_base().get_remote_cli(this.request_handler.current_user.getUsername())
                    .rmdir(directory_name);
            response = new Response(ResponseCode.SUCCESSFUL_RMDIR);
        } catch (Exception exception) {
            exception.printStackTrace();
            if (exception instanceof DirectoryDoesNotExistException) {
                response = new Response(ResponseCode.DIRECTORY_DOES_NOT_EXIST);
            } else {
                response = new Response(ResponseCode.DIRECTORY_NOT_EMPTY);
            }
        }
        return response;
    }

}