package com.jrew.geocatch.mobile.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.ResultReceiver;
import com.jrew.geocatch.mobile.service.call.LoadImageCall;
import com.jrew.geocatch.mobile.service.call.LoadImageThumbnailCall;
import com.jrew.geocatch.mobile.service.call.LoadImagesCall;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/10/13
 * Time: 6:25 AM
 */
public class ImageService extends IntentService {

    /** Reciever intent key  **/
    public static final String RECEIVER_KEY = "reciever";

    /** **/
    public static final String COMMAND_KEY = "command";

    /** **/
    public static final String REQUEST_KEY = "requestKey";

    /** **/
    public static final String RESULT_KEY = "result";

    /** **/
    public static final String IMAGE_KEY = "image";


    public interface Commands {

        /** **/
        public static final String LOAD_IMAGES = "loadImages";

        /** **/
        public static final String LOAD_IMAGE_THUMBNAIL = "loadImageThumbnail";

        /** **/
        public static final String LOAD_IMAGE = "loadImage";

        /** **/
        public static final String UPLOAD_IMAGE = "uploadImage";
    }

    public interface ResultStatus {

        /** **/
        public static final int LOADING = 1;

        /** **/
        public static final int LOAD_IMAGES_FINISHED = 2;

        /** **/
        public static final int LOAD_THUMBNAIL_FINISHED = 3;

        /** **/
        public static final int LOAD_IMAGE_FINISHED = 4;

        /** **/
        public static final int ERROR = 5;
    }

    /** **/
    private LoadImagesCall loadImagesCall;

    /** **/
    private LoadImageThumbnailCall loadImageThumbnailCall;

    /** **/
    private LoadImageCall loadImageCall;

    public ImageService(String name) {
        super(name);
        loadImagesCall = new LoadImagesCall();
        loadImageThumbnailCall = new LoadImageThumbnailCall();
        loadImageCall = new LoadImageCall();
    }

    public ImageService() {
        super(ImageService.class.getName());
        loadImagesCall = new LoadImagesCall();
        loadImageThumbnailCall = new LoadImageThumbnailCall();
        loadImageCall = new LoadImageCall();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra(RECEIVER_KEY);
        String command = intent.getStringExtra(COMMAND_KEY);
        Resources resources = getResources();

        try {

            if(command.equals(Commands.LOAD_IMAGES)) {

                receiver.send(ResultStatus.LOAD_IMAGES_FINISHED,
                        loadImagesCall.process(intent, resources));

            } else if (command.equals(Commands.LOAD_IMAGE_THUMBNAIL)) {

                receiver.send(ResultStatus.LOAD_THUMBNAIL_FINISHED,
                        loadImageThumbnailCall.process(intent, resources));

            } else if (command.equals(Commands.LOAD_IMAGE)) {

                receiver.send(ResultStatus.LOAD_IMAGE_FINISHED,
                        loadImageCall.process(intent, resources));
            }

        } catch(Exception exception) {
            Bundle bundle = new Bundle();
            bundle.putString(Intent.EXTRA_TEXT, exception.toString());
            receiver.send(ResultStatus.ERROR, bundle);
        }

        this.stopSelf();
    }


}
