package com.jrew.geocatch.repository.dao.filesystem;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.service.generator.FileNameGenerator;
import com.jrew.geocatch.repository.service.thumbnail.ThumbnailFactory;
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

    /** **/
    private AmazonS3 amazonS3;

    @PostConstruct
    public void init() {
        amazonS3 = new AmazonS3Client(new BasicAWSCredentials(amazonS3AccessKey, amazonS3SecretKey));
    }

    @Override
    public void saveImage(Image image) throws IOException, IllegalArgumentException {

        byte[] inputFile  = image.getFile().getBytes();
        if (inputFile == null || inputFile.length == 0) {
            throw new IllegalArgumentException("Uploaded image file is empty.");
        }

        if (Base64.isArrayByteBase64(inputFile)) {
            inputFile = Base64.decodeBase64(inputFile);
        }

        String imageName = geoTimeFileNameGenerator.generate(image);
        String tmpDirectoryPath = System.getProperty("java.io.tmpdir");

        String fullImagePath = tmpDirectoryPath + File.separator + imageName;
        BufferedImage src = ImageIO.read(new ByteArrayInputStream(inputFile));
        File imageFile = new File(fullImagePath);
        if (!imageFile.exists()) {
            ImageIO.write(src, "jpg", imageFile);
        }


        amazonS3.putObject(new PutObjectRequest(amazonS3BucketName, imageName, imageFile));
        // Set to image relative to image file path
        image.setPath(imageName);

        // 2) Create thumbnail for original image
        File thumbnailFile = thumbnailFactory.createThumbnailFile(imageFile);
        String thumbnailName = thumbnailFile.getName();
        // Set to image relative to thumbnail image file path
        image.setThumbnailPath(thumbnailName);
        amazonS3.putObject(new PutObjectRequest(amazonS3BucketName, thumbnailName, thumbnailFile));

        return;
    }

    @Override
    public void deleteImage(Image image) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updateThumbnailPath(List<Image> images) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void updatePath(Image image) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
