package com.jrew.geocatch.mobile.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.jrew.geocatch.mobile.model.Image;
import com.jrew.geocatch.mobile.model.ImageMarkerPair;
import com.jrew.geocatch.mobile.service.ImageServiceResultReceiver;
import com.jrew.geocatch.mobile.service.ImageService;

import java.util.HashMap;
import java.util.List;
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

        imageMarkerPairs = new HashMap<Integer, ImageMarkerPair>();

        googleMap = getMap();
        int mapType = Integer.parseInt(getResources().getString(R.config.mapType));
        googleMap.setMapType(mapType);

        final MapFragment fragment = this;
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                LatLngBounds latLngBounds = googleMap.getProjection().getVisibleRegion().latLngBounds;
                loadImages(latLngBounds);
            }
        });

        /** Set custom on marker click listener  **/
        MarkerOnClickListener markerOnclickListener = new MarkerOnClickListener(imageMarkerPairs, this);
        googleMap.setOnMarkerClickListener(markerOnclickListener);

        imageResultReceiver = new ImageServiceResultReceiver(new Handler());
        imageResultReceiver.setReceiver(new ImageServiceResultReceiver.Receiver() {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                switch (resultCode) {
                    case ImageService.ResultStatus.LOAD_IMAGES_FINISHED:

                        List<Image> images =
                                (List<Image>) resultData.getSerializable(ImageService.RESULT_KEY);

                            if (images != null && !images.isEmpty()) {
                                for (Image image : images) {
                                    if(!imageMarkerPairs.containsKey(image.getId())) {
                                        loadThumbnail(image);
                                    }
                                }
                            }
                        break;

                    case ImageService.ResultStatus.LOAD_THUMBNAIL_FINISHED:

                        Image image =
                                (Image) resultData.getSerializable(ImageService.IMAGE_KEY);

                        Bitmap bitmap = (Bitmap) resultData.getParcelable(ImageService.RESULT_KEY);

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(new LatLng(image.getLatitude(), image.getLongitude()));
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));

                        Marker marker = googleMap.addMarker(markerOptions);
                        imageMarkerPairs.put(image.getId(), new ImageMarkerPair(image, marker));

                    case ImageService.ResultStatus.ERROR:

                        // handle the error;
                        break;
                }
            }
        });

        return result;
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
    private void loadThumbnail(Image image) {
        final Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), ImageService.class);
        intent.putExtra(ImageService.RECEIVER_KEY, imageResultReceiver);
        intent.putExtra(ImageService.COMMAND_KEY, ImageService.Commands.LOAD_IMAGE_THUMBNAIL);
        intent.putExtra(ImageService.IMAGE_KEY, image);
        getActivity().startService(intent);
    }

}
