package py.org.fundacionparaguaya.pspserver.system.services;

import py.org.fundacionparaguaya.pspserver.system.dtos.ImageDTO;

public interface ImageUploadService {

    String uploadImage(ImageDTO image);

    void deleteImage(String logoUrl, String imageDirectory);
}