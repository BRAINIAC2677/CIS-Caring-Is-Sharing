package server;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;

import exception.DirectoryDoesNotExistException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import util.*;

class ControlConnectionListener implements Runnable {
    private int current_buffersize;
    private int max_buffersize;
    private int min_chunksize;
    private int max_chunksize;
    private HashMap<Integer, PublicFile> public_files;
    private HashMap<Integer, FileRequest> file_requests;
    private HashMap<Integer, UploadMetadata> upload_metadatas;
    private ServerSocket control_socket;
    private UserBase user_base;
    private Thread thread;

    private static ControlConnectionListener instance;

    private ControlConnectionListener() {
        this.current_buffersize = 0;
        this.max_buffersize = ServerLoader.max_buffersize;
        this.min_chunksize = ServerLoader.min_chunksize;
        this.max_chunksize = ServerLoader.max_chunksize;
        this.public_files = new HashMap<Integer, PublicFile>();
        this.file_requests = new HashMap<Integer, FileRequest>();
        this.upload_metadatas = new HashMap<Integer, UploadMetadata>();
        this.user_base = new UserBase();
        try {
            this.control_socket = new ServerSocket(ServerLoader.control_port);
        } catch (Exception exception) {
            ServerLoader.debug(exception);
        }
        this.thread = new Thread(this);
        this.thread.start();
    }

    static ControlConnectionListener get_instance() {
        if (ControlConnectionListener.instance == null) {
            ControlConnectionListener.instance = new ControlConnectionListener();
        }
        return instance;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket client_socket = this.control_socket.accept();
                this.serve(client_socket);
            }
        } catch (Exception exception) {
            ServerLoader.debug(exception);
        }
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
            return this.public_files.get(_fileid);
        } else {
            throw new DirectoryDoesNotExistException(Integer.toString(_fileid));
        }
    }

    void add_upload_metadata(UploadMetadata _upload_metadata) {
        this.upload_metadatas.put(_upload_metadata.get_upload_id(), _upload_metadata);
    }

    void remove_upload_metadata(int _upload_id) {
        if (this.upload_metadatas.containsKey(_upload_id)) {
            this.upload_metadatas.remove(_upload_id);
        }
    }

    UploadMetadata get_upload_metadata(int _upload_id) throws Exception {
        if (this.upload_metadatas.containsKey(_upload_id)) {
            return this.upload_metadatas.get(_upload_id);
        }
        throw new Exception("upload_metadata absent");
    }

    ArrayList<PublicFile> get_public_files() {
        ArrayList<PublicFile> public_files_list = new ArrayList<PublicFile>();
        for (int fileid : this.public_files.keySet()) {
            public_files_list.add(this.public_files.get(fileid));
        }
        return public_files_list;
    }

    ArrayList<FileRequest> get_file_requests() {
        ArrayList<FileRequest> file_request_list = new ArrayList<FileRequest>();
        for (int request_id : this.file_requests.keySet()) {
            file_request_list.add(this.file_requests.get(request_id));
        }
        return file_request_list;
    }

    void serve(Socket _client_socket) throws IOException {
        NetworkUtil network_util = new NetworkUtil(_client_socket);
        new ControlConnection(network_util);
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

}
