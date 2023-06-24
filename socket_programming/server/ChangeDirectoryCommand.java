package server;

import util.*;
import exception.*;

class ChangeDirectoryCommand implements Command {
    private RequestHandler request_handler;

    public ChangeDirectoryCommand(RequestHandler _request_handler) {
        this.request_handler = _request_handler;
    }

    @Override
    public Response execute(Request _request) {
        Response response;
        String directory_name = _request.getParameters()[0];
        try {
            Server.get_instance().user_base.get_remote_cli(this.request_handler.current_user.getUsername())
                    .cd(directory_name);
            response = (new Response(205)).add_user(this.request_handler.current_user);
        } catch (Exception exception) {
            exception.printStackTrace();
            if (exception instanceof DirectoryDoesNotExistException) {
                response = new Response(509);
            } else {
                response = new Response(510);
            }
        }
        return response;
    }

}