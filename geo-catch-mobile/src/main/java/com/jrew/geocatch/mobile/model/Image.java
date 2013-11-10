package com.jrew.geocatch.mobile.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/10/13
 * Time: 7:01 AM
 */
public class Image implements Serializable {

    /**
     *
     */
    public Image() {}

    /** Image id **/
    private int id;

    /** **/
    private int userId;

    /** Description **/
    private String description;

    /** **/
    private double latitude;

    /** **/
    private double longitude;

    /** **/
    private String path;

    /** **/
    private String thumbnailPath;

    /** **/
    private java.util.Date date;

    /** **/
    private int rating;

    /** **/
    private boolean isDeleted;

    /**
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return
     */
    public String getThumbnailPath() {
        return thumbnailPath;
    }

    /**
     * @param thumbnailPath
     */
    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    /**
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * @return
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return
     */
    public int getRating() {
        return rating;
    }

    /**
     * @param rating
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * @return
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     *
     * @param deleted
     */
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

}
