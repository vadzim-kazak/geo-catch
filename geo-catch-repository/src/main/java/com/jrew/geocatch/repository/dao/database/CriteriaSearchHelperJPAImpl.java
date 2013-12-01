package com.jrew.geocatch.repository.dao.database;

import com.jrew.geocatch.repository.model.*;
import com.jrew.geocatch.repository.model.criteria.SearchCriteria;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  Implements criteria query creation functionality under JPA specification
 */
public class CriteriaSearchHelperJPAImpl implements CriteriaSearchHelper {

    /** **/
    private static String OWNER_KEY = "owner";

    /** **/
    private static String OWNER_ANY_VALUE = "any";

    /** **/
    private static String OWNER_SELF_VALUE = "self";

    /** **/
    private static String DEVICE_ID = "deviceId";


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CriteriaQuery<Image> createSearchImagesCriteria(SearchCriteria searchCriteria) {

        // Criteria builder
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        // Criteria query
        CriteriaQuery<Image> criteriaQuery = criteriaBuilder.createQuery(Image.class);
        // Root criteria instance
        Root<Image> image = criteriaQuery.from(Image.class);

        // Fetch all domain properties during search - emulate EAGER fetch strategy
        image.fetch(Image_.domainProperties);

        List<Predicate> predicates = new ArrayList<Predicate>();

        // Add view bound predicate
        predicates.add(createViewBoundsCriteria(criteriaBuilder, image, searchCriteria));

        // Add owner predicate
        predicates.add(createOwnerCriteria(criteriaBuilder, image, searchCriteria));

        // Add domainProperties predicate
        Predicate domainPropertiesCriteria = createDomainInfoCriteria(criteriaBuilder, image, searchCriteria);
        if (domainPropertiesCriteria != null) {
            predicates.add(domainPropertiesCriteria);
        }

        Predicate[] predicatesArray = predicates.toArray(new Predicate[predicates.size()]);
        criteriaQuery.where(criteriaBuilder.and(predicatesArray));
        criteriaQuery.select(image);

        return criteriaQuery;
    }

    /**
     *
     * @param criteriaBuilder
     * @param image
     * @param searchCriteria
     * @return
     */
    private Predicate createViewBoundsCriteria(CriteriaBuilder criteriaBuilder,
                                                     Root<Image> image, SearchCriteria searchCriteria) {

        ViewBounds viewBounds = searchCriteria.getViewBounds();

        // Latitude predicate
        Predicate latitudePredicate = criteriaBuilder.between(image.get(Image_.latitude),
                viewBounds.getSouthWestLat(), viewBounds.getNorthEastLat());

        // Longitude predicate
        Predicate longitudePredicate = criteriaBuilder.between(image.get(Image_.longitude),
                viewBounds.getSouthWestLng(), viewBounds.getNorthEastLng());

        return criteriaBuilder.and(latitudePredicate, longitudePredicate);
    }

    /**
     *
     * @param criteriaBuilder
     * @param image
     * @param searchCriteria
     * @return
     */
    private Predicate createOwnerCriteria(CriteriaBuilder criteriaBuilder,
                                          Root<Image> image, SearchCriteria searchCriteria) {
        //searchCriteria.put(DEVICE_ID, "1");

        // Provided deviceId
        String deviceId = searchCriteria.getDeviceId();

        /**
         * We need to search for public images and at the same time for user's private images in two cases:
         * 1) User selected OWNER_ANY_VALUE on UI in the owner field
         * 2) There is no any owner setting came from UI - search for public by default
         */
        String owner = searchCriteria.getOwner();
        if (OWNER_ANY_VALUE.equalsIgnoreCase(owner) || StringUtils.isEmpty(owner)) {

            Predicate ownerPublicPredicate = criteriaBuilder.equal(image.get(Image_.privacyLevel),
                    Image.PrivacyLevel.PUBLIC);

            /**
             * Currently images are mapped to user by its deviceId.
             * So, try to find user images only if deviceId provided.
             */
            if (!StringUtils.isEmpty(deviceId)) {
                Predicate devicePredicate = criteriaBuilder.equal(image.get(Image_.deviceId), deviceId);
                Predicate privatePredicate = criteriaBuilder.equal(image.get(Image_.privacyLevel),
                        Image.PrivacyLevel.PRIVATE);

                Predicate ownerPrivatePredicate = criteriaBuilder.and(devicePredicate, privatePredicate);
                return criteriaBuilder.or(ownerPublicPredicate, ownerPrivatePredicate);
            }

            return ownerPublicPredicate;

        } else {

            /**
             *  We need to search for user private images if there is deviceId came from UI
             */
             return criteriaBuilder.equal(image.get(Image_.deviceId), deviceId);
        }
    }

    /**
     *
     * @param criteriaBuilder
     * @param image
     * @param searchCriteria
     * @return
     */
    private Predicate createDomainInfoCriteria(CriteriaBuilder criteriaBuilder,
                                               Root<Image> image, SearchCriteria searchCriteria) {

        List<DomainProperty> domainProperties = searchCriteria.getDomainProperties();
        if (domainProperties != null && !domainProperties.isEmpty()) {

            // Perform join to have possibility to search by Domain Properties
            Join domainPropertyJoin = image.join(Image_.domainProperties);
            List<Predicate> domainPropertyPredicates = new ArrayList<Predicate>();

            for (DomainProperty domainProperty : domainProperties) {
                Predicate domainPropertyPredicate =
                        //criteriaBuilder.equal(domainPropertyJoin.get(DomainProperty_.item), (Long) domainProperty.getType());
                        criteriaBuilder.equal(domainPropertyJoin.get(DomainProperty_.item), domainProperty.getType());
                domainPropertyPredicates.add(domainPropertyPredicate);
            }

            Predicate[] predicates = domainPropertyPredicates.toArray(new Predicate[domainPropertyPredicates.size()]);
            return criteriaBuilder.and(predicates);
        }

        return null;
    }

}
