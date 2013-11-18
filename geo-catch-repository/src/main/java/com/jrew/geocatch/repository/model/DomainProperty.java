package com.jrew.geocatch.repository.model;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/17/13
 * Time: 1:13 PM
 */

@Embeddable
public class DomainProperty {

    /** **/
    @Id
    @GeneratedValue
    private int id;

    /** **/
    private String locale;

    /** **/
    private String value;

    /** **/
    private boolean isDefault;

    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getLocale() {
        return locale;
    }

    /**
     *
     * @param locale
     */
    public void setLocale(String locale) {
        this.locale = locale;
    }

    /**
     *
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     *
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     *
     * @return
     */
    public boolean isDefault() {
        return isDefault;
    }

    /**
     *
     * @param aDefault
     */
    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
