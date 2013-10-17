package com.jrew.geocatch.repository.dao.filesystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 9/10/13
 * Time: 7:01 PM
 *
 * It is planned to store images in different folders, not in one from performance point of view.
 * This class provides path to folder according to image's latitude & longitude values.
 *
 * Each folder will store images from some area. Range of this area depends on foldersDegreeCoverage variable.
 */
public class FolderLocatorImpl implements FolderLocator {

    private static final Logger logger = LogManager.getLogger(FolderLocatorImpl.class);

    /** Max latitude value **/
    private final static float MAX_LATITUDE_VALUE = 90f;

    /** Min latitude value **/
    private final static float MIN_LATITUDE_VALUE = -90f;

    /** Max longitude value **/
    private final static float MAX_LONGITUDE_VALUE = 180f;

    /** Min longitude value **/
    private final static float MIN_LONGITUDE_VALUE = -180f;

    /** Folder's name is composed from area coordinates:
     * <lat1>separator<long1>separator<lat2>separator<long2>
     *  This variable represents separator value   **/
    private final String folderDegreeSeparator;

    /** Path to root folder of images storage  **/
    private final String rootFolderPath;

    /**
     *  Each folder will store images from some area with lat1, long1, lat2, long2 coordinates.
     *  This variable set degrees range for all folders, i.e. |lat1 - lat2| value.
     *  **/
        private final float foldersDegreeCoverage;

    /**
     *  Util class for degree range value representation.
     */
    public class DegreeRange {

        /** Start degree range value **/
        private float startDegree;

        /** End degree range value **/
        private float endDegree;

        /**
         */
        public DegreeRange() {
        }

        /**
         * @return
         */
        float getStartDegree() {
            return startDegree;
        }

        /**
         * @param startDegree
         */
        void setStartDegree(float startDegree) {
            this.startDegree = startDegree;
        }

        /**
         * @return
         */
        float getEndDegree() {
            return endDegree;
        }

        /**
         * @param endDegree
         */
        void setEndDegree(float endDegree) {
            this.endDegree = endDegree;
        }
    }

    /**
     * Constructor
     *
     * @param folderDegreeSeparator
     * @param rootFolderPath
     * @param foldersDegreeCoverage
     */
    public FolderLocatorImpl(String folderDegreeSeparator, String rootFolderPath, float foldersDegreeCoverage) {
        this.folderDegreeSeparator = folderDegreeSeparator;
        this.rootFolderPath = rootFolderPath;
        this.foldersDegreeCoverage = foldersDegreeCoverage;
    }

    @Override
    public String getRelativeToRootPath(String fullPath) {

        if (fullPath.contains(rootFolderPath)) {
            //logger.info("Relative folder path is: " + rootFolderPath);
            return fullPath.substring(rootFolderPath.length());
        }

        throw new IllegalArgumentException("Provided path: " + fullPath +
                " doesn't contains root folder: " + rootFolderPath);
    }

    @Override
    public String getFolderAbsolutePath(float latitude, float longitude) throws IOException {
        return  rootFolderPath + File.separator + getFolder(latitude, longitude);
    }

    /**
     * Gets folder name for presented coordinates
     *
     * @param latitude
     * @param longitude
     * @return folder name
     */
    private String getFolder(float latitude, float longitude) {

        DegreeRange latitudeRange = getFolderLatitudeRange(latitude);
        DegreeRange longitudeRange = getFolderLongitudeRange(longitude);

        /* Generate folder name according to template:
            <lat1>separator<long1>separator<lat2>separator<long2>
            Coordinate values are rounded from float to int
        */

        return (int)latitudeRange.getStartDegree() + folderDegreeSeparator +
               (int)longitudeRange.getStartDegree() + folderDegreeSeparator +
               (int)latitudeRange.getEndDegree() + folderDegreeSeparator +
               (int)longitudeRange.getEndDegree();
    }

    /**
     * Gets folder latitude range for provided latitude value.
     *
     * @param latitude
     * @return
     */
    private DegreeRange getFolderLatitudeRange(float latitude) {
        return  getFolderDegreeRange(latitude, MIN_LATITUDE_VALUE, MAX_LATITUDE_VALUE);
    }

    /**
     * Gets folder longitude range for provided latitude value.
     *
     * @param longitude
     * @return
     */
    private DegreeRange getFolderLongitudeRange(float longitude) {
        return getFolderDegreeRange(longitude, MIN_LONGITUDE_VALUE, MAX_LONGITUDE_VALUE);
    }

    /**
     * Gets folder degree range for provided degree value and degree range borders.
     *
     * It separates original length |maxValue - minValue| on segments with length equals to foldersDegreeCoverage value,
     * finds to which of this segments degree value is belonged and returns borders of this segment.
     *
     * @param degree
     * @param minValue
     * @param maxValue
     * @return
     */
    private DegreeRange getFolderDegreeRange(float degree, float minValue, float maxValue){

        DegreeRange degreeRange = new DegreeRange();

        for (float i = minValue; i < maxValue + foldersDegreeCoverage; i = i + foldersDegreeCoverage) {
            if (i >= degree) {
                degreeRange.setStartDegree(i - foldersDegreeCoverage);
                if (i <= maxValue) {
                    degreeRange.setEndDegree(i);
                } else {
                    degreeRange.setEndDegree(maxValue);
                }
                break;
            }
        }

        return degreeRange;
    }

}
