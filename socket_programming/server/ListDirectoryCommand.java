package server;

import java.util.HashMap;

import util.*;
import exception.*;

class ListDirectoryCommand implements Command {
    private RequestHandler request_handler;

    public ListDirectoryCommand(RequestHandler _request_handler) {
        this.request_handler = _request_handler;
    }

    @Override
    public Response execute(Request _request) {
        Response response;
        String directory_name = _request.getParameters()[0];
        try {
            HashMap<String, Boolean> files = Server.get_instance().get_user_base()
                    .get_remote_cli(this.request_handler.current_user.getUsername())
                    .ls(directory_name);
            response = (new Response(ResponseCode.SUCCESSFUL_LS)).add_user(this.request_handler.current_user)
                    .add_obj("file_list", files);
        } catch (Exception exception) {
            exception.printStackTrace();
            if (exception instanceof DirectoryDoesNotExistException) {
                response = new Response(ResponseCode.DIRECTORY_DOES_NOT_EXIST);
            } else {
                response = new Response(ResponseCode.NOT_A_DIRECTORY);
            }
        }
        return response;
    }

}