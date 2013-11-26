package com.jrew.geocatch.repository.dao.database;

import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.model.ViewBounds;

import java.util.List;

/**
 * Presents Image processing functionality
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
     * @param viewBounds
     * @return
     */
    public List<Image> loadImages(ViewBounds viewBounds);
}
