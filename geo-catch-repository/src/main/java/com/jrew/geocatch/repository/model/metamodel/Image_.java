package com.jrew.geocatch.repository.model.metamodel;

import com.jrew.geocatch.repository.model.DomainProperty;
import com.jrew.geocatch.repository.model.Image;

import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 29.11.13
 * Time: 16:41
 * To change this template use File | Settings | File Templates.
 */

@StaticMetamodel(com.jrew.geocatch.repository.model.Image.class)
/**
 *  Metamodel class for {@link Image} entity.
 */
public class Image_ {

    /** **/
    public static volatile SingularAttribute<Image, Long> id;

    /** **/
    public static volatile SingularAttribute<Image, Long> userId;

    /** **/
    public static volatile SingularAttribute<Image, String> deviceId;

    /** **/
    public static volatile SingularAttribute<Image, String> description;

    /** **/
    public static volatile SingularAttribute<Image, Double> latitude;

    /** **/
    public static volatile SingularAttribute<Image, Double> longitude;

    /** **/
    public static volatile SingularAttribute<Image, String> path;

    /** **/
    public static volatile SingularAttribute<Image, String> thumbnailPath;

    /** **/
    public static volatile SingularAttribute<Image, Date> date;

    /** **/
    public static volatile SingularAttribute<Image, Integer> rating;

    /** **/
    public static volatile SingularAttribute<Image, Boolean> isDeleted;

    /** **/
    public static volatile ListAttribute<Image, DomainProperty> domainProperties;

    /** **/
    public static volatile SingularAttribute<Image, Image.PrivacyLevel> privacyLevel;

}
