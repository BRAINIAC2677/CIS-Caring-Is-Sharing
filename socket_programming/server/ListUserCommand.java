package server;

import java.util.ArrayList;

import util.*;

class ListUserCommand implements Command {

    @Override
    public Response execute(Request _request) {
        ArrayList<String> usernames = new ArrayList<String>();
        if (_request.getVerb().equalsIgnoreCase("lsau")) {
            usernames = Server.get_instance().get_user_base().get_all_usernames();
        } else {
            usernames = Server.get_instance().get_user_base().get_loggedin_usernames();
        }
        Response response = (new Response(202)).add_obj("user_list", usernames);
        return response;
    }

}