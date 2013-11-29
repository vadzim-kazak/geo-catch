package com.jrew.geocatch.repository.service;

import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.model.ViewBounds;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Represents images manipulation functionality.
 */
public interface ImageService {

    /**
     *
     * @param viewBounds
     * @param searchCriteria
     * @return
     */
    public List<Image> getImages(ViewBounds viewBounds, Map<String, String> searchCriteria);

    /**
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
