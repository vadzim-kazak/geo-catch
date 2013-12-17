package com.jrew.geocatch.mobile.reciever;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jrew.geocatch.mobile.fragment.MapFragment;
import com.jrew.geocatch.mobile.model.ImageMarkerPair;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.web.model.ClientImage;
import com.jrew.geocatch.web.model.ClientImagePreview;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/10/13
 * Time: 7:17 AM
 */
public class ImageServiceResultReceiver extends ResultReceiver {

    /**
     * Implements thumbnails loading
     */
    public class ThumbnailLoader {

        /**
         *
         */
        private List<ClientImagePreview> images;

        /**
         *
         */
        private int counter;

        /**
         *
         */
        private boolean isLoading;

        /**
         *
         * @param images
         */
        public void loadThumbnails(List<ClientImagePreview> images) {
            this.images = images;
            counter = 0;
            isLoading = true;
            loadNext();
        }

        /**
         *
         */
        public void loadNext() {
            if(counter < images.size()) {
                ClientImagePreview image = images.get(counter);
                mapFragment.loadThumbnail(image);
                counter++;
            } else {
                isLoading = false;
            }
        }

        /**
         *
         * @return
         */
        public boolean isLoading() {
            return isLoading;
        }
    }

    /** **/
    private MapFragment mapFragment;

    /**
     *
     */
    private ThumbnailLoader thumbnailLoader;

    /**
     *
     * @param handler
     * @param mapFragment
     */
    public ImageServiceResultReceiver(Handler handler, MapFragment mapFragment) {
        super(handler);
        this.mapFragment = mapFragment;
        thumbnailLoader = new ThumbnailLoader();
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

       // Map<Integer, ImageMarkerPair> imageMarkerPairs = mapFragment.getImageMarkerPairs();

        switch (resultCode) {

            // Handle images json loading
            case ImageService.ResultStatus.LOAD_IMAGES_FINISHED:

                List<ClientImagePreview> images =
                        (List<ClientImagePreview>) resultData.getSerializable(ImageService.RESULT_KEY);

                if (images != null && !images.isEmpty()) {

                    filterDisplayedImages(images);
                    if(!images.isEmpty()) {
                        thumbnailLoader.loadThumbnails(images);
                    }
                }
                break;

            // Handle thumbnail loading
            case ImageService.ResultStatus.LOAD_THUMBNAIL_FINISHED:

                ClientImagePreview image = (ClientImagePreview) resultData.getSerializable(ImageService.IMAGE_KEY);
                Bitmap bitmap = (Bitmap) resultData.getParcelable(ImageService.RESULT_KEY);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(image.getLatitude(), image.getLongitude()));
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));

                Marker marker = mapFragment.getGoogleMap().addMarker(markerOptions);
                mapFragment.getImageMarkerPairs().put(image.getId(), new ImageMarkerPair(image, marker));

                // Load next thumbnail image
                thumbnailLoader.loadNext();
                break;

            // Handle service error;
            case ImageService.ResultStatus.ERROR:

                // The could be issue with loading of particular image.
                // So, try to load next images in the row
                if(thumbnailLoader.isLoading()) {
                    thumbnailLoader.loadNext();
                }
                break;
        }
    }

    /**
     *
     * @param images
     */
    private void filterDisplayedImages(List<ClientImagePreview> images) {

        Map<Long, ImageMarkerPair> imageMarkerPairs = mapFragment.getImageMarkerPairs();

        Iterator<ClientImagePreview> imageIterator = images.listIterator();
        while(imageIterator.hasNext()) {
            ClientImagePreview image = imageIterator.next();
            if(imageMarkerPairs.containsKey(image.getId())) {
                imageIterator.remove();
            }
        }
    }
}
