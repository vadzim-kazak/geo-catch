package com.jrew.geocatch.mobile.util;

import android.content.res.Resources;
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

}
