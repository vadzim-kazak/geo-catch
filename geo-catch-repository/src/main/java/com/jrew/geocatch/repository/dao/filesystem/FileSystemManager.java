package com.jrew.geocatch.repository.dao.filesystem;

import com.jrew.geocatch.repository.model.Image;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 7/31/13
 * Time: 9:39 PM
 *
 *
 */
public interface FileSystemManager {

    /**
     * Saves provided image on file system.
     *
     * @param image
     * @return full path to saved image.
     * @throws IOException
     * @throws IllegalArgumentException
     */
    public void saveImage(Image image) throws IOException, IllegalArgumentException;

    /**
     * Performs image deletion on file system.
     *
     * @param image
     */
    public void deleteImage(Image image);

    /**
     * Updates images path.
     *
     * Each image record in database contain image path relative to root folder.
     * This method updates all thumbnail relative paths with some configured relative to
     * app prefix.
     *
     *
     * @param images
     * @return
     */
    public void updateThumbnailPath(List<Image> images);

    /**
     *
     *
     *
     *
     * @param image
     * @return
     */
    public void updatePath(Image image);

}
