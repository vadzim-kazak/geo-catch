package com.jrew.geocatch.mobile.util;

import android.app.Activity;
import android.content.res.Configuration;

import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 07.02.14
 * Time: 16:04
 * To change this template use File | Settings | File Templates.
 */
public class LocalizationUtil {

    /**
     *
     * @param localeToSwitch
     * @param activity
     */
    public static void switchToLocale(String localeToSwitch, Activity activity) {

        switchToLocale(localeToSwitch, activity, false);
    }

    /**
     *
     * @param localeToSwitch
     * @param activity
     * @param saveLocale
     */
    public static void switchToLocale(String localeToSwitch, Activity activity, boolean saveLocale) {

        Locale locale = new Locale(localeToSwitch);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        activity.getBaseContext().getResources().updateConfiguration(config,
                activity.getBaseContext().getResources().getDisplayMetrics());

        if (saveLocale) {
            SharedPreferencesHelper.saveLocale(localeToSwitch, activity);
            activity.recreate();
        }
    }
}
