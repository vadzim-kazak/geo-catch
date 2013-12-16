package com.jrew.geocatch.mobile.model;

import com.google.android.gms.maps.model.Marker;
import com.jrew.geocatch.web.model.ClientImagePreview;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/11/13
 * Time: 3:55 PM
 */
public class ImageMarkerPair {

    /** **/
    private Marker marker;

    /** **/
    private ClientImagePreview image;

    /**
     *
     * @param image
     * @param marker
     */
    public ImageMarkerPair(ClientImagePreview image, Marker marker) {
        this.image = image;
        this.marker = marker;
    }

    /**
     *
     * @return
     */
    public Marker getMarker() {
        return marker;
    }

    /**
     *
     * @param marker
     */
    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    /**
     *
     * @return
     */
    public ClientImagePreview getImage() {
        return image;
    }

    /**
     *
     * @param image
     */
    public void setImage(ClientImagePreview image) {
        this.image = image;
    }
}
