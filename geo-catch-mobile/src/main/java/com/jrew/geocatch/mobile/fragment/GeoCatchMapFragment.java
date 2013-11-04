package com.jrew.geocatch.mobile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.jrew.geocatch.mobile.util.ImageRepositoryUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 28.10.13
 * Time: 18:03
 * To change this template use File | Settings | File Templates.
 *
 */
public class GeoCatchMapFragment extends SupportMapFragment { // implements GoogleMap.OnCameraChangeListener {

    /** **/
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = super.onCreateView(inflater, container, savedInstanceState);

        googleMap = getMap();

        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                LatLngBounds latLngBounds = googleMap.getProjection().getVisibleRegion().latLngBounds;
               // ImageRepositoryUtil.loadImages(latLngBounds);
            }
        });

        return result;
    }
}
