package com.jrew.geocatch.mobile.dao;

import android.content.Context;
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
    public synchronized static void persistPostponedImage(Context context, PostponedImage postponedImage) {

        PostponedImageDatabaseHelper helper = getHelper(context);
        try {
            helper.getDao().create(postponedImage);
        } catch (SQLException exception) {
            Log.e(LOG_NAME, "Couldn't persist postponed image.", exception);
        }
    }

    /**
     *
     * @param context
     * @param postponedImage
     */
    public synchronized static void deletePostponedImage(Context context, PostponedImage postponedImage) {

        PostponedImageDatabaseHelper helper = getHelper(context);
        try {
            helper.getDao().delete(postponedImage);
        } catch (SQLException exception) {
            Log.e(LOG_NAME, "Couldn't delete postponed image.", exception);
        }
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
    public synchronized static void close() {
        if (helper != null) {
            helper.close();
            helper = null;
        }
    }

}
