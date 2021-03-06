package com.jrew.geocatch.repository.dao.database;

import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.web.model.criteria.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 7/31/13
 * Time: 9:46 PM
 */
public class ImageDBManagerJPAImpl implements ImageDBManager {

    /** Limits max fetching images number per request**/
    @Value("#{configProperties['databaseManager.maxImagesPerQuery']}")
    private int maxImagesPerQuery;

    @Value("#{queryProperties['query.images.load']}")
    private String loadImagesQuery;

    /** **/
    @Autowired
    private CriteriaSearchHelper criteriaSearchHelper;

    public ImageDBManagerJPAImpl() {}

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void saveImage(Image image) {
        entityManager.merge(image);
    }

    @Override
    @Transactional
    public void deleteImage(Image image) {
        Image loadedImage = entityManager.find(Image.class, image.getId());
        if (loadedImage != null) {
            entityManager.remove(loadedImage);
        }
    }

    @Override
    @Transactional
    public List<Image> loadImages(SearchCriteria searchCriteria) {

        CriteriaQuery<Image> criteriaQuery = criteriaSearchHelper.createSearchImagesCriteria(searchCriteria);

        TypedQuery<Image> query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult(0);
        query.setMaxResults(maxImagesPerQuery);

        List<Image> result = query.getResultList();
        if (result != null) {
            for (Image image : result) {
                // Need this to load domain properties collection
                image.getDomainProperties().size();
            }
        }

        return result;
    }

    @Override
    @Transactional
    public Image loadImage(long id) {
        Image image = entityManager.find(Image.class, id);

        if (image != null) {
            // Need this to load domain properties collection
            image.getDomainProperties().size();
        }

        return image;
    }
}
