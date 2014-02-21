package com.jrew.geocatch.mobile.reciever;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.fragment.MapFragment;
import com.jrew.geocatch.mobile.model.ImageMarkerPair;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.mobile.util.CommonUtil;
import com.jrew.geocatch.mobile.util.ServiceUtil;
import com.jrew.geocatch.web.model.ClientImagePreview;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

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

    /** **/
    private Paint iconPaint;

    /** **/
    private Paint thumbnailPaint;

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

        iconPaint = new Paint();
        iconPaint.setColor(Color.WHITE);
        iconPaint.setStyle(Paint.Style.FILL);

        thumbnailPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        switch (resultCode) {

            // Handle images json loading
            case ImageService.ResultStatus.LOAD_IMAGES_FINISHED:

                List<ClientImagePreview> images =
                        (List<ClientImagePreview>) resultData.getSerializable(ImageService.RESULT_KEY);

                if (images != null && !images.isEmpty()) {

                    filterDisplayedImages(images);
                    if(!images.isEmpty()) {

                        for (final ClientImagePreview imagePreview : images) {

                            // Load image, decode it to Bitmap and return Bitmap to callback
                            ImageLoader imageLoader = ImageLoader.getInstance();
                            imageLoader.loadImage(imagePreview.getThumbnailPath(), new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                                    MarkerOptions markerOptions = new MarkerOptions();
                                    markerOptions.position(new LatLng(imagePreview.getLatitude(), imagePreview.getLongitude()));

                                    markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createIconWithBorder(loadedImage)));

                                    Marker marker = mapFragment.getGoogleMap().addMarker(markerOptions);
                                    mapFragment.getImageMarkerPairs().put(imagePreview.getId(), new ImageMarkerPair(imagePreview, marker));

                                }
                            });
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

    /**
     *
     * @param thumbnail
     * @return
     */
    private Bitmap createIconWithBorder(Bitmap thumbnail) {

       double scaleFactor = Double.parseDouble(mapFragment.getResources().getString(R.config.thumbnailSizeScaleFactor));
       double borderSizeFactor = Double.parseDouble(mapFragment.getResources().getString(R.config.thumbnailBorderSizeScaleFactor));

       // get larger display size
       int largerSide = CommonUtil.getDisplayLargerSideSize(mapFragment.getActivity());

       int thumbnailSize = (int) (largerSide * scaleFactor);
       int borderSize = (int)(thumbnailSize * borderSizeFactor);

       int iconSize = thumbnailSize + borderSize * 2;

       Bitmap icon = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.RGB_565);

       Canvas canvas = new Canvas(icon);
       canvas.drawRect(0, 0, iconSize, iconSize, iconPaint);

       Bitmap scaledThumbnail = Bitmap.createScaledBitmap(thumbnail, thumbnailSize, thumbnailSize, false);

       // Draw scaled thumbnail on bitmap
       canvas.drawBitmap(scaledThumbnail, borderSize, borderSize, thumbnailPaint);

       return icon;
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
