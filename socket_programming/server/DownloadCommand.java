package server;

import java.util.ArrayList;

import util.*;
import exception.*;

class DownloadCommand implements Command {
    private ControlConnection request_handler;

    public DownloadCommand(ControlConnection _request_handler) {
        this.request_handler = _request_handler;
    }

    @Override
    public Response execute(Request _request) {
        Response response;
        boolean is_owner = (_request.get_parameters()[0].equalsIgnoreCase("-o") ? true : false);
        String relative_pathstring = _request.get_parameters()[1];
        try {
            byte[] filecontent;
            if (is_owner) {
                filecontent = ControlConnectionListener.get_instance().get_user_base()
                        .get_remote_cli(this.request_handler.current_user.getUsername())
                        .get_filecontent(relative_pathstring);

            } else {
                int fileid = Integer.parseInt(relative_pathstring);
                filecontent = ControlConnectionListener.get_instance().get_user_base()
                        .get_remote_cli(this.request_handler.current_user.getUsername())
                        .get_filecontent(fileid);
            }
            response = (new Response(ResponseCode.SUCCESSFUL_DOWNLOAD)).add_obj("filecontent", filecontent);

        } catch (Exception exception) {
            ServerLoader.debug(exception);
            if (exception instanceof DirectoryDoesNotExistException) {
                response = new Response(ResponseCode.DIRECTORY_DOES_NOT_EXIST);
            } else {
                response = new Response(ResponseCode.FAILED_DOWNLOAD);
            }
        }
        return response;
    }

}