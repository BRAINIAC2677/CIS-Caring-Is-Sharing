package client;

import java.io.File;
import org.json.simple.JSONObject;
import java.io.FileInputStream;
import java.net.SocketTimeoutException;

import util.*;

class DataConnection implements Runnable {
    private int server_port;
    private int chunksize;
    private int upload_id;
    private String server_ip;
    private File file;
    private NetworkUtil network_util;
    private Thread thread;

    DataConnection(JSONObject _response_body, File _file) {
        this.extract_metadata(_response_body);
        this.file = _file;
        try {
            this.network_util = new NetworkUtil(this.server_ip, this.server_port);
        } catch (Exception exception) {
            ClientLoader.debug(exception);
        }
        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        try {
            Response response = this.get_response(new Request("upid").add_obj("upload_id", this.upload_id));
            if (response.get_code() != ResponseCode.SUCCESSFUL_OPERATION) {
                return;
            }
            if (!this.send_chunks()) {
                LoggedinSession.out_error_msg("unsuccessful upload.");
                return;
            }
            response = this.get_response(new Request("upcomp"));
            if (response.get_code() == ResponseCode.SUCCESSFUL_UPLOAD) {
                LoggedinSession.out_success_msg("successful upload.");
            } else {
                LoggedinSession.out_error_msg("unsuccessful upload.");
            }
        } catch (Exception exception) {
            ClientLoader.debug(exception);
        }

    }

    void extract_metadata(JSONObject _response_body) {
        this.server_port = (Integer) _response_body.get("server_port");
        this.chunksize = (Integer) _response_body.get("chunksize");
        this.upload_id = (Integer) _response_body.get("upload_id");
        this.server_ip = (String) _response_body.get("server_ip");
    }

    boolean send_chunks() {
        int remaining_filesize = (int) this.file.length();
        try {
            FileInputStream fis = new FileInputStream(this.file);
            while (remaining_filesize > 0) {
                byte[] chunk = new byte[Math.min(remaining_filesize, this.chunksize)];
                fis.read(chunk);
                remaining_filesize -= chunk.length;
                Request request = (new Request("updata")).add_obj("chunk", chunk);
                Response response = this.get_response(request);
                if (response.get_code() != ResponseCode.SUCCESSFUL_OPERATION) {
                    fis.close();
                    return false;
                }
            }
            fis.close();
        } catch (Exception exception) {
            ClientLoader.debug(exception);
            return false;
        }
        return true;
    }

    Response get_response(Request _request) {
        Response response;
        while (true) {
            try {
                this.network_util.write(_request);
                response = (Response) this.network_util.read();
                break;
            } catch (Exception exception) {
                // Client.debug(exception);
                if (exception instanceof SocketTimeoutException) {
                    while (true) {
                        try {
                            this.network_util.write(new Request("timeout"));
                            System.exit(0);
                        } catch (Exception exception2) {
                            ClientLoader.debug(exception2);
                        }
                    }
                }
            }
        }
        return response;
    }

}