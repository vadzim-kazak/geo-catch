package com.jrew.geocatch.mobile.listener;

import android.content.Intent;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.jrew.geocatch.mobile.fragment.GeoCatchMapFragment;
import com.jrew.geocatch.mobile.model.Image;
import com.jrew.geocatch.mobile.model.ImageMarkerPair;
import com.jrew.geocatch.mobile.service.ImageService;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/11/13
 * Time: 3:42 PM
 */
public class MarkerOnclickListener implements GoogleMap.OnMarkerClickListener {

    /** **/
    private GeoCatchMapFragment mapFragment;

    /** **/
    private Map<Integer, ImageMarkerPair> imageMarkerPairs;

    /** Constructor **/
    public MarkerOnclickListener(Map<Integer, ImageMarkerPair> imageMarkerPairs, GeoCatchMapFragment mapFragment){
        this.imageMarkerPairs = imageMarkerPairs;
        this.mapFragment = mapFragment;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Image image = findImage(marker);
        if (image != null) {
            mapFragment.loadImagePicture(image);
        }

        return false;
    }

    private Image findImage(Marker marker) {

        for (Map.Entry<Integer, ImageMarkerPair> entry : imageMarkerPairs.entrySet()) {
            ImageMarkerPair imageMarkerPair = entry.getValue();
            if (imageMarkerPair.getMarker().equals(marker)) {
                return imageMarkerPair.getImage();
            }
        }

        return null;
    }
}
