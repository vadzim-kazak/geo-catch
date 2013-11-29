package com.jrew.geocatch.repository.model;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 7/23/13
 * Time: 9:27 PM
 * To change this template use File | Settings | File Templates.
 *
 * Represents Google Maps Map View Bounds entity
 */
public class ViewBounds {

    /** **/
    @NotNull
    @Range(min = -90, max = 90, message = "Invalid value")
    private double northEastLat;

    /** **/
    @NotNull
    @Range(min = -180, max = 180, message = "Invalid value")
    private double northEastLng;

    /** **/
    @NotNull
    @Range(min = -90, max = 90, message = "Invalid value")
    private double southWestLat;

    /** **/
    @NotNull
    @Range(min = -180, max = 180, message = "Invalid value")
    private double southWestLng;

    /**
     *
     */
    public ViewBounds() {}
    /**
     * Constructor
     *
     * @param northEastLat
     * @param northEastLng
     * @param southWestLat
     * @param southWestLng
     */
    public ViewBounds(double northEastLat,
                      double northEastLng,
                      double southWestLat,
                      double southWestLng) {

        this.northEastLat = northEastLat;
        this.northEastLng = northEastLng;
        this.southWestLat = southWestLat;
        this.southWestLng = southWestLng;
    }

    /**
     *
     * @return
     */
    public double getNorthEastLat() {
        return northEastLat;
    }

    /**
     *
     * @return
     */
    public double getNorthEastLng() {
        return northEastLng;
    }

    /**
     *
     * @return
     */
    public double getSouthWestLat() {
        return southWestLat;
    }

    /**
     *
     * @return
     */
    public double getSouthWestLng() {
        return southWestLng;
    }

    /**
     * @param northEastLat
     */
    public void setNorthEastLat(double northEastLat) {
        this.northEastLat = northEastLat;
    }

    /**
     * @param northEastLng
     */
    public void setNorthEastLng(double northEastLng) {
        this.northEastLng = northEastLng;
    }

    /**
     * @param southWestLat
     */
    public void setSouthWestLat(double southWestLat) {
        this.southWestLat = southWestLat;
    }

    /**
     * @param southWestLng
     */
    public void setSouthWestLng(double southWestLng) {
        this.southWestLng = southWestLng;
    }

    @Override
    public String toString() {

        StringBuilder message = new StringBuilder();
        message.append(northEastLat).append(':')
               .append(northEastLng).append(':')
               .append(southWestLat).append(':')
               .append(southWestLng);

        return message.toString();
    }
}
