package com.jrew.geocatch.web.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Simplified version of Image dedicated for view image action
 */
public class ClientImage implements Serializable {

    /**
     *  Image privacy level
     */
    public enum PrivacyLevel {

        /** **/
        PRIVATE,

        FRIENDS_ONLY,

        /** **/
        PUBLIC
    }

    /** Image id **/
    private long id;

    /** Description **/
    private String description;

    /** **/
    private double latitude;

    /** **/
    private double longitude;

    /** **/
    private String path;

    /** **/
    private Date date;

    /** **/
    private int rating;

    private List<DomainProperty> domainProperties;

    /** **/
    private PrivacyLevel privacyLevel;

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
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
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
    public String getPath() {
        return path;
    }

    /**
     *
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     *
     * @return
     */
    public Date getDate() {
        return date;
    }

    /**
     *
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     *
     * @return
     */
    public int getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     */
    public void setRating(int rating) {
        this.rating = rating;
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
    public PrivacyLevel getPrivacyLevel() {
        return privacyLevel;
    }

    /**
     *
     * @param privacyLevel
     */
    public void setPrivacyLevel(PrivacyLevel privacyLevel) {
        this.privacyLevel = privacyLevel;
    }
}
