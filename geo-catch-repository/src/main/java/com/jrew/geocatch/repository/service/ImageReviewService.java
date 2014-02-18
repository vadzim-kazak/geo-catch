package com.jrew.geocatch.repository.service;

import com.jrew.geocatch.web.model.ImageReview;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 18.02.14
 * Time: 17:31
 * To change this template use File | Settings | File Templates.
 */
public interface ImageReviewService {

    /**
     *
     * @param imageReview
     */
    public void handleReview(ImageReview imageReview);

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
