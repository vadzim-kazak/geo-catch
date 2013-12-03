package com.jrew.geocatch.repository.model;

import com.jrew.geocatch.repository.model.DomainProperty;
import com.jrew.geocatch.repository.model.Image;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

/**
 * Metamodel class for {@link com.jrew.geocatch.repository.model.Image} entity.
 */
@StaticMetamodel(DomainProperty.class)
public class DomainProperty_ {

    /** **/
    public static volatile SingularAttribute<Image, Long> id;

    /** **/
    public static volatile SingularAttribute<Image, Long> type;

    /** **/
    public static volatile SingularAttribute<Image, Long> item;

    /** **/
    public static volatile SingularAttribute<Image, String> locale;

    /** **/
    public static volatile SingularAttribute<Image, String> value;

    /** **/
    public static volatile SingularAttribute<Image, Boolean> isDefault;

}