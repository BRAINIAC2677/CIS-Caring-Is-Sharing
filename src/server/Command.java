package server;

import util.Request;
import util.Response;

public interface Command {
    public Response execute(Request _request);
}