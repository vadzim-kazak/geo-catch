package com.jrew.geocatch.repository.model;

import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 7/13/13
 * Time: 12:10 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name="Images")
public class Image {

    /**
     *
     */
    public Image() {}

    /** Image id **/
    @Id
    @GeneratedValue
    private long id;

    /** **/
    private int userId;

    /** Description **/
    private String description;

    /** **/
    @NotNull
    @Range(min = -90, max = 90, message = "Invalid value")
    private float latitude;

    /** **/
    @NotNull
    @Range(min = -180, max = 180, message = "Invalid value")
    private float longitude;

    /** **/
    private String path;

    /** **/
    private String thumbnailPath;

    /** **/
    @Column(columnDefinition = "DATETIME")
    @NotNull
    @Past(message = "Invalid date")
    private Date date;

    /** **/
    private int rating;

    /** **/
    private boolean isDeleted;

    /** **/
    @Transient
    @NotNull
    MultipartFile file;

   // @ElementCollection
   // private List<DomainProperty> domainProperties = new ArrayList<DomainProperty>();

    /**
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(long id) {
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
    public float getLatitude() {
        return latitude;
    }

    /**
     * @param latitude
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    /**
     * @return
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * @param longitude
     */
    public void setLongitude(float longitude) {
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

    /**
     *
     * @return
     */
    public MultipartFile getFile() {
        return file;
    }

    /**
     *
     * @param file
     */
    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @Override
    public String toString() {

        StringBuilder message = new StringBuilder();
        message.append(id).append(':')
               .append(userId).append(':')
               .append(description).append(':')
               .append(latitude).append(':')
               .append(longitude).append(':')
               .append(path).append(':')
               .append(thumbnailPath).append(':')
               .append(date).append(':')
               .append(rating).append(':')
               .append(isDeleted);

        return message.toString();
    }
}
