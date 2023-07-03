package server;

import java.util.ArrayList;

import util.*;

class UploadCommand implements Command {
    private ControlConnection request_handler;
    private Request request;
    private Response response;

    public UploadCommand(ControlConnection _request_handler) {
        this.request_handler = _request_handler;
    }

    @Override
    public Response execute(Request _request) {

        System.out.println("upcom exe: start");

        String filename = _request.getParameters()[0];
        int filesize = Integer.parseInt(_request.getParameters()[1]);
        Boolean is_public = (_request.getParameters()[2].equalsIgnoreCase("private") ? false : true);
        UploadMetadata upload_metadata = (new UploadMetadata(filename)).set_filesize(filesize).set_is_public(is_public)
                .set_owner_username(this.request_handler.current_user.getUsername());
        if (this.is_stoi(_request.getParameters()[2])) {
            try {
                int file_request_id = Integer.parseInt(_request.getParameters()[2]);
                FileRequest file_request = ControlConnectionListener.get_instance().get_file_request(file_request_id);
                upload_metadata.set_file_request(file_request);
            } catch (Exception exception) {
                this.response = new Response(ResponseCode.FAILED_UPLOAD);
                return this.response;
            }
        }

        if (ControlConnectionListener.get_instance().allocate_buffer(filesize) > 0) {
            try {
                int chunksize = ControlConnectionListener.get_instance().get_random_chunksize();
                ControlConnectionListener.get_instance().add_upload_metadata(upload_metadata);
                this.response = (new Response(ResponseCode.SUCCESSFUL_BUFFER_ALLOCATION))
                        .add_obj("server_ip", ServerLoader.server_ip).add_obj("server_port", ServerLoader.data_port)
                        .add_obj("upload_id", upload_metadata.get_upload_id()).add_obj("chunksize", chunksize);
                System.out.println("uc: success buffer allocation.");
                // ArrayList<byte[]> chunks = this.receive_chunks();
                // int total_chunksize = this.get_total_chunksize(chunks);
                // if (filesize == total_chunksize) {
                // byte[] file_content = this.merge_chunks(chunks);
                // PublicFile cis_file = Server.get_instance().get_user_base()
                // .get_remote_cli(this.request_handler.current_user.getUsername())
                // .touch(filename,
                // file_content, visibility);
                // if (_request.getParameters().length > 3) {
                // int file_request_id = Integer.parseInt(_request.getParameters()[3]);
                // FileRequest file_request =
                // Server.get_instance().get_file_request(file_request_id);
                // FileResponse file_response = (new
                // FileResponse()).set_file_request(file_request)
                // .set_public_file(cis_file);
                // Server.get_instance().get_user_base().get_user(file_request.get_requestee())
                // .add_unread_file_response(file_response);
                // }
                // this.response = new Response(ResponseCode.SUCCESSFUL_UPLOAD);
                // } else {
                // this.response = new Response(ResponseCode.FAILED_UPLOAD);
                // }
            } catch (Exception exception) {
                exception.printStackTrace();
                this.response = new Response(ResponseCode.FAILED_UPLOAD);
            } finally {
                ControlConnectionListener.get_instance().release_buffer(filesize);
            }
        } else {
            this.response = new Response(ResponseCode.FAILED_BUFFER_ALLOCATION);
        }
        return this.response;
    }

    boolean is_stoi(String _s) {
        try {
            Integer.parseInt(_s);
            return true;
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    ArrayList<byte[]> receive_chunks() throws Exception {
        ArrayList<byte[]> chunks = new ArrayList<byte[]>();
        this.request = this.request_handler.get_request();
        while (this.request.getVerb().equalsIgnoreCase("updata")) {
            byte[] chunk = (byte[]) this.request.getBody().get("chunk");
            chunks.add(chunk);
            this.response = new Response(ResponseCode.SUCCESSFUL_BUFFER_ALLOCATION);
            this.request_handler.send_response(this.response);
            this.request = this.request_handler.get_request();
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