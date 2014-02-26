package com.jrew.geocatch.mobile.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import com.jrew.geocatch.mobile.reciever.DomainInfoServiceResultReceiver;
import com.jrew.geocatch.mobile.service.DomainInfoService;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.mobile.service.ReviewService;
import com.jrew.geocatch.web.model.ClientImage;
import com.jrew.geocatch.web.model.ClientImagePreview;
import com.jrew.geocatch.web.model.ImageReview;
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

    /**
     *
     * @param image
     * @param resultReceiver
     * @param activity
     */
    public static void callLoadImageDataService(ClientImagePreview image, ResultReceiver resultReceiver, Activity activity) {

        final Intent intent = new Intent(Intent.ACTION_SYNC, null, activity, ImageService.class);
        intent.putExtra(ImageService.RECEIVER_KEY, resultReceiver);
        intent.putExtra(ImageService.COMMAND_KEY, ImageService.Commands.LOAD_IMAGE_DATA);
        intent.putExtra(ImageService.REQUEST_KEY, image.getId());
        activity.startService(intent);
    }

    /**
     *
     * @param image
     * @param resultReceiver
     * @param activity
     */
    public static void callLoadImageService(ClientImage image, ResultReceiver resultReceiver, Activity activity) {

        final Intent intent = new Intent(Intent.ACTION_SYNC, null, activity, ImageService.class);
        intent.putExtra(ImageService.RECEIVER_KEY, resultReceiver);
        intent.putExtra(ImageService.COMMAND_KEY, ImageService.Commands.LOAD_IMAGE);
        intent.putExtra(ImageService.IMAGE_KEY, image);
        activity.startService(intent);
    }

    /**
     *
     * @param bundle
     * @param resultReceiver
     * @param activity
     */
    public static void callDeleteImageService(Bundle bundle, ResultReceiver resultReceiver, Activity activity) {

        final Intent intent = new Intent(Intent.ACTION_SYNC, null, activity, ImageService.class);
        intent.putExtra(ImageService.RECEIVER_KEY, resultReceiver);
        intent.putExtra(ImageService.COMMAND_KEY, ImageService.Commands.DELETE_IMAGE);
        intent.putExtra(ImageService.REQUEST_KEY, bundle);
        activity.startService(intent);
    }

    /**
     *
     * @param image
     * @param resultReceiver
     * @param activity
     */
    public static void callLoadImageThumbnailService(ClientImagePreview image,
                                                     ResultReceiver resultReceiver, Activity activity) {

        final Intent intent = new Intent(Intent.ACTION_SYNC, null, activity, ImageService.class);
        intent.putExtra(ImageService.RECEIVER_KEY, resultReceiver);
        intent.putExtra(ImageService.COMMAND_KEY, ImageService.Commands.LOAD_IMAGE_THUMBNAIL);
        intent.putExtra(ImageService.IMAGE_KEY, image);
        activity.startService(intent);
    }

    /**
     *
     * @param bundle
     * @param resultReceiver
     * @param activity
     */
    public static void callLoadDomainInfoService(Bundle bundle, ResultReceiver resultReceiver, Activity activity) {

        final Intent intent = new Intent(Intent.ACTION_SYNC, null, activity, DomainInfoService.class);
        intent.putExtra(DomainInfoService.REQUEST_KEY, bundle);
        intent.putExtra(DomainInfoService.RECEIVER_KEY, resultReceiver);
        activity.startService(intent);
    }

    /**
     *
     * @param imageReview
     * @param resultReceiver
     * @param activity
     */
    public static void callUploadReviewService(ImageReview imageReview, ResultReceiver resultReceiver, Activity activity) {

        final Intent intent = new Intent(Intent.ACTION_SYNC, null, activity, ReviewService.class);
        intent.putExtra(ReviewService.REVIEW_KEY, imageReview);
        intent.putExtra(ReviewService.COMMAND_KEY, ReviewService.Commands.UPLOAD_REVIEW);
        intent.putExtra(ReviewService.RECEIVER_KEY, resultReceiver);
        activity.startService(intent);
    }

    /**
     *
     * @param context
     */
    public static void abortImageService(Context context) {
        final Intent intent = new Intent(Intent.ACTION_SYNC, null, context, ImageService.class);
        intent.putExtra(ImageService.COMMAND_KEY, ImageService.Commands.ABORT);
        context.startService(intent);
    }

}
