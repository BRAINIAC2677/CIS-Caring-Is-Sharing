package server;

import java.net.SocketException;
import java.util.ArrayList;

import util.*;

class DataConnection implements Runnable {
    private UploadMetadata upload_metadata;
    private Thread thread;
    private NetworkUtil network_util;
    private Request request;

    DataConnection(NetworkUtil _network_util) {
        this.upload_metadata = null;
        this.network_util = _network_util;
        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        try {
            this.request = this.get_request();
            if (this.request.get_verb().equalsIgnoreCase("upid")) {
                int upload_id = (Integer) this.request.get_body().get("upload_id");
                try {
                    this.upload_metadata = ControlConnectionListener.get_instance().get_upload_metadata(upload_id);
                    this.send_response(new Response(ResponseCode.SUCCESSFUL_OPERATION));
                } catch (Exception exception) {
                    ServerLoader.debug(exception);
                    this.send_response(new Response(ResponseCode.FAILED_UPLOAD));
                    return;
                }
            } else {
                this.send_response(new Response(ResponseCode.FAILED_UPLOAD));
                return;
            }
            ArrayList<byte[]> chunks = this.receive_chunks();
            int total_chunksize = this.get_total_chunksize(chunks);
            if (this.upload_metadata.get_filesize() == total_chunksize) {
                byte[] file_content = this.merge_chunks(chunks);
                PublicFile cis_file = ControlConnectionListener.get_instance().get_user_base()
                        .get_remote_cli(this.upload_metadata.get_owner_username())
                        .touch(this.upload_metadata.get_filename(),
                                file_content, this.upload_metadata.get_is_public());
                if (this.upload_metadata.get_file_request() != null) {
                    FileResponse file_response = (new FileResponse())
                            .set_file_request(this.upload_metadata.get_file_request())
                            .set_public_file(cis_file);
                    this.upload_metadata.get_file_request().inc_response_count();
                    ControlConnectionListener.get_instance().get_user_base()
                            .get_user(this.upload_metadata.get_file_request().get_requestee())
                            .add_unread_file_response(file_response);
                }
                this.send_response(new Response(ResponseCode.SUCCESSFUL_UPLOAD));
            } else {
                if (this.upload_metadata != null) {
                    ControlConnectionListener.get_instance()
                            .remove_upload_metadata(this.upload_metadata.get_upload_id());
                }
                this.send_response(new Response(ResponseCode.FAILED_UPLOAD));
            }
        } catch (Exception exception) {
            ServerLoader.debug(exception);
            if (this.upload_metadata != null) {
                ControlConnectionListener.get_instance().remove_upload_metadata(this.upload_metadata.get_upload_id());
            }
            this.send_response(new Response(ResponseCode.FAILED_UPLOAD));
        }

    }

    void send_response(Response _response) {
        try {
            this.network_util.write(_response);
        } catch (Exception exception) {
            ServerLoader.debug(exception);
            if (exception instanceof SocketException) {
                this.cancel_upload();
            }
        }
    }

    Request get_request() {
        try {
            this.request = (Request) this.network_util.read();
        } catch (Exception exception) {
            if (exception instanceof SocketException) {
                this.cancel_upload();
            }
        }
        if (request.get_verb().equalsIgnoreCase("timeout")) {
            this.cancel_upload();
        }
        return this.request;
    }

    void cancel_upload() {
        if (this.upload_metadata != null) {
            ControlConnectionListener.get_instance().remove_upload_metadata(this.upload_metadata.get_upload_id());
        }
        try {
            this.network_util.closeConnection();
        } catch (Exception exception) {
            ServerLoader.debug(exception);
        }
        System.exit(0);
    }

    ArrayList<byte[]> receive_chunks() throws Exception {
        ArrayList<byte[]> chunks = new ArrayList<byte[]>();
        this.request = this.get_request();
        while (this.request.get_verb().equalsIgnoreCase("updata")) {
            byte[] chunk = (byte[]) this.request.get_body().get("chunk");
            chunks.add(chunk);
            this.send_response(new Response(ResponseCode.SUCCESSFUL_OPERATION));
            this.request = this.get_request();
        }
        return chunks;
    }

    byte[] merge_chunks(ArrayList<byte[]> _chunks) {
        int total_chunksize = this.get_total_chunksize(_chunks);
        byte[] merged_chunk = new byte[total_chunksize];
        int current_idx = 0;
        for (byte[] chunk : _chunks) {
            for (int i = 0; i < chunk.length; i++) {
                merged_chunk[current_idx++] = chunk[i];
            }
        }
        return merged_chunk;
    }

    int get_total_chunksize(ArrayList<byte[]> _chunks) {
        int total_chunksize = 0;
        for (byte[] chunk : _chunks) {
            total_chunksize += chunk.length;
        }
        return total_chunksize;
    }

}