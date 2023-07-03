package client;

import util.NetworkUtil;

public class ClientLoader {
    public static final String server_address = "127.0.0.1";
    public static final int server_port = 33333;
    public static final boolean is_debug_mode = false;
    public static final String RED_ANSI = "\u001B[31m";
    public static final String GREEN_ANSI = "\u001B[32m";
    public static final String BLUE_ANSI = "\u001B[34m";
    public static final String YELLOW_ANSI = "\u001B[33m";
    public static final String RESET_ANSI = "\u001B[0m";

    public static void debug(Exception _exception) {
        if (ClientLoader.is_debug_mode) {
            ClientLoader.debug(_exception);
        }
    }

    public static void main(String args[]) {
        try {
            NetworkUtil network_util = new NetworkUtil(ClientLoader.server_address, ClientLoader.server_port);
            new ControlConnection(network_util);
        } catch (Exception exception) {
            ClientLoader.debug(exception);
        }
    }
}
