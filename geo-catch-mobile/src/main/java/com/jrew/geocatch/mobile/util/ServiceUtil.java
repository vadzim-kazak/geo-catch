package com.jrew.geocatch.mobile.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.web.model.criteria.SearchCriteria;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 05.02.14
 * Time: 11:33
 * To change this template use File | Settings | File Templates.
 */
public class ServiceUtil {

    /**
     *
     * @param bundle
     * @param resultReceiver
     * @param activity
     */
    public static void callUploadImageService(Bundle bundle, ResultReceiver resultReceiver, Activity activity) {
        final Intent intent = new Intent(Intent.ACTION_SYNC, null, activity, ImageService.class);
        intent.putExtra(ImageService.RECEIVER_KEY, resultReceiver);
        intent.putExtra(ImageService.COMMAND_KEY, ImageService.Commands.UPLOAD_IMAGE);
        intent.putExtra(ImageService.REQUEST_KEY, bundle);
        activity.startService(intent);
    }

    /**
     *
     * @param searchCriteria
     * @param resultReceiver
     * @param activity
     */
    public static void callLoadImagesService(SearchCriteria searchCriteria, ResultReceiver resultReceiver, Activity activity) {
        final Intent intent = new Intent(Intent.ACTION_SYNC, null, activity, ImageService.class);
        intent.putExtra(ImageService.RECEIVER_KEY, resultReceiver);
        intent.putExtra(ImageService.COMMAND_KEY, ImageService.Commands.LOAD_IMAGES);
        intent.putExtra(ImageService.REQUEST_KEY, searchCriteria);
        activity.startService(intent);
    }

}
