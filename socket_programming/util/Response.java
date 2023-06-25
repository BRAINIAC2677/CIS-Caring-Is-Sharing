package util;

import java.io.Serializable;
import org.json.simple.JSONObject;

public class Response implements Serializable {
    private ResponseCode code;
    private JSONObject body;

    public Response(ResponseCode _code) {
        this.code = _code;
        this.body = new JSONObject();
    }

    public Response(ResponseCode _code, JSONObject _body) {
        this.code = _code;
        this.body = _body;
    }

    public Response setCode(ResponseCode _code) {
        this.code = _code;
        return this;
    }

    public Response setBody(JSONObject _body) {
        this.body = _body;
        return this;
    }

    public ResponseCode getCode() {
        return this.code;
    }

    public JSONObject getBody() {
        return this.body;
    }

    public Response add_user(User _user) {
        this.body.put("user", _user);
        return this;
    }

    public Response add_obj(String _key, Object _obj) {
        this.body.put(_key, _obj);
        return this;
    }

}