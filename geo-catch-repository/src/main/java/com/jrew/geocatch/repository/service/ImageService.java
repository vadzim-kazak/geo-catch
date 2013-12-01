package com.jrew.geocatch.repository.service;

import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.model.ViewBounds;
import com.jrew.geocatch.repository.model.criteria.SearchCriteria;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Represents images manipulation functionality.
 */
public interface ImageService {

    /**
     *
     * @param searchCriteria
     * @return
     */
    public List<Image> getImages(SearchCriteria searchCriteria);

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
