package com.jrew.geocatch.repository.model;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 21.02.14
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */
public class Location {

    /** **/
    double latitude, longitude;

    /**
     *
     */
    public Location() {}

    /**
     *
     * @param latitude
     * @param longitude
     */
    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     *
     * @return
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     *
     * @param latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     *
     * @return
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     *
     * @param longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
