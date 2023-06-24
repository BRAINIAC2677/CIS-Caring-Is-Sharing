package server;

import util.*;
import exception.*;

class MakeDirectoryCommand implements Command {
    private RequestHandler request_handler;

    public MakeDirectoryCommand(RequestHandler _request_handler) {
        this.request_handler = _request_handler;
    }

    @Override
    public Response execute(Request _request) {
        Response response;
        String directory_name = _request.getParameters()[0];
        try {
            Server.get_instance().user_base.get_remote_cli(this.request_handler.current_user.getUsername())
                    .mkdir(directory_name);
            response = new Response(203);
        } catch (DirectoryExistsException exception) {
            exception.printStackTrace();
            response = new Response(506);
        }
        return response;
    }

}