package com.jrew.geocatch.web.model.criteria;

/**
 * Represents day period search criteria
 */
public class DayPeriodSearchCriterion {

    /** **/
    private int fromHour;

    /** **/
    private int toHour;

    /**
     *
     * @return
     */
    public int getFromHour() {
        return fromHour;
    }

    /**
     *
     * @param fromHour
     */
    public void setFromHour(int fromHour) {
        this.fromHour = fromHour;
    }

    /**
     *
     * @return
     */
    public int getToHour() {
        return toHour;
    }

    /**
     *
     * @param toHour
     */
    public void setToHour(int toHour) {
        this.toHour = toHour;
    }
}
