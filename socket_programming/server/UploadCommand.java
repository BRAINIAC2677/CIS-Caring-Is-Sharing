package server;

import java.util.ArrayList;

import util.*;
import exception.*;

class UploadCommand implements Command {
    private RequestHandler request_handler;

    public UploadCommand(RequestHandler _request_handler) {
        this.request_handler = _request_handler;
    }

    @Override
    public Response execute(Request _request) {
        Response response;
        Request request;
        String fileName = _request.getParameters()[0];
        int fileSize = Integer.parseInt(_request.getParameters()[1]);
        Boolean visibility = (_request.getParameters()[2].equalsIgnoreCase("public") ? true : false);
        if (Server.get_instance().allocateBuffer(fileSize) > 0) {
            int chunkSize = Server.get_instance().getRandomChunkSize();
            response = (new Response(207)).add_obj("chunk_size", chunkSize);
            this.request_handler.send_response(response);
            int receivedFileSize = 0;
            ArrayList<byte[]> receivedContent = new ArrayList<byte[]>();
            try {
                request = this.request_handler.get_request();
                while (request.getVerb().equalsIgnoreCase("updata")) {
                    byte[] chunk = (byte[]) request.getBody().get("chunk");
                    receivedContent.add(chunk);
                    receivedFileSize += chunk.length;
                    response = new Response(207);
                    this.request_handler.send_response(response);
                    request = this.request_handler.get_request();
                }

                if (request.getVerb().equalsIgnoreCase("upcomp")) {
                    if (fileSize == receivedFileSize) {
                        byte[] fileContent = new byte[fileSize];
                        int currentIdx = 0;
                        for (byte[] chunk : receivedContent) {
                            for (int i = 0; i < chunk.length; i++) {
                                fileContent[currentIdx++] = chunk[i];
                            }
                        }

                        Server.get_instance().user_base.get_remote_cli(this.request_handler.current_user.getUsername())
                                .touch(fileName,
                                        fileContent, visibility);
                        response = new Response(208);
                        this.request_handler.send_response(response);
                    } else {
                        response = new Response(514);
                        this.request_handler.send_response(response);
                    }
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                Server.get_instance().releaseBuffer(fileSize);
            }
        } else {
            response = new Response(513);
            this.request_handler.send_response(response);
        }
        return response;
    }

}