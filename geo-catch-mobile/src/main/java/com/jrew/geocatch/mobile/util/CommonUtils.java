package com.jrew.geocatch.mobile.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.provider.Settings;
import android.view.Display;
import com.jrew.geocatch.mobile.R;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 13.11.13
 * Time: 16:45
 * To change this template use File | Settings | File Templates.
 */
public class CommonUtils {

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

}
