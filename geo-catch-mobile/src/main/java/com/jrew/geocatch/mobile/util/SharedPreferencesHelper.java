package com.jrew.geocatch.mobile.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.jrew.geocatch.mobile.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 2/1/14
 * Time: 3:44 PM
 */
public class SharedPreferencesHelper {

    /** **/
    private static final String LAST_SYNC_DATE_PROPERTY = "lastSyncDate";

    /** **/
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     *
     * @param activity
     * @return
     */
    public static Date getLastSyncDate(Activity activity) {

        SharedPreferences preferences = activity.getPreferences(Activity.MODE_PRIVATE);
        String lastSyncDateRecord = preferences.getString(LAST_SYNC_DATE_PROPERTY, "");
        if (lastSyncDateRecord.length() > 0) {
            DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
            Date lastSyncDate = null;
            try {
                return lastSyncDate = dateFormat.parse(lastSyncDateRecord);
            } catch(ParseException exception) {
                Log.e(CommonUtils.getDebugTag(activity.getResources()), "Couldn't parse last domain sync date.", exception);
            }
        }

        return null;
    }

    /**
     *
     * @param activity
     */
    public static void setLastSyncDate(Activity activity) {

        SharedPreferences preferences = activity.getPreferences(Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT);
        Date now = new Date();
        editor.putString(LAST_SYNC_DATE_PROPERTY, dateFormat.format(now));
        editor.commit();
    }

}
