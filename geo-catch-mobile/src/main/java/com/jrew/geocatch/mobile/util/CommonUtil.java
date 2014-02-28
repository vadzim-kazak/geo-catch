package com.jrew.geocatch.mobile.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.provider.Settings;
import android.view.Display;
import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.Marker;
import com.androidmapsextensions.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.service.cache.ImageCache;
import com.jrew.geocatch.web.model.ClientImagePreview;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 13.11.13
 * Time: 16:45
 * To change this template use File | Settings | File Templates.
 */
public class CommonUtil {

    /**
     *
     * @param activity
     * @return
     */
    public static boolean isGPSEnabled(Activity activity) {
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            return true;
        }

        return false;
    }

    /**
     *
     * @param resources
     * @return
     */
    public static String getDebugTag(Resources resources) {
        return resources.getString(R.config.debugTag);
    }

    /**
     *
     * @param activity
     * @return
     */
    public static int getDisplayLargerSideSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        int largerSide = display.getWidth();
        if (display.getHeight() > largerSide) {
            largerSide = display.getHeight();
        }

        return largerSide;
    }

    /**
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    /**
     *
     * @param markerBitmap
     * @param markerOptions
     * @param imagePreview
     * @param markers
     * @param googleMap
     */
    public static void addMarker(Bitmap markerBitmap,
                                 MarkerOptions markerOptions,
                                 ClientImagePreview imagePreview,
                                 Map<Long, Marker> markers,
                                 GoogleMap googleMap) {

        markerOptions.position(new LatLng(imagePreview.getLatitude(), imagePreview.getLongitude()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(markerBitmap));
        synchronized (markers) {
            if (!markers.containsKey(imagePreview.getId())) {
                Marker marker = googleMap.addMarker(markerOptions);
                marker.setData(imagePreview);
                markers.put(imagePreview.getId(), marker);
            }
        }

    }

}
