package com.jrew.geocatch.repository.dao.filesystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

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
    private final static double MAX_LATITUDE_VALUE = 90d;

    /** Min latitude value **/
    private final static double MIN_LATITUDE_VALUE = -90d;

    /** Max longitude value **/
    private final static double MAX_LONGITUDE_VALUE = 180d;

    /** Min longitude value **/
    private final static double MIN_LONGITUDE_VALUE = -180d;

    /** Folder's name is composed from area coordinates:
     * <lat1>separator<long1>separator<lat2>separator<long2>
     *  This variable represents separator value   **/
    @Value("#{configProperties['folderLocator.folderDegreeSeparator']}")
    private String folderDegreeSeparator;

    /** Path to root folder of images storage  **/
    @Value("#{configProperties['folderLocator.rootFolderPath']}")
    private String rootFolderPath;

    /**
     *  Each folder will store images from some area with lat1, long1, lat2, long2 coordinates.
     *  This variable set degrees range for all folders, i.e. |lat1 - lat2| value.
     *  **/
    @Value("#{configProperties['folderLocator.foldersDegreeCoverage']}")
    private double foldersDegreeCoverage;

    /**
     *  Util class for degree range value representation.
     */
    public class DegreeRange {

        /** Start degree range value **/
        private double startDegree;

        /** End degree range value **/
        private double endDegree;

        /**
         */
        public DegreeRange() {
        }

        /**
         * @return
         */
        public double getStartDegree() {
            return startDegree;
        }

        /**
         * @param startDegree
         */
        public void setStartDegree(double startDegree) {
            this.startDegree = startDegree;
        }

        /**
         * @return
         */
        public double getEndDegree() {
            return endDegree;
        }

        /**
         * @param endDegree
         */
        public void setEndDegree(double endDegree) {
            this.endDegree = endDegree;
        }
    }

    /**
     * Constructor
     */
    public FolderLocatorImpl() {}

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
    public String getFolderAbsolutePath(double latitude, double longitude) throws IOException {
        return  rootFolderPath + File.separator + getFolder(latitude, longitude);
    }

    /**
     * Gets folder latitude range for provided latitude value.
     *
     * @param latitude
     * @return
     */
    private DegreeRange getFolderLatitudeRange(double latitude) {
        return  getFolderDegreeRange(latitude, MIN_LATITUDE_VALUE, MAX_LATITUDE_VALUE);
    }

    /**
     * Gets folder name for presented coordinates
     *
     * @param latitude
     * @param longitude
     * @return folder name
     */
    private String getFolder(double latitude, double longitude) {

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
     * Gets folder longitude range for provided latitude value.
     *
     * @param longitude
     * @return
     */
    private DegreeRange getFolderLongitudeRange(double longitude) {
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
    private DegreeRange getFolderDegreeRange(double degree, double minValue, double maxValue){

        DegreeRange degreeRange = new DegreeRange();

        for (double i = minValue; i < maxValue + foldersDegreeCoverage; i = i + foldersDegreeCoverage) {
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

    @Override
    public String getRootFolderPath() {
        return rootFolderPath;
    }
}
