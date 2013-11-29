package com.jrew.geocatch.repository.dao.database;

import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.model.ViewBounds;

import java.util.List;
import java.util.Map;

/**
 * Represents Image processing functionality on persistence layer
 */
public interface ImageDBManager {

    /**
     *
     * @param image
     */
    public void saveImage(Image image);

    /**
     *
     * @param image
     */
    public void deleteImage(Image image);

    /**
     *
     *
     * @param viewBounds
     * @param searchCriteria
     * @return
     */
    public List<Image> loadImages(ViewBounds viewBounds, Map<String, String> searchCriteria);
}
