package com.jrew.geocatch.repository.model;

import com.jrew.geocatch.repository.model.DomainProperty;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Metamodel class for {@link com.jrew.geocatch.repository.model.Image} entity.
 */
@StaticMetamodel(com.jrew.geocatch.repository.model.DomainProperty.class)
public class DomainProperty_ {

    /** **/
    public static volatile SingularAttribute<DomainProperty, Long> id;

    /** **/
    public static volatile SingularAttribute<DomainProperty, Long> type;

    /** **/
    public static volatile SingularAttribute<DomainProperty, Long> item;

    /** **/
    public static volatile SingularAttribute<DomainProperty, String> locale;

    /** **/
    public static volatile SingularAttribute<DomainProperty, String> value;

    /** **/
    public static volatile SingularAttribute<DomainProperty, Boolean> isDefault;

}
