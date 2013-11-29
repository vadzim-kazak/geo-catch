package com.jrew.geocatch.repository.service;

import com.jrew.geocatch.repository.dao.database.ImageDBManager;
import com.jrew.geocatch.repository.dao.filesystem.FileSystemManager;
import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.model.ViewBounds;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Implementation for {@link ImageService} interface
 */
public class ImageServiceImpl implements ImageService {

    /** **/
    private final FileSystemManager fileSystemManager;

    /** **/
    private final ImageDBManager imageDBManager;

    public ImageServiceImpl(FileSystemManager fileSystemManager, ImageDBManager imageDBManager) {
        this.fileSystemManager = fileSystemManager;
        this.imageDBManager = imageDBManager;
    }

    @Override
    public List<Image> getImages(ViewBounds viewBounds, Map<String, String> searchCriteria) {

        List<Image> images = imageDBManager.loadImages(viewBounds, searchCriteria);
        return fileSystemManager.updateImagesPath(images);
    }

    @Override
    public void uploadImage(Image image) throws IOException {

        // Save uploaded image to file system
        fileSystemManager.saveImage(image);

        // Save image to database
        imageDBManager.saveImage(image);
    }

    @Override
    public void deleteImage(Image image) {
    }

}
