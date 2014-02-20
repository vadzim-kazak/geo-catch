package com.jrew.geocatch.repository.dao.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 18.02.14
 * Time: 17:38
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class ImageReviewDBManagerImpl implements ImageReviewDBManager {

    @Value("#{queryProperties['query.review.like.insert']}")
    private String insertLikeReviewQuery;

    @Value("#{queryProperties['query.review.like.delete']}")
    private String deleteLikeReviewQuery;

    @Value("#{queryProperties['query.review.dislike.insert']}")
    private String insertDislikeReviewQuery;

    @Value("#{queryProperties['query.review.dislike.delete']}")
    private String deleteDislikeReviewQuery;

    @Value("#{queryProperties['query.review.report.insert']}")
    private String insertReportReviewQuery;

    @Value("#{queryProperties['query.review.report.delete']}")
    private String deleteReportReviewQuery;

    @Value("#{queryProperties['query.review.like.count']}")
    private String countLikeReviewQuery;

    @Value("#{queryProperties['query.review.dislike.count']}")
    private String countDislikeReviewQuery;

    @Value("#{queryProperties['query.review.report.count']}")
    private String countReportReviewQuery;

    @Value("#{queryProperties['query.review.like.presented']}")
    private String isLikeReviewPresentedQuery;

    @Value("#{queryProperties['query.review.dislike.presented']}")
    private String isDislikeReviewPresentedQuery;

    @Value("#{queryProperties['query.review.report.presented']}")
    private String isReportReviewPresentedQuery;

    /** **/
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void handleSetLikeReview(long imageId, String deviceId) {
        processReview(imageId, deviceId, insertLikeReviewQuery);
    }

    @Override
    public void handleClearLikeReview(long imageId, String deviceId) {
        processReview(imageId, deviceId, deleteLikeReviewQuery);
    }

    @Override
    public void handleSetDislikeReview(long imageId, String deviceId) {
        processReview(imageId, deviceId, insertDislikeReviewQuery);
    }

    @Override
    public void handleClearDislikeReview(long imageId, String deviceId) {
        processReview(imageId, deviceId, deleteDislikeReviewQuery);
    }

    @Override
    public void handleSetReportReview(long imageId, String deviceId) {
        processReview(imageId, deviceId, insertReportReviewQuery);
    }

    @Override
    public void handleClearReportReview(long imageId, String deviceId) {
        processReview(imageId, deviceId, deleteReportReviewQuery);
    }

    /**
     *
     * @param imageId
     * @param deviceId
     * @param actionQuery
     */
    private void processReview(long imageId, String deviceId, String actionQuery) {
        Query query = entityManager.createNativeQuery(actionQuery);
        query.setParameter(1, imageId);
        query.setParameter(2, deviceId);

        query.executeUpdate();
    }

    @Override
    public int getLikeReviewsCount(long imageId) {
        return countReviews(imageId, countLikeReviewQuery);
    }

    @Override
    public int getDislikeReviewsCount(long imageId) {
        return countReviews(imageId, countDislikeReviewQuery);
    }

    @Override
    public int getReportReviewsCount(long imageId) {
        return countReviews(imageId, countReportReviewQuery);
    }

    /**
     *
     * @param imageId
     * @param countQuery
     * @return
     */
    private int countReviews(long imageId, String countQuery) {
        Query query = entityManager.createNativeQuery(countQuery);
        query.setParameter(1, imageId);

        List<BigInteger> result = query.getResultList();
        if (result != null && result.size() > 0) {
            return result.get(0).intValue();
        }

        return 0;
    }

    @Override
    public boolean isLikeSelected(long imageId, String deviceId) {
        return isReviewPresented(imageId, deviceId, isLikeReviewPresentedQuery);
    }

    @Override
    public boolean isDislikeSelected(long imageId, String deviceId) {
        return isReviewPresented(imageId, deviceId, isDislikeReviewPresentedQuery);
    }

    @Override
    public boolean isReportSelected(long imageId, String deviceId) {
        return isReviewPresented(imageId, deviceId, isReportReviewPresentedQuery);
    }

    /**
     *
     * @param imageId
     * @param deviceId
     * @param searchQuery
     * @return
     */
    private boolean isReviewPresented(long imageId, String deviceId, String searchQuery) {
        Query query = entityManager.createNativeQuery(searchQuery);
        query.setParameter(1, imageId);
        query.setParameter(2, deviceId);

        List result = query.getResultList();
        if (result != null && result.size() > 0) {
            return true;
        }

        return false;
    }
}
