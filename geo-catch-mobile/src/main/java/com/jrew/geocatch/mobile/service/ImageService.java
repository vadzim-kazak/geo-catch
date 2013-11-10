package com.jrew.geocatch.mobile.service;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.ResultReceiver;
import com.jrew.geocatch.mobile.model.Image;
import com.jrew.geocatch.mobile.service.call.LoadImageThumbnailCall;
import com.jrew.geocatch.mobile.service.call.LoadImagesCall;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.*;

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
        public static final String UPLOAD_IMAGE = "uploadImage";
    }

    public interface ResultStatus {

        /** **/
        public static final int LOAD_IMAGES_FINISHED = 1;

        /** **/
        public static final int LOAD_THUMBNAIL_FINISHED = 2;

        /** **/
        public static final int ERROR = 3;
    }

    /** **/
    private LoadImagesCall loadImagesCall;

    /** **/
    private LoadImageThumbnailCall loadImageThumbnailCall;


    public ImageService(String name) {
        super(name);
        loadImagesCall = new LoadImagesCall();
        loadImageThumbnailCall = new LoadImageThumbnailCall();
    }

    public ImageService() {
        super(ImageService.class.getName());
        loadImagesCall = new LoadImagesCall();
        loadImageThumbnailCall = new LoadImageThumbnailCall();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra(RECEIVER_KEY);
        String command = intent.getStringExtra(COMMAND_KEY);

        try {
            if(command.equals(Commands.LOAD_IMAGES)) {

                receiver.send(ResultStatus.LOAD_IMAGES_FINISHED,
                        loadImagesCall.process(intent));

            } else if (command.equals(Commands.LOAD_IMAGE_THUMBNAIL)) {

                receiver.send(ResultStatus.LOAD_THUMBNAIL_FINISHED,
                        loadImageThumbnailCall.process(intent));
            }

        } catch(Exception exception) {
            Bundle bundle = new Bundle();
            bundle.putString(Intent.EXTRA_TEXT, exception.toString());
            receiver.send(ResultStatus.ERROR, bundle);
        }

        this.stopSelf();
    }


}
