package util;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PublicFile implements Serializable {
    private String pathstring;

    public PublicFile(String _pathstring) {
        this.pathstring = _pathstring;
    }

    public static int get_fileid(String _pathstring) {
        return _pathstring.hashCode();
    }

    public PublicFile set_pathstring(String _pathstring) {
        this.pathstring = _pathstring;
        return this;
    }

    public String get_pathstring() {
        return this.pathstring;
    }

    public int get_fileid() {
        return PublicFile.get_fileid(pathstring);
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
        if (path.getNameCount() >= 3) {
            owner_name = path.getName(2).toString();
        }
        owner_name = owner_name.substring(0, owner_name.lastIndexOf("."));
        return owner_name;
    }

}