package com.jrew.geocatch.repository.dao.database;

import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.model.ViewBounds;

import javax.persistence.criteria.CriteriaQuery;
import java.util.Map;

/**
 * Interface for criteria query creation functionality
 */
public interface CriteriaSearchHelper {

    /**
     *
     * @param viewBounds
     * @param searchCriteria
     * @return
     */
    public CriteriaQuery<Image> createSearchImagesCriteria(ViewBounds viewBounds, Map<String, String> searchCriteria);

}
