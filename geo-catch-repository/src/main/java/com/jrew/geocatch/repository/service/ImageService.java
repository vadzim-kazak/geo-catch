package com.jrew.geocatch.repository.service;

import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.model.ViewBounds;

import java.io.IOException;
import java.util.List;

/**
 *
 * Represents
 */
public interface ImageService {

    /**
     *
     * @param viewBounds
     * @return
     */
    public List<Image> getImages(ViewBounds viewBounds);

    /**
     *
     * @param image
     * @throws IOException
     */
    public void uploadImage(Image image) throws IOException;

    /**
     *
     * @param image
     */
    public void deleteImage(Image image);
}
