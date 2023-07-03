package server;

import java.nio.file.Paths;

public class ServerLoader {
    public static final String server_ip = "127.0.0.1";
    public static final int control_port = 33333;
    public static final int data_port = 33334;
    public static final int max_buffersize = 1000000000;
    public static final int min_chunksize = 100000;
    public static final int max_chunksize = 200000;
    public static final String storage_directory = "src/storage/"; // relative to project directory
    public static final int root_directory_name_count = Paths.get(storage_directory).getNameCount() + 1;
    public static final boolean is_debug_mode = false;

    public static void debug(Exception _exception) {
        if (ServerLoader.is_debug_mode) {
            _exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ControlConnectionListener.get_instance();
        DataConnectionListener.get_instance();
    }
}