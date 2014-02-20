package com.jrew.geocatch.repository.model;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 20.02.14
 * Time: 14:51
 * To change this template use File | Settings | File Templates.
 */
public class Region {

    /** **/
    private DegreeRange latitudeRange;

    /** **/
    private DegreeRange longitudeRange;

    /**
     *
     */
    public Region() {
    }

    /**
     *
     * @param startLatitude
     * @param endLatitude
     * @param startLongitude
     * @param endLongitude
     */
    public Region(double startLatitude, double endLatitude,
                  double startLongitude, double endLongitude) {

        latitudeRange = new DegreeRange(startLatitude, endLatitude);
        longitudeRange = new DegreeRange(startLongitude, endLongitude);
    }

    /**
     *
     * @param latitude
     * @param longitude
     * @return
     */
    public boolean isLocationInside(double latitude, double longitude) {

        if (latitudeRange.isDegreeInside(latitude) && longitudeRange.isDegreeInside(longitude)) {
            return true;
        }

        return false;
    }
}
