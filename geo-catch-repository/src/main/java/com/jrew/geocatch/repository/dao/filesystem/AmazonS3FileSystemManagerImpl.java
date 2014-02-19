package com.jrew.geocatch.repository.dao.filesystem;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.service.generator.FileNameGenerator;
import com.jrew.geocatch.repository.service.thumbnail.ThumbnailFactory;
import com.jrew.geocatch.repository.util.FileUtil;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 19.02.14
 * Time: 17:54
 * To change this template use File | Settings | File Templates.
 */
public class AmazonS3FileSystemManagerImpl implements FileSystemManager {

    private static final String TMP_DIRECTORY_KEY = "java.io.tmpdir";

    /** Used for file name generation **/
    @Autowired
    private FileNameGenerator geoTimeFileNameGenerator;

    /** Used for images thumbnails creation **/
    @Autowired
    private ThumbnailFactory thumbnailFactory;

    @Value("#{configProperties['amazon.s3.accessKey']}")
    private String amazonS3AccessKey;

    @Value("#{configProperties['amazon.s3.secretKey']}")
    private String amazonS3SecretKey;

    @Value("#{configProperties['amazon.s3.bucket.name']}")
    private String amazonS3BucketName;

    @Value("#{configProperties['amazon.s3.storage.prefix']}")
    private String amazonS3StoragePrefix;

    /**  **/
    @Value("#{configProperties['imageFileExtension']}")
    private String fileExtension;

    /** **/
    private AmazonS3 amazonS3;

    @PostConstruct
    public void init() {
        amazonS3 = new AmazonS3Client(new BasicAWSCredentials(amazonS3AccessKey, amazonS3SecretKey));
    }

    @Override
    public void saveImage(Image image) throws IOException, IllegalArgumentException {

        String imageFileName = geoTimeFileNameGenerator.generate(image);
        File imageFile = FileUtil.createFile(System.getProperty(TMP_DIRECTORY_KEY) + File.separator + imageFileName);

        FileUtil.writeImageToFile(imageFile, image.getFile().getBytes(), fileExtension);

        PutObjectRequest putObjectRequest = new PutObjectRequest(amazonS3BucketName, imageFileName, imageFile);
        putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3.putObject(putObjectRequest);
        // Set to image relative to image file path
        image.setPath(imageFileName);

        // Create thumbnail for original image
        File thumbnailFile = thumbnailFactory.createThumbnailFile(imageFile);
        String thumbnailFileName = thumbnailFile.getName();
        // Set to image relative to thumbnail image file path
        putObjectRequest = new PutObjectRequest(amazonS3BucketName, thumbnailFileName, thumbnailFile);
        putObjectRequest.withCannedAcl(CannedAccessControlList.PublicRead);
        amazonS3.putObject(putObjectRequest);
        image.setThumbnailPath(thumbnailFileName);

        // Delete image files from file system
        FileUtil.deleteFile(imageFile);
        FileUtil.deleteFile(thumbnailFile);
    }

    @Override
    public void deleteImage(Image image) {
        // delete image file
        amazonS3.deleteObject(new DeleteObjectRequest(amazonS3BucketName, image.getPath()));

        // delete image thumbnail file
        amazonS3.deleteObject(new DeleteObjectRequest(amazonS3BucketName, image.getThumbnailPath()));
    }

    @Override
    public void updateThumbnailPath(List<Image> images) {
        for (Image image : images) {
            String thumbnailFileName = image.getThumbnailPath();
            image.setThumbnailPath(amazonS3StoragePrefix + thumbnailFileName);
        }
    }

    @Override
    public void updatePath(Image image) {
        String imageFileName = image.getPath();
        image.setPath(amazonS3StoragePrefix + imageFileName);
    }
}
