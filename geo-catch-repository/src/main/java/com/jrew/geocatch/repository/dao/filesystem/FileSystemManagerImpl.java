package com.jrew.geocatch.repository.dao.filesystem;

import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.service.generator.FileNameGenerator;
import com.jrew.geocatch.repository.service.thumbnail.ThumbnailFactory;
import com.jrew.geocatch.repository.util.FolderUtils;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 7/31/13
 * Time: 9:48 PM
 *
 * Implements saving of image file to file system functionality.
 */
public class FileSystemManagerImpl implements FileSystemManager {

    /** Used for image folder resolving **/
    private FolderLocator folderLocator;

    /** Used for file name generation **/
    private FileNameGenerator fileNameGenerator;

    /** Used for images thumbnails creation **/
    private ThumbnailFactory thumbnailFactory;

    /** Relative to application path **/
    @Value("#{configProperties['fileSystemManager.rootRelativePath']}")
    private String rootRelativePath;

    /**  **/
    @Value("#{configProperties['imageFileExtension']}")
    private String fileExtension;

    @Override
    public void saveImage(Image image) throws IOException, IllegalArgumentException {

        // 1) Save image
        String imageFolderPath = folderLocator.getFolderAbsolutePath(image.getLatitude(), image.getLongitude());
        FolderUtils.checkOrCreateFoldersStructure(imageFolderPath);

        String imageAbsolutePath = imageFolderPath  + File.separator + fileNameGenerator.generate(image);
        writeImageToFolder(image, imageAbsolutePath);
        // Set to image relative to image file path
        image.setPath(getImageRelativePath(imageAbsolutePath));

        // 2) Create thumbnail for original image
        String thumbnailAbsolutePath = thumbnailFactory.createThumbnail(imageAbsolutePath);
        // Set to image relative to thumbnail image file path
        image.setThumbnailPath(getImageRelativePath(thumbnailAbsolutePath));

        return;
    }

    @Override
    public void updateThumbnailPath(List<Image> images) {
        for (Image image : images) {
            String thumbnailFullPath = rootRelativePath + image.getThumbnailPath();
            image.setThumbnailPath(thumbnailFullPath);
        }
    }

    @Override
    public void updatePath(Image image) {
        String imageFullPath = rootRelativePath + image.getPath();
        image.setPath(imageFullPath);
    }

    /**
     * Writes provided image to folder in case if file doesn't exist.
     *
     *
     * @param image
     * @param fullImagePath
     * @throws IOException
     */
    private void writeImageToFolder(Image image, String fullImagePath) throws IOException {

        byte[] inputFile  = image.getFile().getBytes();
        if (inputFile == null || inputFile.length == 0) {
            throw new IllegalArgumentException("Uploaded image file is empty.");
        }

        if (Base64.isArrayByteBase64(inputFile)) {
            inputFile = Base64.decodeBase64(inputFile);
        }

        BufferedImage src = ImageIO.read(new ByteArrayInputStream(inputFile));
        File destination = new File(fullImagePath);
        if (!destination.exists()) {
            ImageIO.write(src, fileExtension, destination);
        }
    }

    @Override
    public void deleteImage(Image image) {

        // Get absolute path images prefix
        String pathPrefix = folderLocator.getRootFolderPath();

        // Delete image
        String imageFilePath = pathPrefix + image.getPath();
        File imageFile = new File(imageFilePath);
        if (imageFile.exists()) {
            imageFile.delete();
        }

        // Delete image thumbnail
        String imageThumbnailFilePath = pathPrefix + image.getThumbnailPath();
        File imageThumbnailFile = new File(imageThumbnailFilePath);
        if (imageThumbnailFile.exists()) {
            imageThumbnailFile.delete();
        }
    }

    /**
     *
     * @param fullAbsolutePath
     * @return
     */
    private String getImageRelativePath(String fullAbsolutePath) {
        return folderLocator.getRelativeToRootPath(fullAbsolutePath);
    }

    /**
     *
     * @param folderLocator
     */
    public void setFolderLocator(FolderLocator folderLocator) {
        this.folderLocator = folderLocator;
    }

    /**
     *
     * @param fileNameGenerator
     */
    public void setFileNameGenerator(FileNameGenerator fileNameGenerator) {
        this.fileNameGenerator = fileNameGenerator;
    }

    /**
     *
     * @param thumbnailFactory
     */
    public void setThumbnailFactory(ThumbnailFactory thumbnailFactory) {
        this.thumbnailFactory = thumbnailFactory;
    }
}
