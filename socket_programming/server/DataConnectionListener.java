package server;

import java.net.ServerSocket;
import java.net.Socket;

import util.*;

class DataConnectionListener implements Runnable {
    private Thread thread;
    private ServerSocket data_socket;
    private static DataConnectionListener instance;

    private DataConnectionListener() {
        try {
            this.data_socket = new ServerSocket(ServerLoader.data_port);
            System.out.println("data port listening." + ServerLoader.data_port);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        this.thread = new Thread(this);
        this.thread.start();
    }

    public static DataConnectionListener get_instance() {
        if (DataConnectionListener.instance == null) {
            DataConnectionListener.instance = new DataConnectionListener();
        }
        return DataConnectionListener.instance;
    }

    @Override
    public void run() {
        System.out.println("starting data port listening.");
        while (true) {
            try {
                Socket client_socket = this.data_socket.accept();
                NetworkUtil network_util = new NetworkUtil(client_socket);
                new DataConnection(network_util);
                System.out.println("listened a new data connection.");
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}