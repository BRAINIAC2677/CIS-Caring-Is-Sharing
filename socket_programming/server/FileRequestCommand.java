package server;

import util.*;
import exception.*;

class FileRequestCommand implements Command {
    private RequestHandler request_handler;

    public FileRequestCommand(RequestHandler _request_handler) {
        this.request_handler = _request_handler;
    }

    @Override
    public Response execute(Request _request) {
        String description = _request.getParameters()[0];
        long epoch_time = System.currentTimeMillis();
        int id = (Long.toString(epoch_time) + description).hashCode();
        FileRequest file_request = (new FileRequest()).set_id(id).set_is_responded(false).set_description(description)
                .set_requestee(this.request_handler.current_user.getUsername());
        Server.get_instance().add_file_request(file_request);
        for (User user : Server.get_instance().get_user_base().get_all_users()) {
            if (!(user.getUsername().equalsIgnoreCase(this.request_handler.current_user.getUsername()))) {
                user.add_unread_file_requests(file_request);
                System.out.println("inside: " + user.get_unread_file_requests().size());
            }
        }
        for (User user : Server.get_instance().get_user_base().get_all_users()) {
            if (!(user.getUsername().equalsIgnoreCase(this.request_handler.current_user.getUsername()))) {
                System.out.println("outside: " + user.get_unread_file_requests().size());
            }
        }
        Response response = new Response(ResponseCode.SUCCESSFUL_OPERATION);
        return response;
    }

}