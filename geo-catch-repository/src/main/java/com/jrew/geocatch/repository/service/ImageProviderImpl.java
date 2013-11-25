package com.jrew.geocatch.repository.service;

import com.jrew.geocatch.repository.dao.database.DatabaseManager;
import com.jrew.geocatch.repository.dao.database.DomainPropertyDBManager;
import com.jrew.geocatch.repository.dao.filesystem.FileSystemManager;
import com.jrew.geocatch.repository.model.DomainProperty;
import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.model.ViewBounds;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 7/23/13
 * Time: 9:37 PM
 * To change this template use File | Settings | File Templates.
 *
 * Implementation for {@link ImageProvider} interface
 */
public class ImageProviderImpl implements ImageProvider {

    /** **/
    private final FileSystemManager fileSystemManager;

    /** **/
    private final DatabaseManager databaseManager;

    @Autowired
    private DomainPropertyDBManager propertyManager;

    public ImageProviderImpl(FileSystemManager fileSystemManager, DatabaseManager databaseManager) {
        this.fileSystemManager = fileSystemManager;
        this.databaseManager = databaseManager;
    }

    @Override
    public List<Image> getImages(ViewBounds viewBounds) {

        List<Image> images = databaseManager.loadImages(viewBounds);
        return fileSystemManager.updateImagesPath(images);
    }

    @Override
    public void uploadImage(Image image) throws IOException {

        // Save uploaded image to file system
        fileSystemManager.saveImage(image);

        List<DomainProperty> properties =  image.getDomainProperties();
        for (DomainProperty domainProperty : properties) {
            propertyManager.saveDomainProperty(domainProperty);
        }

        // Save image to database
        databaseManager.saveImage(image);
    }

    @Override
    public void deleteImage(Image image) {
    }

}
