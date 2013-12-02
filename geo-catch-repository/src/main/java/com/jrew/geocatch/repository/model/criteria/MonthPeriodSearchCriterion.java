package com.jrew.geocatch.repository.model.criteria;

/**
 * Represents day period search criteria
 */
public class MonthPeriodSearchCriterion {

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
