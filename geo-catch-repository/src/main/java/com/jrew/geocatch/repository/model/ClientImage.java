package com.jrew.geocatch.repository.model;

import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.Date;
import java.util.List;

/**
 * Simplified version of {@link Image} dedicated for view image action
 */
public class ClientImage {

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
    private Image.PrivacyLevel privacyLevel;

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
    public Image.PrivacyLevel getPrivacyLevel() {
        return privacyLevel;
    }

    /**
     *
     * @param privacyLevel
     */
    public void setPrivacyLevel(Image.PrivacyLevel privacyLevel) {
        this.privacyLevel = privacyLevel;
    }
}
