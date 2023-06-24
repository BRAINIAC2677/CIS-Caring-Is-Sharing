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
            HashMap<String, Boolean> files = Server.get_instance().user_base
                    .get_remote_cli(this.request_handler.current_user.getUsername())
                    .ls(directory_name);
            response = (new Response(206)).add_user(this.request_handler.current_user).add_obj("file_list", files);
        } catch (Exception exception) {
            exception.printStackTrace();
            if (exception instanceof DirectoryDoesNotExistException) {
                response = new Response(511);
            } else {
                response = new Response(512);
            }
        }
        return response;
    }

}