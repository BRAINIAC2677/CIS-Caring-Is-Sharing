package util;

import java.io.Serializable;
import org.json.simple.JSONObject;

public class Request implements Serializable {
    private String verb;
    private String[] parameters;
    private JSONObject body;

    public Request(String _verb) {
        this.verb = _verb;
        this.body = new JSONObject();
    }

    public Request(String _verb, String[] _parameters) {
        this.verb = _verb;
        this.parameters = _parameters;
        this.body = new JSONObject();
    }

    public Request(String _verb, String[] _parameters, JSONObject _body) {
        this.verb = _verb;
        this.parameters = _parameters;
        this.body = _body;
    }

    public Request set_verb(String _verb) {
        this.verb = _verb;
        return this;
    }

    public Request set_parameters(String[] _parameters) {
        this.parameters = _parameters;
        return this;
    }

    public Request set_body(JSONObject _body) {
        this.body = _body;
        return this;
    }

    public String get_verb() {
        return this.verb;
    }

    public String[] get_parameters() {
        return this.parameters;
    }

    public JSONObject get_body() {
        return this.body;
    }

    public Request add_obj(String _key, Object _obj) {
        this.body.put(_key, _obj);
        return this;
    }
}