package com.jrew.geocatch.mobile.dao;

import android.content.Context;
import android.util.Log;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.jrew.geocatch.mobile.model.PostponedImage;

import java.sql.SQLException;
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
    private static Boolean isBusy = false;

    /**
     *
     * @param context
     * @return
     */
    public static List<PostponedImage> loadPostponedImages(Context context) {

        PostponedImageDatabaseHelper helper = getHelper(context);
        if (helper != null) {
            List<PostponedImage> postponedImages = null;
            try {

                return helper.getDao().queryForAll();

            } catch (SQLException exception) {
                Log.e(LOG_NAME, "Couldn't load canned bundles.", exception);
            } finally {
              releaseHelper();
            }
        }

        return null;
    }

    /**
     *
     * @param context
     * @param postponedImage
     */
    public static void persistPostponedImage(Context context, PostponedImage postponedImage) {

        PostponedImageDatabaseHelper helper = getHelper(context);
        if (helper != null) {
            try {
                helper.getDao().create(postponedImage);
            } catch (SQLException exception) {
                Log.e(LOG_NAME, "Couldn't persist canned bundle.", exception);
            }finally {
                releaseHelper();
            }
        }
    }

    /**
     *
     * @param context
     * @return
     */
    private static synchronized PostponedImageDatabaseHelper getHelper(Context context) {

        if (!isBusy) {

            if (helper == null) {
                helper = new PostponedImageDatabaseHelper(context);
            }

            isBusy = true;
            return helper;
        }

        return null;
    }

    /**
     *
     */
    private synchronized static void releaseHelper() {
         isBusy = false;
    }

    /**
     *
     */
    public static void close() {
        if (helper != null) {
            helper.close();
            helper = null;
        }
        isBusy = false;
    }

}
