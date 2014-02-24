package com.jrew.geocatch.mobile.dao;

import android.content.Context;
import android.database.DatabaseUtils;
import android.util.Log;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.jrew.geocatch.mobile.model.PostponedImage;
import com.jrew.geocatch.mobile.util.PostponedImageDescComparator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 04.02.14
 * Time: 17:02
 * To change this template use File | Settings | File Templates.
 */
public class PostponedImageManager {

    /** **/
    private final static String LOG_NAME = PostponedImageManager.class.getName();

    /** **/
    private static PostponedImageDatabaseHelper helper;

    /** **/
    private static PostponedImageDescComparator comparator = new PostponedImageDescComparator();

    /**
     *
     * @param context
     * @return
     */
    public static synchronized boolean isPostponedImagesPresented(Context context) {

        PostponedImageDatabaseHelper helper = getHelper(context);
        try {
            if (helper.getDao().countOf() > 0) {
                return true;
            }
        } catch (SQLException exception) {
            Log.e(LOG_NAME, "Couldn't count postponed images number.", exception);
        }

        return false;
    }

    /**
     *
     * @param context
     * @return
     */
    public synchronized static List<PostponedImage> loadPostponedImages(Context context) {

        PostponedImageDatabaseHelper helper = getHelper(context);
        try {
            List<PostponedImage> result = helper.getDao().queryForAll();
            // Sort in desc order by id
            Collections.sort(result, comparator);
            return result;

        } catch (SQLException exception) {
            Log.e(LOG_NAME, "Couldn't load postponed images.", exception);
        }

        return new ArrayList<PostponedImage>();
    }

    /**
     *
     * @param context
     * @param postponedImage
     */
    public synchronized static boolean persistPostponedImage(Context context, PostponedImage postponedImage) {

        PostponedImageDatabaseHelper helper = getHelper(context);
        try {
            helper.getDao().create(postponedImage);
            return true;
        } catch (SQLException exception) {
            Log.e(LOG_NAME, "Couldn't persist postponed image.", exception);
        }

        return false;
    }

    /**
     *
     * @param context
     * @param postponedImage
     */
    public synchronized static boolean deletePostponedImage(Context context, PostponedImage postponedImage) {

        PostponedImageDatabaseHelper helper = getHelper(context);
        try {
            helper.getDao().delete(postponedImage);
            return true;
        } catch (SQLException exception) {
            Log.e(LOG_NAME, "Couldn't delete postponed image.", exception);
        }

        return false;
    }

    /**
     *
     * @param context
     * @return
     */
    private static synchronized PostponedImageDatabaseHelper getHelper(Context context) {

        if (helper == null) {
            helper = new PostponedImageDatabaseHelper(context);
        }

        return helper;
    }

    /**
     *
     */
    public static synchronized void close() {
        if (helper != null) {
            helper.close();
            helper = null;
        }
    }

}
