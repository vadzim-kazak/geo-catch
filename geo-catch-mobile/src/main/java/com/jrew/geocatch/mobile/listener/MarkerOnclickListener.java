package com.jrew.geocatch.mobile.listener;

import android.os.Bundle;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.jrew.geocatch.mobile.activity.MainActivity;
import com.jrew.geocatch.mobile.fragment.MapFragment;
import com.jrew.geocatch.mobile.model.ImageMarkerPair;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.web.model.ClientImagePreview;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/11/13
 * Time: 3:42 PM
 */
public class MarkerOnClickListener implements GoogleMap.OnMarkerClickListener {

    /** **/
    private MapFragment mapFragment;

    /** **/
    private Map<Long, ImageMarkerPair> imageMarkerPairs;

    /** Constructor **/
    public MarkerOnClickListener(Map<Long, ImageMarkerPair> imageMarkerPairs, MapFragment mapFragment){
        this.imageMarkerPairs = imageMarkerPairs;
        this.mapFragment = mapFragment;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        ClientImagePreview image = findImage(marker);
        if (image != null) {
            Bundle fragmentData = new Bundle();
            fragmentData.putSerializable(ImageService.IMAGE_KEY, image);
            MainActivity activity = (MainActivity) mapFragment.getActivity();
            activity.getFragmentSwitcher().showImageViewFragment(fragmentData);
        }

        return false;
    }

    private ClientImagePreview findImage(Marker marker) {

        for (Map.Entry<Long, ImageMarkerPair> entry : imageMarkerPairs.entrySet()) {
            ImageMarkerPair imageMarkerPair = entry.getValue();
            Marker currentMarker = imageMarkerPair.getMarker();
            if (currentMarker.getId().equals(marker.getId())) {
                return imageMarkerPair.getImage();
            }
        }

        return null;
    }
}
