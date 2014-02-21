package com.jrew.geocatch.repository.dao.filesystem;

import com.jrew.geocatch.repository.model.Location;

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
     *
     * @param latitude
     * @param longitude
     * @return
     * @throws IOException
     */
    public String getFolderName(double latitude, double longitude) throws IOException;

    /**
     *
     * @param folderName
     * @return
     */
    public Location getFolderCentralLocation(String folderName);

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
