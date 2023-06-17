package util;

import java.io.File;

public class FileUtil {

    static public void create_file(String _filepath) throws Exception {
        File file = new File(_filepath);
        try {
            file.createNewFile();
        } catch (Exception exception) {
            throw exception;
        }
    }

    static public void create_directory(String _directory_path) throws Exception {
        File file = new File(_directory_path);
        try {
            file.mkdir();
        } catch (Exception exception) {
            throw exception;
        }
    }

}