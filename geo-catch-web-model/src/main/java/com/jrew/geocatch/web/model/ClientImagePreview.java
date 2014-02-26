package com.jrew.geocatch.web.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Lightweight version of Image entity for web representation
 */
public class ClientImagePreview implements Serializable {

    /** **/
    private long id;

    /** **/
    private double latitude;

    /** **/
    private double longitude;

    /** **/
    private String thumbnailPath;

    /** **/
    private List<DomainProperty> domainProperties;

    /** **/
    private boolean own;

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

    /**
     *
     * @return
     */
    public List<DomainProperty> getDomainProperties() {
        return domainProperties;
    }

    /**
     *
     * @param domainProperties
     */
    public void setDomainProperties(List<DomainProperty> domainProperties) {
        this.domainProperties = domainProperties;
    }

    /**
     *
     * @return
     */
    public boolean isOwn() {
        return own;
    }

    /**
     *
     * @param own
     */
    public void setOwn(boolean own) {
        this.own = own;
    }
}
