package client;

import util.NetworkUtil;

public class Client {

    public Client(String serverAddress, int serverPort) {
        try {
            NetworkUtil network_util = new NetworkUtil(serverAddress, serverPort);
            new ControlConnection(network_util);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String args[]) {
        String serverAddress = "127.0.0.1";
        int serverPort = 33333;
        Client client = new Client(serverAddress, serverPort);
    }
}
