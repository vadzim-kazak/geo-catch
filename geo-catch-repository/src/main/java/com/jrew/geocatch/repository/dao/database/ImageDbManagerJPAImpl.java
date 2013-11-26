package com.jrew.geocatch.repository.dao.database;

import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.model.ViewBounds;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 7/31/13
 * Time: 9:46 PM
 */
@Repository
public class ImageDBManagerJPAImpl implements ImageDBManager {

    /** Limits max fetching images number per request**/
    private int maxImagesPerQuery;

    /** Load images typed parameters query **/
    private String loadImagesQuery;

    public ImageDBManagerJPAImpl() {
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void saveImage(Image image) {
        entityManager.persist(image);
        entityManager.flush();
        entityManager.close();
    }

    @Override
    public void deleteImage(Image image) {}

    @Override
    public List<Image> loadImages(ViewBounds viewBounds) {

        TypedQuery<Image> query = entityManager.createQuery(loadImagesQuery, Image.class);
        query.setParameter(1, viewBounds.getNorthEastLat());
        query.setParameter(2, viewBounds.getNorthEastLng());
        query.setParameter(3, viewBounds.getSouthWestLat());
        query.setParameter(4, viewBounds.getSouthWestLng());

        query.setFirstResult(0);
        query.setMaxResults(maxImagesPerQuery);

        return query.getResultList();
    }

    /**
     *
     * @param maxImagesPerQuery
     */
    public void setMaxImagesPerQuery(int maxImagesPerQuery) {
        this.maxImagesPerQuery = maxImagesPerQuery;
    }

    /**
     *
     * @param loadImagesQuery
     */
    public void setLoadImagesQuery(String loadImagesQuery) {
        this.loadImagesQuery = loadImagesQuery;
    }
}
