package com.jrew.geocatch.web.model.criteria;

import com.jrew.geocatch.web.model.DomainProperty;
import com.jrew.geocatch.web.model.ViewBounds;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents images search criteria
 */
public class SearchCriteria implements Serializable {

    /** **/
    public static String OWNER_ANY_VALUE = "any";

    /** **/
    public static String OWNER_SELF_VALUE = "self";

    /** **/
    private ViewBounds viewBounds;

    /** **/
    private String deviceId;

    /** **/
    private boolean loadOwnImages;

    /** **/
    private String owner;

    /** **/
    private List<DomainProperty> domainProperties = new ArrayList<DomainProperty>(3);

    /** **/
    private DayPeriodSearchCriterion dayPeriod;

    /** **/
    private MonthPeriodSearchCriterion monthPeriod;

    /**
     *
     * @return
     */
    public ViewBounds getViewBounds() {
        return viewBounds;
    }

    /**
     *
     * @param viewBounds
     */
    public void setViewBounds(ViewBounds viewBounds) {
        this.viewBounds = viewBounds;
    }

    /**
     *
     * @return
     */
    public String getDeviceId() {
        return deviceId;
    }

    /**
     *
     * @param deviceId
     */
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    /**
     *
     * @return
     */
    public String getOwner() {
        return owner;
    }

    /**
     *
     * @param owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     *
     * @return
     */
    public List<DomainProperty> getDomainProperties() {
        return domainProperties;
    }

    /**
     *
     * @return
     */
    public DayPeriodSearchCriterion getDayPeriod() {
        return dayPeriod;
    }

    /**
     *
     * @param dayPeriod
     */
    public void setDayPeriod(DayPeriodSearchCriterion dayPeriod) {
        this.dayPeriod = dayPeriod;
    }

    /**
     *
     * @return
     */
    public MonthPeriodSearchCriterion getMonthPeriod() {
        return monthPeriod;
    }

    /**
     *
     * @param monthPeriod
     */
    public void setMonthPeriod(MonthPeriodSearchCriterion monthPeriod) {
        this.monthPeriod = monthPeriod;
    }

    /**
     *
     * @return
     */
    public boolean isLoadOwnImages() {
        return loadOwnImages;
    }

    /**
     *
     * @param loadOwnImages
     */
    public void setLoadOwnImages(boolean loadOwnImages) {
        this.loadOwnImages = loadOwnImages;
    }
}
