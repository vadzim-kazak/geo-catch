package com.jrew.geocatch.repository.dao.database;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 18.02.14
 * Time: 17:36
 * To change this template use File | Settings | File Templates.
 */
public interface ImageReviewDBManager {

    /**
     *
     * @param imageId
     * @param deviceId
     */
    public void handleSetLikeReview(long imageId, String deviceId);

    /**
     *
     * @param imageId
     * @param deviceId
     */
    public void handleClearLikeReview(long imageId, String deviceId);

    /**
     *
     * @param imageId
     * @param deviceId
     */
    public void handleSetDislikeReview(long imageId, String deviceId);

    /**
     *
     * @param imageId
     * @param deviceId
     */
    public void handleClearDislikeReview(long imageId, String deviceId);

    /**
     *
     * @param imageId
     * @param deviceId
     */
    public void handleSetReportReview(long imageId, String deviceId);

    /**
     *
     * @param imageId
     * @param deviceId
     */
    public void handleClearReportReview(long imageId, String deviceId);

    /**
     *
     * @param imageId
     * @return
     */
    public int getLikeReviewsCount(long imageId);

    /**
     *
     * @param imageId
     * @return
     */
    public int getDislikeReviewsCount(long imageId);

    /**
     *
     * @param imageId
     * @return
     */
    public int getReportReviewsCount(long imageId);

    /**
     *
     * @param imageId
     * @param deviceId
     * @return
     */
    public boolean isLikeSelected(long imageId, String deviceId);

    /**
     *
     * @param imageId
     * @param deviceId
     * @return
     */
    public boolean isDislikeSelected(long imageId, String deviceId);

    /**
     *
     * @param imageId
     * @param deviceId
     * @return
     */
    public boolean isReportSelected(long imageId, String deviceId);
}
