package server;

import java.net.ServerSocket;
import java.net.Socket;

import util.*;

class DataConnectionListener implements Runnable {
    private Thread thread;
    private ServerSocket data_socket;
    private static DataConnectionListener instance;

    private DataConnectionListener() {
        this.thread = new Thread(this);
        try {
            this.data_socket = new ServerSocket(ServerLoader.data_port);
        } catch (Exception exception) {
            ServerLoader.debug(exception);
        }
        this.thread.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket client_socket = this.data_socket.accept();
                NetworkUtil network_util = new NetworkUtil(client_socket);
                new DataConnection(network_util);
            } catch (Exception exception) {
                ServerLoader.debug(exception);
            }
        }
    }

    public static DataConnectionListener get_instance() {
        if (DataConnectionListener.instance == null) {
            DataConnectionListener.instance = new DataConnectionListener();
        }
        return DataConnectionListener.instance;
    }

}