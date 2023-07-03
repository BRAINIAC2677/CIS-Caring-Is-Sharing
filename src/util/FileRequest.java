package util;

import java.io.Serializable;

public class FileRequest implements Serializable {
    private int id;
    private int response_count;
    private String requestee;
    private String description;

    public FileRequest() {
        this.response_count = 0;
    }

    public FileRequest set_id(int _id) {
        this.id = _id;
        return this;
    }

    public FileRequest set_requestee(String _requestee) {
        this.requestee = _requestee;
        return this;
    }

    public FileRequest set_description(String _description) {
        this.description = _description;
        return this;
    }

    public FileRequest inc_response_count() {
        this.response_count++;
        return this;
    }

    public int get_id() {
        return this.id;
    }

    public int get_response_count() {
        return this.response_count;
    }

    public String get_requestee() {
        return this.requestee;
    }

    public String get_description() {
        return this.description;
    }
}