package server;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

import exception.DirectoryDoesNotExistException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import util.*;

class Server {
    private int current_buffersize;
    private int max_buffersize;
    private int min_chunksize;
    private int max_chunksize;
    private HashMap<Integer, PublicFile> public_files;
    private HashMap<Integer, FileRequest> file_requests;
    private ServerSocket server_socket;
    private UserBase user_base;
    private static Server instance;

    private Server(int _max_buffersize, int _min_chunksize, int _max_chunksize) {
        try {
            this.current_buffersize = 0;
            this.max_buffersize = _max_buffersize;
            this.min_chunksize = _min_chunksize;
            this.max_chunksize = _max_chunksize;
            this.public_files = new HashMap<Integer, PublicFile>();
            this.file_requests = new HashMap<Integer, FileRequest>();
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

    void add_file_request(FileRequest _file_request) {
        this.file_requests.put(_file_request.get_id(), _file_request);
    }

    FileRequest get_file_request(int _id) throws Exception {
        if (this.file_requests.containsKey(_id)) {
            return this.file_requests.get(_id);
        }
        throw new Exception("no file request with this id.");
    }

    void add_public_file(PublicFile _public_file) {
        this.public_files.put(_public_file.get_fileid(), _public_file);
    }

    void remove_public_file(int _fileid) {
        if (this.public_files.containsKey(_fileid)) {
            this.public_files.remove(_fileid);
        }
    }

    PublicFile get_public_file(int _fileid) throws DirectoryDoesNotExistException {
        if (this.public_files.containsKey(_fileid)) {
            System.out.println("gotch");
            return this.public_files.get(_fileid);
        } else {
            throw new DirectoryDoesNotExistException(Integer.toString(_fileid));
        }
    }

    ArrayList<PublicFile> get_public_files() {
        ArrayList<PublicFile> public_files_list = new ArrayList<PublicFile>();
        for (int fileid : this.public_files.keySet()) {
            public_files_list.add(this.public_files.get(fileid));
        }
        return public_files_list;
    }

    void serve(Socket _client_socket) throws IOException {
        NetworkUtil network_util = new NetworkUtil(_client_socket);
        new RequestHandler(network_util);
    }

    void broadcast_file_request(FileRequest _file_request) {

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
