package com.jrew.geocatch.mobile.util;

import com.jrew.geocatch.web.model.criteria.SearchCriteria;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 12/23/13
 * Time: 3:14 AM
 */
public class SearchCriteriaHolder {

    /** **/
    private static SearchCriteria searchCriteria;

    /**
     * Create search criteria instance
     */
    static {
        searchCriteria = new SearchCriteria();
    }

    /**
     *
     * @return
     */
    public static SearchCriteria getSearchCriteria() {
        return searchCriteria;
    }
}
