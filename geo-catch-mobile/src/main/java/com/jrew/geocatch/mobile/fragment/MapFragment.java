package com.jrew.geocatch.mobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.listener.MarkerOnClickListener;
import com.jrew.geocatch.mobile.model.ImageMarkerPair;
import com.jrew.geocatch.mobile.reciever.ImageServiceResultReceiver;
import com.jrew.geocatch.mobile.service.ImageService;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 28.10.13
 * Time: 18:03
 * To change this template use File | Settings | File Templates.
 *
 */
public class MapFragment extends SupportMapFragment {

    /** **/
    private GoogleMap googleMap;

    /** **/
    private Map<Integer, ImageMarkerPair> imageMarkerPairs;

    /** **/
    public ImageServiceResultReceiver imageResultReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View result = super.onCreateView(inflater, container, savedInstanceState);

        if (googleMap == null) {

            googleMap = getMap();
            int mapType = Integer.parseInt(getResources().getString(R.config.mapType));
            googleMap.setMapType(mapType);

            imageMarkerPairs = new HashMap<Integer, ImageMarkerPair>();

            final MapFragment fragment = this;
            googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

                @Override
                public void onCameraChange(CameraPosition cameraPosition) {

                    // Get current view bounds
                    LatLngBounds latLngBounds = googleMap.getProjection().getVisibleRegion().latLngBounds;

                    // Remove invisible markers
                    removeInvisibleMarkers(latLngBounds);

                    // Load new images for view bounds
                    loadImages(latLngBounds);
                }
            });

            /** Set custom on marker click listener  **/
            MarkerOnClickListener markerOnclickListener = new MarkerOnClickListener(imageMarkerPairs, this);
            googleMap.setOnMarkerClickListener(markerOnclickListener);

            imageResultReceiver = new ImageServiceResultReceiver(new Handler(), this);
        }

        return result;
    }

    /**
     *
     * @param latLngBounds
     */
    private void removeInvisibleMarkers(LatLngBounds latLngBounds) {

        for (Map.Entry<Integer, ImageMarkerPair> entry : imageMarkerPairs.entrySet()) {
//            ImageMarkerPair imageMarkerPair = entry.getValue();
//            Marker currentMarker = imageMarkerPair.getMarker();
//            if (latLngBounds.contains(currentMarker.getPosition())) {
//                currentMarker.remove();
//                Image image = imageMarkerPair.getImage();
//                imageMarkerPairs.remove(image.getId());
//            }
        }
    }

    /**
     *
     * @param latLngBounds
     */
    private void loadImages(LatLngBounds latLngBounds) {
        final Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), ImageService.class);
        intent.putExtra(ImageService.RECEIVER_KEY, imageResultReceiver);
        intent.putExtra(ImageService.COMMAND_KEY, ImageService.Commands.LOAD_IMAGES);
        intent.putExtra(ImageService.REQUEST_KEY, latLngBounds);
        getActivity().startService(intent);
    }

    /**
     *
     * @param image
     */
    public void loadThumbnail(Image image) {
        final Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), ImageService.class);
        intent.putExtra(ImageService.RECEIVER_KEY, imageResultReceiver);
        intent.putExtra(ImageService.COMMAND_KEY, ImageService.Commands.LOAD_IMAGE_THUMBNAIL);
        intent.putExtra(ImageService.IMAGE_KEY, image);
        getActivity().startService(intent);
    }

    /**
     *
     * @return
     */
    public Map<Integer, ImageMarkerPair> getImageMarkerPairs() {
        return imageMarkerPairs;
    }

    /**
     *
     * @return
     */
    public GoogleMap getGoogleMap() {
        return googleMap;
    }
}
