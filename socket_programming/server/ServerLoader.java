package server;

class ServerLoader {
    public static final String server_ip = "127.0.0.1";
    public static final int control_port = 33333;
    public static final int data_port = 33334;
    public static final int max_buffersize = 1000000000;
    public static final int min_chunksize = 100;
    public static final int max_chunksize = 200;

    public static void main(String[] args) {
        ControlConnectionListener.get_instance();
        DataConnectionListener.get_instance();
    }
}