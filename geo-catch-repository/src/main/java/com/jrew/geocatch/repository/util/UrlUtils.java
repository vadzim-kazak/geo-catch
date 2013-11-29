package com.jrew.geocatch.repository.util;

import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Util class for manipulation with url
 */
public class UrlUtils {

   /** Separator between search criteria: <criterion1>CRITERIA_SEPARATOR<criteriaon2> **/
   private static String CRITERIA_SEPARATOR = "&";

    /** Separator between search criterion values: <key>CRITERION_SEPARATOR<value> **/
   private static String CRITERION_SEPARATOR = "=";

    /**
     * Parses query string
     *
     * @param query
     * @return
     */
    public static Map<String, String> createSearchCriteria(String query) {

        Map<String, String> criteria = new HashMap<String, String>();

        if(!StringUtils.isEmpty(query)) {

            String[] criteriaEntries = query.split(CRITERIA_SEPARATOR);
            for (String criteriaEntry : criteriaEntries) {
                String[] splittedCriteriaEntry = criteriaEntry.split(CRITERION_SEPARATOR);
                if(splittedCriteriaEntry.length == 2) {
                    String criterionName = splittedCriteriaEntry[0];
                    String criterionValue = splittedCriteriaEntry[1];
                    criteria.put(criterionName, criterionValue);
                }
            }
        }

        return criteria;
    }

}
