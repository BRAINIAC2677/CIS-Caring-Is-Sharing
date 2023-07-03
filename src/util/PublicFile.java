package util;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PublicFile implements Serializable {
    private String pathstring;
    private int root_directory_name_count;

    public PublicFile(String _pathstring, int _root_directory_name_count) {
        this.pathstring = _pathstring;
        this.root_directory_name_count = _root_directory_name_count;
    }

    public PublicFile set_pathstring(String _pathstring) {
        this.pathstring = _pathstring;
        return this;
    }

    public int get_fileid() {
        return PublicFile.get_fileid(pathstring);
    }

    public static int get_fileid(String _pathstring) {
        return _pathstring.hashCode();
    }

    public String get_pathstring() {
        return this.pathstring;
    }

    public String get_filename() {
        Path path = Paths.get(this.pathstring);
        String filename = path.getFileName().toString();
        filename = filename.substring(0, filename.lastIndexOf("."));
        return filename;
    }

    public String get_owner_name() {
        Path path = Paths.get(this.pathstring);
        String owner_name = "";
        if (path.getNameCount() >= this.root_directory_name_count) {
            owner_name = path.getName(this.root_directory_name_count - 1).toString();
        }
        owner_name = owner_name.substring(0, owner_name.lastIndexOf("."));
        return owner_name;
    }
}