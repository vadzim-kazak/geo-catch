package com.jrew.geocatch.repository.dao.filesystem;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 9/10/13
 * Time: 6:51 PM
 *
 * Image file folder resolving functionality interface.
 *
 */
public interface FolderLocator {

    /**
     * Gets full path to image folder according with image's coordinates.
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public String getFolderAbsolutePath(double latitude, double longitude) throws IOException;

    /**
     * Gets full path to images root folder
     *
     * @return
     * @param fullPath
     */
    public String getRelativeToRootPath(String fullPath);

    /**
     * Gets path to root folder of images repository
     *
     * @return
     */
    public String getRootFolderPath();
}
