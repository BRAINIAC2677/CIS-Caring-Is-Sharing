package util;

import java.io.Serializable;
import org.json.simple.JSONObject;

public class Response implements Serializable {
    private int code;
    private JSONObject body;

    public Response(int _code) {
        this.code = _code;
    }

    public Response(int _code, JSONObject _body) {
        this.code = _code;
        this.body = _body;
    }

    public Response setCode(int _code) {
        this.code = _code;
        return this;
    }

    public Response setBody(JSONObject _body) {
        this.body = _body;
        return this;
    }

    public int getCode() {
        return this.code;
    }

    public JSONObject getBody() {
        return this.body;
    }

}