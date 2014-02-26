package com.jrew.geocatch.mobile.reciever;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import com.androidmapsextensions.Marker;
import com.androidmapsextensions.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.jrew.geocatch.mobile.fragment.MapFragment;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.mobile.service.cache.ImageCache;
import com.jrew.geocatch.mobile.util.BitmapUtil;
import com.jrew.geocatch.web.model.ClientImagePreview;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/10/13
 * Time: 7:17 AM
 */
public class ImageServiceResultReceiver extends ResultReceiver {



    /** **/
    private MapFragment mapFragment;

    /**
     *
     * @param handler
     * @param mapFragment
     */
    public ImageServiceResultReceiver(Handler handler, MapFragment mapFragment) {
        super(handler);
        this.mapFragment = mapFragment;


    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        switch (resultCode) {

            // Handle images json loading
            case ImageService.ResultStatus.LOAD_IMAGES_FINISHED:

                List<ClientImagePreview> images =
                        (List<ClientImagePreview>) resultData.getSerializable(ImageService.RESULT_KEY);

                if (images != null && !images.isEmpty()) {

                    ImageCache.getInstance().addClientImagesPreview(images);

                    if(!images.isEmpty()) {

                        final Map<Long, Marker> markers = mapFragment.getMarkers();
                        for (final ClientImagePreview imagePreview : images) {

                            if (!markers.containsKey(imagePreview.getId())) {

                                // Load image, decode it to Bitmap and return Bitmap to callback
                                ImageLoader imageLoader = ImageLoader.getInstance();
                                imageLoader.loadImage(imagePreview.getThumbnailPath(), new SimpleImageLoadingListener() {
                                    @Override
                                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                                        if (mapFragment.getActivity() != null) {

                                                MarkerOptions markerOptions = new MarkerOptions();
                                                markerOptions.position(new LatLng(imagePreview.getLatitude(), imagePreview.getLongitude()));
                                                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapUtil.createIconWithBorder(loadedImage, mapFragment.getActivity())));

                                                Marker marker = mapFragment.getGoogleMap().addMarker(markerOptions);
                                                marker.setData(imagePreview);

                                                markers.put(imagePreview.getId(), marker);
                                            }
                                        }
                                });
                            }
                        }
                    }
                }
                break;

            case ImageService.ResultStatus.UPLOAD_IMAGE_STARTED:

                break;

            // Handle service error;
            case ImageService.ResultStatus.ERROR:

                break;
        }
    }
}
