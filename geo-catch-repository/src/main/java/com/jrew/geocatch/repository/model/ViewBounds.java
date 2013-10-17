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
    private float northEastLat;

    /** **/
    @NotNull
    @Range(min = -180, max = 180, message = "Invalid value")
    private float northEastLng;

    /** **/
    @NotNull
    @Range(min = -90, max = 90, message = "Invalid value")
    private float southWestLat;

    /** **/
    @NotNull
    @Range(min = -180, max = 180, message = "Invalid value")
    private float southWestLng;

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
    public ViewBounds(float northEastLat,
                      float northEastLng,
                      float southWestLat,
                      float southWestLng) {

        this.northEastLat = northEastLat;
        this.northEastLng = northEastLng;
        this.southWestLat = southWestLat;
        this.southWestLng = southWestLng;
    }

    /**
     *
     * @return
     */
    public float getNorthEastLat() {
        return northEastLat;
    }

    /**
     *
     * @return
     */
    public float getNorthEastLng() {
        return northEastLng;
    }

    /**
     *
     * @return
     */
    public float getSouthWestLat() {
        return southWestLat;
    }

    /**
     *
     * @return
     */
    public float getSouthWestLng() {
        return southWestLng;
    }

    /**
     * @param northEastLat
     */
    public void setNorthEastLat(float northEastLat) {
        this.northEastLat = northEastLat;
    }

    /**
     * @param northEastLng
     */
    public void setNorthEastLng(float northEastLng) {
        this.northEastLng = northEastLng;
    }

    /**
     * @param southWestLat
     */
    public void setSouthWestLat(float southWestLat) {
        this.southWestLat = southWestLat;
    }

    /**
     * @param southWestLng
     */
    public void setSouthWestLng(float southWestLng) {
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
