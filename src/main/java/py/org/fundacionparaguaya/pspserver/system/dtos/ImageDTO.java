package py.org.fundacionparaguaya.pspserver.system.dtos;

import java.io.File;
import java.io.Serializable;

public class ImageDTO implements Serializable {

    private File file;

    private String format;

    private String imageDirectory;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getImageDirectory() {
        return imageDirectory;
    }

    public void setImageDirectory(String imageDirectory) {
        this.imageDirectory = imageDirectory;
    }
}