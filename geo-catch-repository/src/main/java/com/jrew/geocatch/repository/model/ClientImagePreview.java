package com.jrew.geocatch.repository.model;

import java.util.Date;
import java.util.List;

/**
 * Lightweight version of {@link Image} entity for web representation
 */
public class ClientImagePreview {

    /** **/
    private long id;

    /** **/
    private double latitude;

    /** **/
    private double longitude;

    /** **/
    private String thumbnailPath;

    /**
     *
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(long id) {
        this.id = id;
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

    /**
     *
     * @return
     */
    public String getThumbnailPath() {
        return thumbnailPath;
    }

    /**
     *
     * @param thumbnailPath
     */
    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }
}