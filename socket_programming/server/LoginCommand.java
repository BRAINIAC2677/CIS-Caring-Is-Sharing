package server;

import util.*;
import exception.*;

class LoginCommand implements Command {
    private RequestHandler request_handler;

    public LoginCommand(RequestHandler _request_handler) {
        this.request_handler = _request_handler;
    }

    @Override
    public Response execute(Request _request) {
        Response response;
        String username = _request.getParameters()[0];
        String password = _request.getParameters()[1];
        try {
            User user = Server.get_instance().get_user_base().login_user(username, password);
            this.request_handler.current_user = user;
            response = (new Response(201)).add_user(user);
        } catch (Exception exception) {
            exception.printStackTrace();
            if (exception instanceof UserNotFoundException) {
                response = new Response(502);
            } else if (exception instanceof UserAlreadyLoggedinException) {
                response = new Response(503);
            } else {
                response = new Response(504);
            }
        }
        return response;
    }
}