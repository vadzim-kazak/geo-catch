package com.jrew.geocatch.repository.service;

import com.jrew.geocatch.repository.dao.database.ImageDBManager;
import com.jrew.geocatch.repository.dao.filesystem.FileSystemManager;
import com.jrew.geocatch.repository.model.ClientImagePreview;
import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.model.criteria.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
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
    private Converter<Image, ClientImagePreview> clientImageConverter;

    public ImageServiceImpl(FileSystemManager fileSystemManager, ImageDBManager imageDBManager) {
        this.fileSystemManager = fileSystemManager;
        this.imageDBManager = imageDBManager;
    }

    @Override
    public List<ClientImagePreview> getImages(SearchCriteria searchCriteria) {

        List<Image> images = imageDBManager.loadImages(searchCriteria);

        // Update images pathes
        images = fileSystemManager.updateImagesPath(images);

        return convertToClientImages(images);
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

    /**
     *
     * @param images
     * @return
     */
    private List<ClientImagePreview> convertToClientImages(List<Image> images) {
        List<ClientImagePreview> clientImagePreviews = new ArrayList<ClientImagePreview>(images.size());
        for (Image image : images) {
            clientImagePreviews.add(clientImageConverter.convert(image));
        }
        return clientImagePreviews;
    }
}