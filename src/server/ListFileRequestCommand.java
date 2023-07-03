package server;

import java.util.ArrayList;

import util.*;

class ListFileRequestCommand implements Command {

    @Override
    public Response execute(Request _request) {
        ArrayList<FileRequest> file_requests = ControlConnectionListener.get_instance().get_file_requests();
        Response response = (new Response(ResponseCode.SUCCESSFUL_OPERATION)).add_obj("file_request_list",
                file_requests);
        return response;
    }

}