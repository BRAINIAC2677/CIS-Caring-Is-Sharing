package util;

import java.io.Serializable;

public class FileResponse implements Serializable {
    FileRequest file_request;
    PublicFile public_file;

    public FileResponse set_file_request(FileRequest _file_request) {
        this.file_request = _file_request;
        return this;
    }

    public FileResponse set_public_file(PublicFile _public_file) {
        this.public_file = _public_file;
        return this;
    }

    public FileRequest get_file_request() {
        return this.file_request;
    }

    public PublicFile get_public_file() {
        return this.public_file;
    }
}