package server;

import util.*;

public class LogoutCommand implements Command {
    private ControlConnection request_handler;

    public LogoutCommand(ControlConnection _request_handler) {
        this.request_handler = _request_handler;
    }

    @Override
    public Response execute(Request _request) {
        Response response = new Response(ResponseCode.FAILED_LOGOUT);
        User current_user = this.request_handler.current_user;
        if (current_user != null) {
            try {
                User user = ControlConnectionListener.get_instance().get_user_base()
                        .logout_user(current_user.getUsername());
                this.request_handler.current_user = null;
                response = new Response(ResponseCode.SUCCESSFUL_LOGOUT);

            } catch (Exception exception) {
                ServerLoader.debug(exception);
            }
        }
        return response;
    }

}
