package util;

import java.io.Serializable;

public class FileRequest implements Serializable {
    private int id;
    private boolean is_responded;
    private String requestee;
    private String description;

    public FileRequest set_id(int _id) {
        this.id = _id;
        return this;
    }

    public FileRequest set_is_responded(boolean _is_responded) {
        this.is_responded = _is_responded;
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

    public int get_id() {
        return this.id;
    }

    public boolean get_is_responded() {
        return this.is_responded;
    }

    public String get_requestee() {
        return this.requestee;
    }

    public String get_description() {
        return this.description;
    }
}