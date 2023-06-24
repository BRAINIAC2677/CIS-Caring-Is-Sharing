package server;

import util.*;

class RegistrationCommand implements Command {
    private RequestHandler request_handler;

    public RegistrationCommand(RequestHandler _request_handler) {
        this.request_handler = _request_handler;
    }

    @Override
    public Response execute(Request _request) {
        Response response;
        String username = _request.getParameters()[0];
        String password = _request.getParameters()[1];
        try {
            User user = Server.get_instance().get_user_base().register_user(username, password);
            this.request_handler.current_user = user;
            response = (new Response(200)).add_user(user);
        } catch (Exception exception) {
            exception.printStackTrace();
            response = new Response(501);
        }
        return response;
    }
}