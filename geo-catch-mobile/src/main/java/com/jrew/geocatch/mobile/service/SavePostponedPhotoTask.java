package com.jrew.geocatch.mobile.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.dao.PostponedImageManager;
import com.jrew.geocatch.mobile.model.PostponedImage;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 24.02.14
 * Time: 14:27
 * To change this template use File | Settings | File Templates.
 */
public class SavePostponedPhotoTask extends AsyncTask<PostponedImage, Void, Boolean> {

    /** **/
    private final static long DIALOG_DISPLAYING_TIME = 800;

    /** **/
    private Context context;

    /** **/
    private ProgressDialog dialog;

    /**
     *
     * @param context
     * @param dialog
     */
    public SavePostponedPhotoTask(Context context, ProgressDialog dialog) {
        this.context = context;
        this.dialog = dialog;
    }

    @Override
    protected Boolean doInBackground(PostponedImage... postponedImages) {

        boolean result = false;

        long startOperation = System.currentTimeMillis();

        if (postponedImages != null && postponedImages.length > 0) {
            PostponedImage postponedImage = postponedImages[0];
            result = PostponedImageManager.persistPostponedImage(context, postponedImage);
        }

        long endOperation = System.currentTimeMillis();

        long operationExecuting = endOperation - startOperation;
        long timeToSleep = DIALOG_DISPLAYING_TIME - operationExecuting;
        if (timeToSleep > 0) {
            try {
                Thread.sleep(timeToSleep);
            } catch (InterruptedException exc) {}
        }

        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);

        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        if (result) {
            FragmentSwitcherHolder.getFragmentSwitcher().showPostponedPhotosFragment();
        } else {
            CharSequence text = context.getString(R.string.postponedImageSavingError);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
}
