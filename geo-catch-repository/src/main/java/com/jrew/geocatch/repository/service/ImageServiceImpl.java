package com.jrew.geocatch.repository.service;

import com.jrew.geocatch.repository.dao.database.ImageDBManager;
import com.jrew.geocatch.repository.dao.filesystem.FileSystemManager;
import com.jrew.geocatch.repository.model.ClientImage;
import com.jrew.geocatch.repository.model.ClientImagePreview;
import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.web.model.criteria.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation for {@link ImageService} interface
 */
public class ImageServiceImpl implements ImageService {

    /** **/
    private final FileSystemManager fileSystemManager;

    /** **/
    private final ImageDBManager imageDBManager;

    /** **/
    @Autowired
    @Qualifier("clientImagePreviewConverter")
    private Converter<Image, ClientImagePreview> clientImagePreviewConverter;

    /** **/
    @Autowired
    @Qualifier("clientImageConverter")
    private Converter<Image, ClientImage> clientImageConverter;

    public ImageServiceImpl(FileSystemManager fileSystemManager, ImageDBManager imageDBManager) {
        this.fileSystemManager = fileSystemManager;
        this.imageDBManager = imageDBManager;
    }

    @Override
    public List<ClientImagePreview> getImages(SearchCriteria searchCriteria) {

        List<Image> images = imageDBManager.loadImages(searchCriteria);

        // Update images paths
        fileSystemManager.updateThumbnailPath(images);

        return convertToClientImages(images);
    }

    @Override
    public ClientImage getImage(long imageId) {
        Image image = imageDBManager.loadImage(imageId);
        fileSystemManager.updatePath(image);
        return clientImageConverter.convert(image);
    }

    @Override
    public void uploadImage(Image image) throws IOException {

        // Save uploaded image to file system
        fileSystemManager.saveImage(image);

        // Save image to database
        imageDBManager.saveImage(image);
    }

    @Override
    public void deleteImage(long imageId, String deviceId) {

        Image image = imageDBManager.loadImage(imageId);

        // Need to verify that image has the same deviceId as provided before deletion
        if (image != null && image.getDeviceId().equalsIgnoreCase(deviceId)) {

            // 1) Remove image from file system
            fileSystemManager.deleteImage(image);

            //2) Remove image from db
            imageDBManager.deleteImage(image);
        }
    }

    /**
     *
     * @param images
     * @return
     */
    private List<ClientImagePreview> convertToClientImages(List<Image> images) {
        List<ClientImagePreview> clientImagePreviews = new ArrayList<ClientImagePreview>(images.size());
        for (Image image : images) {
            clientImagePreviews.add(clientImagePreviewConverter.convert(image));
        }
        return clientImagePreviews;
    }

    @Override
    public void updateImage(Image image) {
        imageDBManager.saveImage(image);
    }
}
