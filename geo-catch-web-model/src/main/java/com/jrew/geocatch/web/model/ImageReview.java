package com.jrew.geocatch.web.model;

import java.io.Serializable;

/**
 * Represents user image review activity.
 */
public class ImageReview implements Serializable {

    /**
     *
     */
    public enum ReviewType {

        /** **/
        LIKE,

        /** **/
        DISLIKE,

        /** **/
        REPORT
    }

    /** **/
    private String deviceId;

    /** **/
    private long imageId;

    /** **/
    private ReviewType reviewType;

    /** **/
    private boolean selected;

    /**
     *
     * @return
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     *
     * @param deviceId
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     *
     * @return
     */
    public long getImageId() {
        return imageId;
    }

    /**
     *
     * @param imageId
     */
    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    /**
     *
     * @return
     */
    public ReviewType getReviewType() {
        return reviewType;
    }

    /**
     *
     * @param reviewType
     */
    public void setReviewType(ReviewType reviewType) {
        this.reviewType = reviewType;
    }

    /**
     *
     * @return
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     *
     * @param selected
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
