package com.jrew.geocatch.web.model;

/**
 * Represents Google Maps Map View Bounds entity
 */
public class ViewBounds {

    /** **/
    private double northEastLat;

    /** **/
    private double northEastLng;

    /** **/
    private double southWestLat;

    /** **/
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
