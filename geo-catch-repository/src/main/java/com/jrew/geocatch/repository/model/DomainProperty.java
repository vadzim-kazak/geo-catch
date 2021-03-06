package com.jrew.geocatch.repository.model;

import javax.persistence.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/17/13
 * Time: 1:13 PM
 */

@Entity
@Table(name="domain_property")
public class DomainProperty {

    /** **/
    @Id
    @GeneratedValue
    private long id;

    /** **/
    private long type;

    /** **/
    private long item;

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
    public long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public long getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(long type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public long getItem() {
        return item;
    }

    /**
     *
     * @param item
     */
    public void setItem(long item) {
        this.item = item;
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
