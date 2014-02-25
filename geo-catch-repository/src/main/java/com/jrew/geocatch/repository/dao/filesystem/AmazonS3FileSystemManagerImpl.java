package com.jrew.geocatch.repository.dao.filesystem;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.model.Location;
import com.jrew.geocatch.repository.service.generator.FileNameGenerator;
import com.jrew.geocatch.repository.service.thumbnail.ThumbnailFactory;
import com.jrew.geocatch.repository.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 19.02.14
 * Time: 17:54
 * To change this template use File | Settings | File Templates.
 */
public class AmazonS3FileSystemManagerImpl implements FileSystemManager {

    /** One week **/
    private static final long URL_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000;

    /** **/
    private static final String TMP_DIRECTORY_KEY = "java.io.tmpdir";

    /** Used for file name generation **/
    @Autowired
    private FileNameGenerator geoTimeFileNameGenerator;

    /** Used for images thumbnails creation **/
    @Autowired
    private ThumbnailFactory thumbnailFactory;

    /** Used for images thumbnails creation **/
    @Autowired
    private FolderLocator folderLocator;

    /** **/
    @Autowired
    private AmazonS3RegionsConfig amazonS3RegionsConfig;

    @Value("#{configProperties['amazon.s3.accessKey']}")
    private String amazonS3AccessKey;

    @Value("#{configProperties['amazon.s3.secretKey']}")
    private String amazonS3SecretKey;

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

        String bucketName = folderLocator.getFolderName(image.getLatitude(), image.getLongitude());
        checkOrCreateBucket(bucketName, image.getLatitude(), image.getLongitude());

        String imageFileName = geoTimeFileNameGenerator.generate(image);
        File imageFile = FileUtil.createFile(System.getProperty(TMP_DIRECTORY_KEY) + File.separator + imageFileName);

        FileUtil.writeImageToFile(imageFile, image.getFile().getBytes(), fileExtension);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, imageFileName, imageFile);
        putObjectRequest.withCannedAcl(CannedAccessControlList.Private);
        amazonS3.putObject(putObjectRequest);
        // Set to image relative to image file path
        image.setPath(bucketName + File.separator + imageFileName);

        // Create thumbnail for original image
        File thumbnailFile = thumbnailFactory.createThumbnailFile(imageFile);
        String thumbnailFileName = thumbnailFile.getName();
        // Set to image relative to thumbnail image file path
        putObjectRequest = new PutObjectRequest(bucketName, thumbnailFileName, thumbnailFile);
        putObjectRequest.withCannedAcl(CannedAccessControlList.Private);
        amazonS3.putObject(putObjectRequest);
        image.setThumbnailPath(bucketName + File.separator + thumbnailFileName);

        // Delete image files from file system
        FileUtil.deleteFile(imageFile);
        FileUtil.deleteFile(thumbnailFile);
    }

    @Override
    public void deleteImage(Image image) throws IOException {

        String bucketName = folderLocator.getFolderName(image.getLatitude(), image.getLongitude());
        // delete image file
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, image.getPath()));

        // delete image thumbnail file
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, image.getThumbnailPath()));
    }

    @Override
    public void updateThumbnailPath(List<Image> images) {
        for (Image image : images) {
            String thumbnailPath = image.getThumbnailPath();
            image.setThumbnailPath(generatePreSignedUrl(getBucketName(thumbnailPath), getFileName(thumbnailPath)));
        }
    }

    @Override
    public void updatePath(Image image) {
        String imagePath = image.getPath();
        image.setPath(generatePreSignedUrl(getBucketName(imagePath), getFileName(imagePath)));
    }

    /**
     *
     * @param bucketName
     * @param latitude
     * @param longitude
     */
    private void checkOrCreateBucket(String bucketName, double latitude, double longitude) {

        if (!amazonS3.doesBucketExist(bucketName)) {
            Location bucketCenter = folderLocator.getFolderCentralLocation(bucketName);
            Region region = amazonS3RegionsConfig.getRegionForLocation(bucketCenter.getLatitude(), bucketCenter.getLongitude());
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName, region);
            amazonS3.createBucket(createBucketRequest);
        }
    }

    /**
     *
     * @param path
     * @return
     */
    private String getBucketName(String path) {
        int separatorIndex = path.lastIndexOf(File.separator);
        return path.substring(0, separatorIndex);
    }

    /**
     *
     * @param path
     * @return
     */
    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf(File.separator);
        return path.substring(separatorIndex + 1);
    }

    /**
     *
     * @param bucketName
     * @param fileName
     * @return
     */
    private String generatePreSignedUrl(String bucketName, String fileName) {

        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, fileName);
        generatePresignedUrlRequest.setMethod(HttpMethod.GET);

        Date expiration = new java.util.Date();
        long msec = expiration.getTime();
        msec += URL_EXPIRATION_TIME;
        expiration.setTime(msec);
        generatePresignedUrlRequest.setExpiration(expiration);

        URL preSignedUrl = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
        return preSignedUrl.toString();

    }

}
