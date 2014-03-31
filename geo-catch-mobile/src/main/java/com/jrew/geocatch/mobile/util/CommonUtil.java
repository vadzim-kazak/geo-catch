package com.jrew.geocatch.mobile.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Display;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.reciever.DomainInfoServiceResultReceiver;
import com.jrew.geocatch.mobile.service.DomainInfoService;

import java.util.Date;
import java.util.Locale;

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
        return  Math.max(display.getHeight(), display.getWidth());
    }

    /**
     *
     * @param activity
     * @return
     */
    public static int getDisplaySmallerSideSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        return Math.min(display.getHeight(), display.getWidth());
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
     * @param activity
     */
    public static void syncDomainsInfo(Activity activity) {

        Date lastSyncDate = SharedPreferencesHelper.loadLastSyncDate(activity);
        if (lastSyncDate != null) {

            int syncPeriod = activity.getResources().getInteger(R.config.domainInfoSyncPeriodInHours);

            Date currentDate = new Date();
            if (currentDate.getTime() - lastSyncDate.getTime() >= syncPeriod * 60 * 60 * 1000 ) {
                // It's time to perform sync
                processSyncDomainsInfo(activity);
            }

        } else {
            // LastSyncDate isn't set. Probably this is first app launch
            processSyncDomainsInfo(activity);
        }
    }

    /**
     *
     * @param activity
     */
    private static void processSyncDomainsInfo(Activity activity) {

        Bundle bundle = new Bundle();

        String locale = Locale.getDefault().getLanguage();
        bundle.putString(DomainInfoService.LOCALE_KEY, locale);

        ServiceUtil.callLoadDomainInfoService(bundle, new DomainInfoServiceResultReceiver(new Handler(), activity), activity);
    }

}
