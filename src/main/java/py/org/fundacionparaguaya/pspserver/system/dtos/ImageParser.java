package py.org.fundacionparaguaya.pspserver.system.dtos;

import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import py.org.fundacionparaguaya.pspserver.common.exceptions.InternalServerErrorException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageParser {

    private static final Logger LOG = LoggerFactory.getLogger(ImageParser.class);

    private ImageParser() {}

    public static ImageDTO parse(String fileString, String imageDirectory) {
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

                File file;
                try {
                    file = File.createTempFile("file", ".tmp");
                    Files.write(fileBytes, file);
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                    throw new InternalServerErrorException(e);
                }

                image = new ImageDTO();
                image.setFile(file);
                image.setFormat(format);
                image.setImageDirectory(imageDirectory);
            }
        }

        return image;
    }

    public static ImageDTO parse(MultipartFile multipartFile, String imageDirectory)
            throws IOException {

        File file = File.createTempFile("file", ".tmp");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }

        String fileFormat = multipartFile.getContentType();
        ImageDTO image  = new ImageDTO();
        image.setFile(file);
        image.setFormat(fileFormat.substring(fileFormat.indexOf('/')+1, fileFormat.length()));
        image.setImageDirectory(imageDirectory);

        return  image;
    }
}