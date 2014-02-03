package com.jrew.geocatch.mobile.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.ResultReceiver;
import com.jrew.geocatch.mobile.util.RepositoryRestUtil;

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
    }

    /**
     *
     */
    public interface ResultStatus {

        /** **/
        public static final int LOADING = 1;

        /** **/
        public static final int LOAD_IMAGES_FINISHED = 2;

        /** **/
        public static final int LOAD_THUMBNAIL_FINISHED = 3;

        /** **/
        public static final int LOAD_IMAGE_DATA_FINISHED = 4;

        /** **/
        public static final int LOAD_IMAGE_FINISHED = 5;

        /** **/
        public static final int UPLOAD_IMAGE_FINISHED = 6;

        /** **/
        public static final int ERROR = 7;

        /** **/
        public static final int UPLOAD_IMAGE_STARTED = 8;
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
                        RepositoryRestUtil.loadImages(intent, resources));

            } else if (command.equals(Commands.LOAD_IMAGE_THUMBNAIL)) {

                receiver.send(ResultStatus.LOAD_THUMBNAIL_FINISHED,
                        RepositoryRestUtil.loadThumbnail(intent, resources));

            } else if (command.equals(Commands.LOAD_IMAGE_DATA)) {

                receiver.send(ResultStatus.LOAD_IMAGE_DATA_FINISHED,
                        RepositoryRestUtil.loadImageData(intent, resources));

            } else if (command.equals(Commands.LOAD_IMAGE)) {

                receiver.send(ResultStatus.LOAD_IMAGE_FINISHED,
                        RepositoryRestUtil.loadImage(intent, resources));

            } else if (command.equals(Commands.UPLOAD_IMAGE)) {

                receiver.send(ResultStatus.UPLOAD_IMAGE_STARTED, null);

                receiver.send(ResultStatus.UPLOAD_IMAGE_FINISHED,
                        RepositoryRestUtil.uploadImage(intent, resources));
            }

        } catch(Exception exception) {
            Bundle bundle = new Bundle();
            bundle.putString(Intent.EXTRA_TEXT, exception.toString());
            receiver.send(ResultStatus.ERROR, bundle);
        }

        this.stopSelf();
    }


}
