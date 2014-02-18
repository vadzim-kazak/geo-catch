package com.jrew.geocatch.repository.service;

import com.jrew.geocatch.repository.dao.database.ImageReviewDBManager;
import com.jrew.geocatch.web.model.ImageReview;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 18.02.14
 * Time: 17:32
 * To change this template use File | Settings | File Templates.
 */
public class ImageReviewServiceImpl implements ImageReviewService {

    @Autowired
    private ImageReviewDBManager imageReviewDBManager;

    @Override
    public void handleReview(ImageReview imageReview) {
        if (imageReview.getReviewType() == ImageReview.ReviewType.LIKE) {
            handleLikeReview(imageReview);
        } else if (imageReview.getReviewType() == ImageReview.ReviewType.DISLIKE) {
            handleDislikeReview(imageReview);
        } else if (imageReview.getReviewType() == ImageReview.ReviewType.REPORT) {
            handleReportReview(imageReview);
        }
    }

    /**
     *
     * @param imageReview
     */
    private void handleLikeReview(ImageReview imageReview) {
        if (imageReview.isSelected()) {
            imageReviewDBManager.handleSetLikeReview(imageReview.getImageId(), imageReview.getDeviceId());
        } else {
            imageReviewDBManager.handleClearLikeReview(imageReview.getImageId(), imageReview.getDeviceId());
        }
    }

    /**
     *
     * @param imageReview
     */
    private void handleDislikeReview(ImageReview imageReview) {
        if (imageReview.isSelected()) {
            imageReviewDBManager.handleSetDislikeReview(imageReview.getImageId(), imageReview.getDeviceId());
        } else {
            imageReviewDBManager.handleClearDislikeReview(imageReview.getImageId(), imageReview.getDeviceId());
        }
    }

    /**
     *
     * @param imageReview
     */
    private void handleReportReview(ImageReview imageReview) {
        if (imageReview.isSelected()) {
            imageReviewDBManager.handleSetReportReview(imageReview.getImageId(), imageReview.getDeviceId());
        } else {
            imageReviewDBManager.handleClearReportReview(imageReview.getImageId(), imageReview.getDeviceId());
        }
    }

    @Override
    public int getLikeReviewsCount(long imageId) {
        return imageReviewDBManager.getLikeReviewsCount(imageId);
    }

    @Override
    public int getDislikeReviewsCount(long imageId) {
        return imageReviewDBManager.getDislikeReviewsCount(imageId);
    }

    @Override
    public int getReportReviewsCount(long imageId) {
        return imageReviewDBManager.getReportReviewsCount(imageId);
    }

    @Override
    public boolean isLikeSelected(long imageId, String deviceId) {
        return imageReviewDBManager.isLikeSelected(imageId, deviceId);
    }

    @Override
    public boolean isDislikeSelected(long imageId, String deviceId) {
        return imageReviewDBManager.isDislikeSelected(imageId, deviceId);
    }

    @Override
    public boolean isReportSelected(long imageId, String deviceId) {
        return imageReviewDBManager.isReportSelected(imageId, deviceId);
    }
}
