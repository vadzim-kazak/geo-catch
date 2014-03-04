package com.jrew.geocatch.mobile.util;

import android.content.Context;
import android.graphics.Point;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.service.MapAreasManager;
import com.jrew.geocatch.web.model.ClientImagePreview;
import com.jrew.geocatch.web.model.DomainProperty;
import com.jrew.geocatch.web.model.ViewBounds;
import com.jrew.geocatch.web.model.criteria.SearchCriteria;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 26.02.14
 * Time: 17:28
 * To change this template use File | Settings | File Templates.
 */
public class SearchUtil {

    /**
     *
     * @param image
     * @param searchCriteria
     * @return
     */
    public static boolean isPassingSearchCriteria(ClientImagePreview image, SearchCriteria searchCriteria) {

        if (isImageVisible(image, searchCriteria.getViewBounds()) &&
            hasDomainProperties(image, searchCriteria.getDomainProperties()) &&
            isOwn(image, searchCriteria.isLoadOwnImages() )) {

            return true;
        }

        return false;
    }

    /**
     *
     * @param image
     * @param viewBounds
     * @return
     */
    private static boolean isImageVisible(ClientImagePreview image, ViewBounds viewBounds) {

        if (image.getLatitude() >=  viewBounds.getSouthWestLat() &&
                image.getLatitude() < viewBounds.getNorthEastLat() &&
                image.getLongitude() >= viewBounds.getSouthWestLng() &&
                image.getLongitude() < viewBounds.getNorthEastLng()) {

            return true;
        }

        return false;
    }

    /**
     *
     * @param image
     * @param bounds
     * @return
     */
    public static boolean isImageBounded(ClientImagePreview image, LatLngBounds bounds) {

        if (image.getLatitude() >= bounds.southwest.latitude &&
                image.getLatitude() < bounds.northeast.latitude &&
                image.getLongitude() >= bounds.southwest.longitude &&
                image.getLongitude() < bounds.northeast.longitude) {

            return true;
        }

        return false;
    }

    /**
     *
     * @param image
     * @param domainProperties
     * @return
     */
    private static boolean hasDomainProperties(ClientImagePreview image, List<DomainProperty> domainProperties) {

        if (domainProperties != null && !domainProperties.isEmpty()) {
            for (DomainProperty domainProperty : domainProperties) {
                if (!hasDomainProperty(image, domainProperty)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     *
     * @param image
     * @param domainProperty
     * @return
     */
    private static boolean hasDomainProperty(ClientImagePreview image, DomainProperty domainProperty) {

        List<DomainProperty> domainProperties = image.getDomainProperties();
        if (domainProperties != null) {
            for (DomainProperty imageDomainProperty : domainProperties) {
                if (imageDomainProperty.getItem() == domainProperty.getItem()) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     *
     * @param image
     * @param isLoadOwnImages
     * @return
     */
    private static boolean isOwn(ClientImagePreview image, boolean isLoadOwnImages) {

        if (isLoadOwnImages && !image.isOwn()) {
            return false;
        }

        return true;
    }

    /**
     *
     * @param images
     * @param imageId
     * @return
     */
    public static boolean isImagePresented(List<ClientImagePreview> images, long imageId) {

        for (ClientImagePreview image : images) {
            if (image.getId() == imageId) {
                return true;
            }
        }

        return false;
    }
}
