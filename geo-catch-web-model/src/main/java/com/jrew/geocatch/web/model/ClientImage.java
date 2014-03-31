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
    private List<DomainProperty> domainProperties;

    /** **/
    private PrivacyLevel privacyLevel;

    /** **/
    private boolean isLikeSelected;

    /** **/
    private boolean isDislikeSelected;

    /** **/
    private boolean isReportSelected;

    /** **/
    private int likesCount;

    /** **/
    private int dislikesCount;

    /** **/
    private int reportsCount;

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

    /**
     *
     * @return
     */
    public boolean isLikeSelected() {
        return isLikeSelected;
    }

    /**
     *
     * @param likeSelected
     */
    public void setLikeSelected(boolean likeSelected) {
        isLikeSelected = likeSelected;
    }

    /**
     *
     * @return
     */
    public boolean isDislikeSelected() {
        return isDislikeSelected;
    }

    /**
     *
     * @param dislikeSelected
     */
    public void setDislikeSelected(boolean dislikeSelected) {
        isDislikeSelected = dislikeSelected;
    }

    /**
     *
     * @return
     */
    public boolean isReportSelected() {
        return isReportSelected;
    }

    /**
     *
     * @param reportSelected
     */
    public void setReportSelected(boolean reportSelected) {
        isReportSelected = reportSelected;
    }

    /**
     *
     * @return
     */
    public int getLikesCount() {
        return likesCount;
    }

    /**
     *
     * @param likesCount
     */
    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    /**
     *
     * @return
     */
    public int getDislikesCount() {
        return dislikesCount;
    }

    /**
     *
     * @param dislikesCount
     */
    public void setDislikesCount(int dislikesCount) {
        this.dislikesCount = dislikesCount;
    }

    /**
     *
     * @return
     */
    public int getReportsCount() {
        return reportsCount;
    }

    /**
     *
     * @param reportsCount
     */
    public void setReportsCount(int reportsCount) {
        this.reportsCount = reportsCount;
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
