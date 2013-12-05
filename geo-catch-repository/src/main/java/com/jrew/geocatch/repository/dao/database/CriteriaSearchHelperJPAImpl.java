package com.jrew.geocatch.repository.dao.database;

import com.jrew.geocatch.repository.model.DomainProperty;
import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.model.ViewBounds;
import com.jrew.geocatch.repository.model.criteria.DayPeriodSearchCriterion;
import com.jrew.geocatch.repository.model.criteria.MonthPeriodSearchCriterion;
import com.jrew.geocatch.repository.model.criteria.SearchCriteria;
import com.jrew.geocatch.repository.model.metamodel.DomainProperty_;
import com.jrew.geocatch.repository.model.metamodel.Image_;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 *  Implements criteria query creation functionality under JPA specification
 */
public class CriteriaSearchHelperJPAImpl implements CriteriaSearchHelper {

    /** **/
    private static String TIME_HOUR_KEY = "hour";

    /** **/
    private static String TIME_MONTH_KEY = "month";

    /** **/
    private static String OWNER_ANY_VALUE = "any";

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

        List<Predicate> predicates = new ArrayList<Predicate>();

        // Add view bound predicate
        predicates.add(createViewBoundsCriterion(criteriaBuilder, image, searchCriteria));

        // Add owner predicate
        predicates.add(createOwnerCriterion(criteriaBuilder, image, searchCriteria));

        // Add domainProperties predicate
        Predicate domainPropertiesCriterion = createDomainInfoCriterion(criteriaBuilder, image, searchCriteria);
        if (domainPropertiesCriterion != null) {
            predicates.add(domainPropertiesCriterion);
        }

        // Add day period predicate
        Predicate dayPeriodCriterion = createDayPeriodCriterion(criteriaBuilder, image, searchCriteria);
        if (dayPeriodCriterion != null) {
            predicates.add(dayPeriodCriterion);
        }

        // Add month period predicate
        Predicate monthPeriodCriterion = createMonthPeriodCriterion(criteriaBuilder, image, searchCriteria);
        if (monthPeriodCriterion != null) {
            predicates.add(monthPeriodCriterion);
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
    private Predicate createViewBoundsCriterion(CriteriaBuilder criteriaBuilder,
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
    private Predicate createOwnerCriterion(CriteriaBuilder criteriaBuilder,
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
    private Predicate createDomainInfoCriterion(CriteriaBuilder criteriaBuilder,
                                                Root<Image> image, SearchCriteria searchCriteria) {

        List<DomainProperty> domainProperties = searchCriteria.getDomainProperties();
        if (domainProperties != null && !domainProperties.isEmpty()) {

            List<Predicate> domainPropertyPredicates = new ArrayList<Predicate>();

            for (DomainProperty domainProperty : domainProperties) {
                // Perform join to have possibility to search by Domain Properties
                Join domainPropertyJoin = image.join(Image_.domainProperties);

                Predicate domainPropertyPredicate =
                        criteriaBuilder.equal(domainPropertyJoin.get(DomainProperty_.item), domainProperty.getItem());
                domainPropertyPredicates.add(domainPropertyPredicate);
            }

            Predicate[] predicates = domainPropertyPredicates.toArray(new Predicate[domainPropertyPredicates.size()]);
            return criteriaBuilder.and(predicates);
        }

        return null;
    }

    /**
     *
     * @param criteriaBuilder
     * @param image
     * @param searchCriteria
     * @return
     */
    private Predicate createDayPeriodCriterion(CriteriaBuilder criteriaBuilder,
                                               Root<Image> image, SearchCriteria searchCriteria) {

        DayPeriodSearchCriterion dayPeriodCriterion = searchCriteria.getDayPeriod();
        if (dayPeriodCriterion != null) {
            // Create expressions that extract time parts:
            Expression<Integer> hour = criteriaBuilder.function(TIME_HOUR_KEY, Integer.class, image.get(Image_.date));
            Predicate fromHour = criteriaBuilder.greaterThanOrEqualTo(hour, dayPeriodCriterion.getFromHour());
            Predicate toHour = criteriaBuilder.lessThan(hour,dayPeriodCriterion.getToHour());
            return criteriaBuilder.and(fromHour, toHour);
        }

        return null;
    }


    /**
     *
     * @param criteriaBuilder
     * @param image
     * @param searchCriteria
     * @return
     */
    private Predicate createMonthPeriodCriterion(CriteriaBuilder criteriaBuilder,
                                                 Root<Image> image, SearchCriteria searchCriteria) {

        MonthPeriodSearchCriterion monthPeriodCriterion = searchCriteria.getMonthPeriod();
        if (monthPeriodCriterion != null) {
            // Create expressions that extract time parts:
            Expression<Integer> month = criteriaBuilder.function(TIME_MONTH_KEY, Integer.class, image.get(Image_.date));
            Predicate fromMonth = criteriaBuilder.greaterThanOrEqualTo(month, monthPeriodCriterion.getFromMonth());
            Predicate toMonth = criteriaBuilder.lessThan(month, monthPeriodCriterion.getToMonth());
            return criteriaBuilder.and(fromMonth, toMonth);
        }

        return null;
    }
}
