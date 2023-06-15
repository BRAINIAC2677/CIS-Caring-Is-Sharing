package server;

import org.json.simple.JSONObject;

import util.*;

class RequestHandler implements Runnable {
    private Server server;
    private NetworkUtil networkUtil;
    private Thread thread;

    RequestHandler(Server _server, NetworkUtil _networkUtil) {
        this.server = _server;
        this.networkUtil = _networkUtil;
        this.thread = new Thread(this);
        this.thread.start();
    }

    void handleRegistration(Request _request) {
        Response response;
        try {
            String username = _request.getParameters()[0];
            String password = _request.getParameters()[1];
            User user = this.server.registerNewUser(username, password);
            JSONObject body = new JSONObject();
            body.put("user", user);
            response = new Response(200, body);
        } catch (Exception exception) {
            exception.printStackTrace();
            response = new Response(501);
        }

        try {
            this.networkUtil.write(response);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            while (true) {
                Request request = (Request) this.networkUtil.read();
                String requestVerb = request.getVerb();

                if (requestVerb.equalsIgnoreCase("regi")) {
                    this.handleRegistration(request);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                this.networkUtil.closeConnection();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}