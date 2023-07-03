package server;

import java.util.ArrayList;

import util.*;

class ListPublicFileCommand implements Command {

    @Override
    public Response execute(Request _request) {
        ArrayList<PublicFile> public_files = ControlConnectionListener.get_instance().get_public_files();
        Response response = (new Response(ResponseCode.SUCCESSFUL_LIST_PUBLIC_FILE)).add_obj("public_file_list",
                public_files);
        return response;
    }

}