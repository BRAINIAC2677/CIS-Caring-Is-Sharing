package practise;

import java.io.File;
import java.util.Scanner;

public class FilePrac {
    public static void main(String[] args) {
        try {
            File file = new File("/home/brainiac77/github/cse322/socket_programming/practise/a");
            file.mkdir();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}