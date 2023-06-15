package practise;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

class FilePrac {

    public static void main(String[] args) {
        try {
            File file = new File("socket_programming/storage/asif");
            file.mkdir();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}