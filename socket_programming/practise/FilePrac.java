package practise;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Scanner;

public class FilePrac {
    public static void main(String[] args) {
        try {
            Path path = Paths.get("socket_programming/storage/a.publ/docs");
            System.out.println(path.subpath(2, path.getNameCount()).toString());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}