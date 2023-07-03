package client;

import java.io.File;
import org.json.simple.JSONObject;
import java.io.FileInputStream;
import java.net.SocketTimeoutException;

import util.*;

class DataConnection implements Runnable {
    private String server_ip;
    private int server_port;
    private int chunksize;
    private int upload_id;
    File file;
    private NetworkUtil network_util;
    private Thread thread;

    DataConnection(JSONObject _response_body, File _file) {
        this.extract_metadata(_response_body);
        System.out.println(this.server_port);
        try {
            this.network_util = new NetworkUtil(this.server_ip, this.server_port);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        this.file = _file;
        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        try {
            Response response = this.get_response(new Request("upid").add_obj("upload_id", this.upload_id));
            if (response.getCode() != ResponseCode.SUCCESSFUL_OPERATION) {
                return;
            }
            if (!this.send_chunks()) {
                // todo: show unsuccessful upload
                return;
            }

            System.out.println("dc: sent all chunks.");

            response = this.get_response(new Request("upcomp"));
            if (response.getCode() == ResponseCode.SUCCESSFUL_UPLOAD) {
                // todo: show successful upload
                System.out.println("dc: successful upload.");
            } else {
                // todo: show unsuccessful upload
                System.out.println("dc: unsuccessful upload.");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    void extract_metadata(JSONObject _response_body) {
        this.server_ip = (String) _response_body.get("server_ip");
        this.server_port = (Integer) _response_body.get("server_port");
        this.upload_id = (Integer) _response_body.get("upload_id");
        this.chunksize = (Integer) _response_body.get("chunksize");
    }

    boolean send_chunks() {
        int remaining_filesize = (int) this.file.length();
        try {
            FileInputStream fis = new FileInputStream(this.file);
            while (remaining_filesize > 0) {
                // System.out.println("dc: rfs: " + remaining_filesize);
                byte[] chunk = new byte[Math.min(remaining_filesize, this.chunksize)];
                fis.read(chunk);
                remaining_filesize -= chunk.length;
                Request request = (new Request("updata")).add_obj("chunk", chunk);
                Response response = this.get_response(request);
                if (response.getCode() != ResponseCode.SUCCESSFUL_OPERATION) {
                    fis.close();
                    return false;
                }
            }
            fis.close();
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
        System.out.println("dc: send chunks complete.");
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
                // exception.printStackTrace();
                if (exception instanceof SocketTimeoutException) {
                    while (true) {
                        try {
                            this.network_util.write(new Request("timeout"));
                            System.out.println("timeout");
                            System.exit(0);
                        } catch (Exception exception2) {
                            exception2.printStackTrace();
                        }
                    }
                }
            }
        }
        return response;
    }

}