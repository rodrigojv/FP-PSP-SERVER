package py.org.fundacionparaguaya.pspserver.system.dtos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageParser {

    private ImageParser() {}

    public static ImageDTO parse(String fileString, String imageDirectory, String imageNamePrefix) throws IOException {
        ImageDTO image = null;

        // Image format validation (MIME type: image/jpeg, image/png, image/bmp, ...)
        String data = fileString.substring(0, fileString.indexOf(';'));
        String attribute = data.substring(0, data.indexOf(':'));
        if ("data".equals(attribute)) {
            String contentType = data.substring(data.indexOf(':') + 1);
            String type = contentType.substring(0, contentType.indexOf('/'));
            if ("image".equals(type)) {
                String format = contentType.substring(contentType.indexOf('/') + 1);

                String base64 = fileString.substring(fileString.indexOf(',') + 1);
                Base64.Decoder decoder = Base64.getDecoder();
                byte[] fileBytes = decoder.decode(base64);

                File file = File.createTempFile("file", ".tmp");
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(fileBytes);
                fos.close();

                image = new ImageDTO();
                image.setFile(file);
                image.setFormat(format);
                image.setImageDirectory(imageDirectory);
                image.setImageNamePrefix(imageNamePrefix);
            }
        }

        return image;
    }
}