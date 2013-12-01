package com.jrew.geocatch.repository.dao.database;

import com.jrew.geocatch.repository.model.Image;
import com.jrew.geocatch.repository.model.ViewBounds;
import com.jrew.geocatch.repository.model.criteria.SearchCriteria;

import javax.persistence.criteria.CriteriaQuery;
import java.util.Map;

/**
 * Interface for criteria query creation functionality
 */
public interface CriteriaSearchHelper {

    /**
     *
     * @param searchCriteria
     * @return
     */
    public CriteriaQuery<Image> createSearchImagesCriteria(SearchCriteria searchCriteria);

}
