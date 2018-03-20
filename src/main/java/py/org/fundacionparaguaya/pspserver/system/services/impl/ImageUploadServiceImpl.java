package py.org.fundacionparaguaya.pspserver.system.services.impl;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import py.org.fundacionparaguaya.pspserver.common.exceptions.AWSS3RuntimeException;
import py.org.fundacionparaguaya.pspserver.config.ApplicationProperties;
import py.org.fundacionparaguaya.pspserver.system.dtos.ImageDTO;
import py.org.fundacionparaguaya.pspserver.system.services.ImageUploadService;

@Service
public class ImageUploadServiceImpl implements ImageUploadService {

    private static final Logger LOG = LoggerFactory.getLogger(ImageUploadServiceImpl.class);

    private final ApplicationProperties applicationProperties;

    @Autowired
    public ImageUploadServiceImpl(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Override
    public String uploadImage(ImageDTO imageDTO, Long entityId) {

        String url = null;

        if (imageDTO != null) {
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
                String imageNamePrefix = imageDTO.getImageNamePrefix();
                String fileName = imageNamePrefix + entityId + "." + imageDTO.getFormat();
                String keyName = imageDirectory + fileName;

                s3Client.putObject(new PutObjectRequest(bucketName, keyName, imageDTO.getFile())
                                            .withCannedAcl(CannedAccessControlList.PublicRead));

                url = "https://s3-" + s3Client.getRegionName() + ".amazonaws.com/" + bucketName + "/" + keyName;

            } catch (AmazonServiceException ase) {
                LOG.error(ase.getMessage(), ase);
                throw new AWSS3RuntimeException(ase);
            } catch (AmazonClientException ace) {
                LOG.error(ace.getMessage(), ace);
                throw new AWSS3RuntimeException(ace);
            }
        }

        return url;
    }
}