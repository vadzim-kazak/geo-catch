package com.jrew.geocatch.web.model.criteria;

import java.io.Serializable;

/**
 * Represents day period search criteria
 */
public class MonthPeriodSearchCriterion implements Serializable {

    /** **/
    private int fromMonth;

    /** **/
    private int toMonth;

    /**
     *
     * @return
     */
    public int getFromMonth() {
        return fromMonth;
    }

    /**
     *
     * @param fromMonth
     */
    public void setFromMonth(int fromMonth) {
        this.fromMonth = fromMonth;
    }

    /**
     *
     * @return
     */
    public int getToMonth() {
        return toMonth;
    }

    /**
     *
     * @param toMonth
     */
    public void setToMonth(int toMonth) {
        this.toMonth = toMonth;
    }
}
