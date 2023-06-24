package server;

import java.util.Random;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import util.*;

class Server {
    private int currentBufferSize;
    private int maxBufferSize;
    private int minChunkSize;
    private int maxChunkSize;
    private ServerSocket serverSocket;
    public UserBase user_base;

    private static Server instance;

    private Server(int _maxBufferSize, int _minChunkSize, int _maxChunkSize) {
        try {
            this.currentBufferSize = 0;
            this.maxBufferSize = _maxBufferSize;
            this.minChunkSize = _minChunkSize;
            this.maxChunkSize = _maxChunkSize;
            this.serverSocket = new ServerSocket(33333);
            this.user_base = new UserBase();
            instance = this;

            while (true) {
                Socket clientSocket = this.serverSocket.accept();
                this.serve(clientSocket);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static Server get_instance() {
        return instance;
    }

    public void serve(Socket _clientSocket) throws IOException {
        NetworkUtil networkUtil = new NetworkUtil(_clientSocket);
        new RequestHandler(networkUtil);
    }

    public UserBase get_user_base() {
        return this.user_base;
    }

    String getUserRootDir(User _user) {
        return "socket_programming/storage/" + _user.getUsername();
    }

    int allocateBuffer(int _fileSize) {
        if (this.currentBufferSize + _fileSize <= this.maxBufferSize) {
            this.currentBufferSize += _fileSize;
            return _fileSize;
        }
        return -1;
    }

    int releaseBuffer(int _fileSize) {
        if (this.currentBufferSize >= _fileSize) {
            this.currentBufferSize -= _fileSize;
            return _fileSize;
        }
        return -1;
    }

    int getRandomChunkSize() {
        Random random = new Random();
        return minChunkSize + random.nextInt(maxChunkSize - minChunkSize + 1);
    }

    public static void main(String args[]) {
        int maxBufferSize = 2000000000;
        int minChunkSize = 1000000000;
        int maxChunkSize = 2000000000;
        if (instance == null) {
            System.out.println("hello");
            new Server(maxBufferSize, minChunkSize, maxChunkSize);
        }
    }
}
