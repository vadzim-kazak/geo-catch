package com.jrew.geocatch.mobile.util;

import android.app.Activity;
import com.jrew.geocatch.mobile.service.LocationManager;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 12.02.14
 * Time: 16:54
 * To change this template use File | Settings | File Templates.
 */
public class LocationManagerHolder {

    /** **/
    private static LocationManager locationManager;

    /**
     *
     * @param activity
     */
    public static void initLocationHolder(Activity activity) {

        if (locationManager == null) {
            locationManager = new LocationManager(activity);
        }
    }

    /**
     *
     * @return
     */
    public static LocationManager getLocationManager() {
        return locationManager;
    }
}
