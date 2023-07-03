package server;

import util.*;

class RegistrationCommand implements Command {
    private ControlConnection request_handler;

    public RegistrationCommand(ControlConnection _request_handler) {
        this.request_handler = _request_handler;
    }

    @Override
    public Response execute(Request _request) {
        Response response;
        String username = _request.getParameters()[0];
        String password = _request.getParameters()[1];
        try {
            User user = ControlConnectionListener.get_instance().get_user_base().register_user(username, password);
            this.request_handler.current_user = user;
            response = (new Response(ResponseCode.SUCCESSFUL_REGISTRATION)).add_user(user);
        } catch (Exception exception) {
            exception.printStackTrace();
            response = new Response(ResponseCode.FAILED_REGISTRATION);
        }
        return response;
    }
}