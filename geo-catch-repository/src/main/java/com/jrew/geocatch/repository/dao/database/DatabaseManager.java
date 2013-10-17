package com.jrew.geocatch.repository.dao.database;

import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.model.ViewBounds;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 7/31/13
 * Time: 9:38 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DatabaseManager {

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
