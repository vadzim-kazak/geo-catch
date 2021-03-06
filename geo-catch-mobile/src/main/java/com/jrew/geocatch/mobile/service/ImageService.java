package com.jrew.geocatch.mobile.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.ResultReceiver;
import com.jrew.geocatch.mobile.util.rest.ImageRestUtil;

import java.io.InterruptedIOException;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/10/13
 * Time: 6:25 AM
 */
public class ImageService extends IntentService {

    /** **/
    private static boolean isAborted;

    /** Receiver intent key  **/
    public static final String RECEIVER_KEY = "receiver";

    /** **/
    public static final String COMMAND_KEY = "command";

    /** **/
    public static final String REQUEST_KEY = "requestKey";

    /** **/
    public static final String RESULT_KEY = "result";

    /** **/
    public static final String IMAGE_KEY = "image";

    /** **/
    public static final String POSTPONED_IMAGE_ID_KEY = "postponedImageId";

    /** **/
    public static final String IMAGE_ID_KEY = "imageId";

    /** **/
    public static final String DEVICE_ID_KEY = "deviceId";

    /**
     *
     */
    public interface Commands {

        /** **/
        public static final String LOAD_IMAGES = "loadImages";

        /** **/
        public static final String LOAD_IMAGE_THUMBNAIL = "loadImageThumbnail";

        /** **/
        public static final String LOAD_IMAGE_DATA = "loadImageData";

        /** **/
        public static final String LOAD_IMAGE = "loadImage";

        /** **/
        public static final String UPLOAD_IMAGE = "uploadImage";

        /** **/
        public static final String DELETE_IMAGE = "deleteImage";

        /** **/
        public static final String ABORT = "abort";
    }

    /**
     *
     */
    public interface ResultStatus {

        /** **/
        public static final int LOAD_IMAGES_FINISHED = 2;

        /** **/
        public static final int LOAD_IMAGE_DATA_FINISHED = 4;

        /** **/
        public static final int UPLOAD_IMAGE_FINISHED = 6;

        /** **/
        public static final int ERROR = 7;

        /** **/
        public static final int UPLOAD_IMAGE_STARTED = 8;

        /** **/
        public static final int DELETE_IMAGE_FINISHED = 9;

        /** **/
        public static final int ABORTED = 10;
    }

    /**
     *
     * @param name
     */
    public ImageService(String name) {
        super(name);
    }

    /**
     *
     */
    public ImageService() {
        super(ImageService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final ResultReceiver receiver = intent.getParcelableExtra(RECEIVER_KEY);
        String command = intent.getStringExtra(COMMAND_KEY);
        Resources resources = getResources();

        try {
            if(command.equals(Commands.LOAD_IMAGES)) {

                receiver.send(ResultStatus.LOAD_IMAGES_FINISHED,
                        ImageRestUtil.loadImages(intent, resources));

            } else if (command.equals(Commands.LOAD_IMAGE_DATA)) {

                receiver.send(ResultStatus.LOAD_IMAGE_DATA_FINISHED,
                        ImageRestUtil.loadImageData(intent, resources));

            } else if (command.equals(Commands.UPLOAD_IMAGE)) {

                receiver.send(ResultStatus.UPLOAD_IMAGE_STARTED, null);

                receiver.send(ResultStatus.UPLOAD_IMAGE_FINISHED,
                        ImageRestUtil.uploadImage(intent, resources));
            } else if (command.equals(Commands.DELETE_IMAGE)) {

                receiver.send(ResultStatus.DELETE_IMAGE_FINISHED,
                        ImageRestUtil.deleteImage(intent, resources));
            }

        } catch(Exception exception) {

            if (isAborted) {
                receiver.send(ResultStatus.ABORTED, null);
            } else {
                receiver.send(ResultStatus.ERROR, null);
            }
        }

        this.stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String command = intent.getExtras().getString(COMMAND_KEY);
        if (Commands.ABORT.equalsIgnoreCase(command)) {
            isAborted = true;
            ImageRestUtil.abort();
            this.stopSelf();
        } else {
            isAborted = false;
        }

        return super.onStartCommand(intent, flags, startId);
    }
}
