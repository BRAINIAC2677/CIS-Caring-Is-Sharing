package server;

import util.*;

class UploadMetadata {
    private boolean is_public;
    private int upload_id;
    private int filesize;
    private String filename;
    private String owner_username;
    private FileRequest file_request;

    UploadMetadata(String _filename) {
        this.filename = _filename;
        this.file_request = null;
        this.upload_id = this.generate_upload_id();
    }

    private int generate_upload_id() {
        Long epoch_time = System.currentTimeMillis();
        return (Long.toString(epoch_time) + this.filename).hashCode();
    }

    UploadMetadata set_is_public(boolean _is_public) {

        this.is_public = _is_public;
        return this;
    }

    UploadMetadata set_file_request(FileRequest _file_request) {
        this.file_request = _file_request;
        return this;
    }

    UploadMetadata set_filesize(int _filesize) {
        this.filesize = _filesize;
        return this;
    }

    UploadMetadata set_owner_username(String _owner_username) {
        this.owner_username = _owner_username;
        return this;
    }

    boolean get_is_public() {
        return this.is_public;
    }

    int get_upload_id() {
        return this.upload_id;
    }

    int get_filesize() {
        return this.filesize;
    }

    String get_filename() {
        return this.filename;
    }

    String get_owner_username() {
        return this.owner_username;
    }

    FileRequest get_file_request() {
        return this.file_request;
    }
}