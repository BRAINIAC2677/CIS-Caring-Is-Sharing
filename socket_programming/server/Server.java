package server;

import java.util.Random;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import util.*;

class Server {
    private int current_buffersize;
    private int max_buffersize;
    private int min_chunksize;
    private int max_chunksize;
    private ServerSocket server_socket;
    private UserBase user_base;
    private static Server instance;

    private Server(int _max_buffersize, int _min_chunksize, int _max_chunksize) {
        try {
            this.current_buffersize = 0;
            this.max_buffersize = _max_buffersize;
            this.min_chunksize = _min_chunksize;
            this.max_chunksize = _max_chunksize;
            this.server_socket = new ServerSocket(33333);
            this.user_base = new UserBase();
            instance = this;
            while (true) {
                Socket client_socket = this.server_socket.accept();
                this.serve(client_socket);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    static Server get_instance() {
        return instance;
    }

    void serve(Socket _client_socket) throws IOException {
        NetworkUtil network_util = new NetworkUtil(_client_socket);
        new RequestHandler(network_util);
    }

    UserBase get_user_base() {
        return this.user_base;
    }

    int allocate_buffer(int _fileSize) {
        if (this.current_buffersize + _fileSize <= this.max_buffersize) {
            this.current_buffersize += _fileSize;
            return _fileSize;
        }
        return -1;
    }

    int release_buffer(int _fileSize) {
        if (this.current_buffersize >= _fileSize) {
            this.current_buffersize -= _fileSize;
            return _fileSize;
        }
        return -1;
    }

    int get_random_chunksize() {
        Random random = new Random();
        return min_chunksize + random.nextInt(max_chunksize - min_chunksize + 1);
    }

    public static void main(String args[]) {
        int max_buffersize = 2000000000;
        int min_chunksize = 1000000000;
        int max_chunksize = 2000000000;
        if (instance == null) {
            new Server(max_buffersize, min_chunksize, max_chunksize);
        }
    }
}
