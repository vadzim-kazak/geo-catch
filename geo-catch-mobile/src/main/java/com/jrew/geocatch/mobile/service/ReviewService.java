package com.jrew.geocatch.mobile.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.ResultReceiver;
import com.jrew.geocatch.mobile.util.rest.ReviewRestUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 2/18/14
 * Time: 2:47 PM
 */
public class ReviewService extends IntentService {

    /** Receiver intent key  **/
    public static final String RECEIVER_KEY = "receiver";

    /** **/
    public static final String COMMAND_KEY = "command";

    /** **/
    public static final String REVIEW_KEY = "review";

    /** **/
    public static final String RESULT_KEY = "result";

    /**
     *
     */
    public interface Commands {

        /** **/
        public static final String UPLOAD_REVIEW = "uploadReview";

    }

    /**
     *
     */
    public interface ResultStatus {

        /** **/
        public static final int UPLOAD_REVIEW_FINISHED = 1;

        /** **/
        public static final int ERROR = 2;
    }

    /**
     *
     * @param name
     */
    public ReviewService(String name) {
        super(name);
    }

    /**
     *
     */
    public ReviewService() {
        super(ReviewService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final ResultReceiver receiver = intent.getParcelableExtra(RECEIVER_KEY);
        String command = intent.getStringExtra(COMMAND_KEY);
        Resources resources = getResources();

        try {
            if(command.equals(Commands.UPLOAD_REVIEW)) {

                receiver.send(ResultStatus.UPLOAD_REVIEW_FINISHED,
                        ReviewRestUtil.uploadReview(intent, resources));

            }

        } catch(Exception exception) {
            Bundle bundle = new Bundle();
            bundle.putString(Intent.EXTRA_TEXT, exception.toString());
            receiver.send(ResultStatus.ERROR, bundle);
        }

        this.stopSelf();
    }
}
