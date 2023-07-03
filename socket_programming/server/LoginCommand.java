package server;

import util.*;
import exception.*;

class LoginCommand implements Command {
    private ControlConnection request_handler;

    public LoginCommand(ControlConnection _request_handler) {
        this.request_handler = _request_handler;
    }

    @Override
    public Response execute(Request _request) {
        Response response;
        String username = _request.getParameters()[0];
        String password = _request.getParameters()[1];
        try {
            User user = ControlConnectionListener.get_instance().get_user_base().login_user(username, password);
            this.request_handler.current_user = user;
            response = (new Response(ResponseCode.SUCCESSFUL_LOGIN)).add_user(user);
        } catch (Exception exception) {
            exception.printStackTrace();
            if (exception instanceof UserNotFoundException) {
                response = new Response(ResponseCode.USER_NOT_FOUND);
            } else if (exception instanceof UserAlreadyLoggedinException) {
                response = new Response(ResponseCode.USER_ALREADY_LOGGED_IN);
            } else {
                response = new Response(ResponseCode.INCORRECT_PASSWORD);
            }
        }
        return response;
    }
}