package py.org.fundacionparaguaya.pspserver.system.services.impl;

import com.amazonaws.SdkClientException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import py.org.fundacionparaguaya.pspserver.common.exceptions.AWSS3RuntimeException;
import py.org.fundacionparaguaya.pspserver.config.ApplicationProperties;
import py.org.fundacionparaguaya.pspserver.system.dtos.ImageDTO;
import py.org.fundacionparaguaya.pspserver.system.services.ImageUploadService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {

    private static final Logger LOG = LoggerFactory.getLogger(ImageUploadServiceImpl.class);

    private final ApplicationProperties applicationProperties;

    @Autowired
    public ImageUploadServiceImpl(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public String uploadImage(ImageDTO imageDTO) {
        if (imageDTO == null) {
            return null;
        }

        String url;
        try {
            String strRegion = applicationProperties
                    .getAws()
                    .getStrRegion();
            Regions region = Regions.valueOf(strRegion);

            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(region)
                    .build();

            String bucketName = applicationProperties
                    .getAws()
                    .getBucketName();

            String imageDirectory = imageDTO.getImageDirectory();
            String imageName = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String fileName = imageName + "." + imageDTO.getFormat();
            String keyName = imageDirectory + fileName;

            s3Client.putObject(new PutObjectRequest(bucketName, keyName, imageDTO.getFile())
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            url = "https://s3." + s3Client.getRegionName() + ".amazonaws.com/" + bucketName + "/" + keyName;

        } catch (SdkClientException sdkClientExc) {
            LOG.error(sdkClientExc.getMessage(), sdkClientExc);
            throw new AWSS3RuntimeException(sdkClientExc);
        }

        return url;
    }

    @Override
    public void deleteImage(String logoUrl, String imageDirectory) {
        if (logoUrl == null) {
            return;
        }

        try {
            String strRegion = applicationProperties
                    .getAws()
                    .getStrRegion();
            Regions region = Regions.valueOf(strRegion);

            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(region)
                    .build();

            String bucketName = applicationProperties
                    .getAws()
                    .getBucketName();

            String fileName = logoUrl.substring(logoUrl.lastIndexOf('/') + 1);
            String keyName = imageDirectory + fileName;

            s3Client.deleteObject(new DeleteObjectRequest(bucketName, keyName));

        } catch (SdkClientException sdkClientExc) {
            LOG.error(sdkClientExc.getMessage(), sdkClientExc);
            throw new AWSS3RuntimeException(sdkClientExc);
        }
    }
}