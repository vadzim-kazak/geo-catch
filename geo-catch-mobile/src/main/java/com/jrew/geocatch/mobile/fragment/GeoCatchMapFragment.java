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
import com.jrew.geocatch.mobile.model.Image;
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
public class GeoCatchMapFragment extends SupportMapFragment { // implements GoogleMap.OnCameraChangeListener {

    /** **/
    private GoogleMap googleMap;

    /** **/
    private Map<Integer, Marker> markers;

    public ImageServiceResultReceiver imageResultReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = super.onCreateView(inflater, container, savedInstanceState);

        markers = new HashMap<Integer, Marker>();

        googleMap = getMap();
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        final GeoCatchMapFragment fragment = this;
        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                LatLngBounds latLngBounds = googleMap.getProjection().getVisibleRegion().latLngBounds;
                loadImages(latLngBounds);
            }
        });

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
                                    if(!markers.containsKey(image.getId())) {
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

                        markers.put(image.getId(), googleMap.addMarker(markerOptions));

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
