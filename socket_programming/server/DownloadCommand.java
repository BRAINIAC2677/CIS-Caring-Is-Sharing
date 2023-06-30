package server;

import java.util.ArrayList;

import util.*;
import exception.*;

class DownloadCommand implements Command {
    private RequestHandler request_handler;

    public DownloadCommand(RequestHandler _request_handler) {
        this.request_handler = _request_handler;
    }

    @Override
    public Response execute(Request _request) {
        Response response;
        boolean is_owner = (_request.getParameters()[0].equalsIgnoreCase("-o") ? true : false);
        String relative_pathstring = _request.getParameters()[1];
        try {
            byte[] filecontent;
            if (is_owner) {
                filecontent = Server.get_instance().get_user_base()
                        .get_remote_cli(this.request_handler.current_user.getUsername())
                        .get_filecontent(relative_pathstring);

            } else {
                int fileid = Integer.parseInt(relative_pathstring);
                filecontent = Server.get_instance().get_user_base()
                        .get_remote_cli(this.request_handler.current_user.getUsername())
                        .get_filecontent(fileid);
            }
            response = (new Response(ResponseCode.SUCCESSFUL_DOWNLOAD)).add_obj("filecontent", filecontent);

        } catch (Exception exception) {
            exception.printStackTrace();
            if (exception instanceof DirectoryDoesNotExistException) {
                response = new Response(ResponseCode.DIRECTORY_DOES_NOT_EXIST);
            } else {
                response = new Response(ResponseCode.FAILED_DOWNLOAD);
            }
        }
        return response;
    }

}