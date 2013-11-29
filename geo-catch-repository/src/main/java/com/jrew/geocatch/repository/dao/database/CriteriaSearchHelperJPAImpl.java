package com.jrew.geocatch.repository.dao.database;

import com.jrew.geocatch.repository.model.DomainProperty;
import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.model.Image_;
import com.jrew.geocatch.repository.model.ViewBounds;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  Encapsulates criteria query creation functionality under JPA specification
 */
public class CriteriaSearchHelperJPAImpl implements CriteriaSearchHelper {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CriteriaQuery<Image> createSearchImagesCriteria(ViewBounds viewBounds, Map<String, String> searchCriteria) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Image> criteriaQuery = criteriaBuilder.createQuery(Image.class);
        Root<Image> image = criteriaQuery.from(Image.class);
        criteriaQuery.select(image);

        Join<Image, DomainProperty> domainProperties = image.join(Image_.domainProperties);
        image.join(Image_.domainProperties);

//        List<Predicate> predicates = new ArrayList<Predicate>();
//        Predicate latitudePredicate = criteriaBuilder.between(image.get(Image_.latitude),
//                viewBounds.getSouthWestLat(), viewBounds.getNorthEastLat());
//        predicates.add(latitudePredicate);
//
//        Predicate longitudePredicate = criteriaBuilder.between(image.get(Image_.longitude),
//                viewBounds.getSouthWestLng(), viewBounds.getNorthEastLng());
//        predicates.add(longitudePredicate);
//
//        Predicate[] predicatesArray = new Predicate[2];
//        predicatesArray[0] = latitudePredicate;
//        predicatesArray[1] = longitudePredicate;
//        criteriaQuery.where(criteriaBuilder.and(predicatesArray));

        return criteriaQuery;
    }
}
