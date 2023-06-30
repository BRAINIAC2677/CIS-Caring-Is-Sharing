package server;

import java.util.ArrayList;

import util.*;

class ListUnreadMessagesCommand implements Command {
    private RequestHandler request_handler;

    public ListUnreadMessagesCommand(RequestHandler _request_handler) {
        this.request_handler = _request_handler;
    }

    @Override
    public Response execute(Request _request) {
        ArrayList<FileRequest> unread_file_requests = (ArrayList<FileRequest>) this.request_handler.current_user
                .get_unread_file_requests().clone();
        ArrayList<FileResponse> unread_file_responses = (ArrayList<FileResponse>) this.request_handler.current_user
                .get_unread_file_responses().clone();
        this.request_handler.current_user.clear_unread_file_requests();
        this.request_handler.current_user.clear_unread_file_responses();
        Response response = (new Response(ResponseCode.SUCCESSFUL_OPERATION)).add_obj("unread_file_requests",
                unread_file_requests).add_obj("unread_file_responses", unread_file_responses);
        return response;
    }

}