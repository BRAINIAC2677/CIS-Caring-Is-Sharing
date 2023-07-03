package server;

import util.*;

class FileRequestCommand implements Command {
    private ControlConnection request_handler;

    public FileRequestCommand(ControlConnection _request_handler) {
        this.request_handler = _request_handler;
    }

    @Override
    public Response execute(Request _request) {
        String description = _request.get_parameters()[0];
        long epoch_time = System.currentTimeMillis();
        int id = (Long.toString(epoch_time) + description).hashCode();
        FileRequest file_request = (new FileRequest()).set_id(id).set_description(description)
                .set_requestee(this.request_handler.current_user.get_username());
        ControlConnectionListener.get_instance().add_file_request(file_request);
        for (User user : ControlConnectionListener.get_instance().get_user_base().get_all_users()) {
            if (!(user.get_username().equalsIgnoreCase(this.request_handler.current_user.get_username()))) {
                user.add_unread_file_requests(file_request);
            }
        }
        for (User user : ControlConnectionListener.get_instance().get_user_base().get_all_users()) {
            if (!(user.get_username().equalsIgnoreCase(this.request_handler.current_user.get_username()))) {
            }
        }
        Response response = new Response(ResponseCode.SUCCESSFUL_OPERATION);
        return response;
    }

}