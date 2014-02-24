package com.jrew.geocatch.mobile.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.adapter.PostponedImageAdapter;
import com.jrew.geocatch.mobile.dao.PostponedImageManager;
import com.jrew.geocatch.mobile.model.PostponedImage;
import com.jrew.geocatch.mobile.util.DialogUtil;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 24.02.14
 * Time: 15:48
 * To change this template use File | Settings | File Templates.
 */
public class DeletePostponedPhotoTask extends AsyncTask<PostponedImage, Void, Boolean> {

    /** **/
    private final static long DIALOG_DISPLAYING_TIME = 800;

    /** **/
    private Context context;

    /** **/
    private ProgressDialog progressDialog;

    /** **/
    private PostponedImage postponedImageToDelete;

    /** **/
    private PostponedImageAdapter adapter;

    /**
     *
     * @param context
     * @param adapter
     */
    public DeletePostponedPhotoTask(Context context, PostponedImageAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = DialogUtil.createProgressDialog(context, R.string.postponedPhotosDeletingMessage);

    }

    @Override
    protected Boolean doInBackground(PostponedImage... postponedImages) {

        boolean result = false;

        long startOperation = System.currentTimeMillis();


        if (postponedImages != null && postponedImages.length > 0) {
            postponedImageToDelete = postponedImages[0];
            result = PostponedImageManager.deletePostponedImage(context, postponedImageToDelete);
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



        if (result) {
            adapter.removePostponedImage(postponedImageToDelete);
            if (PostponedImageManager.isPostponedImagesPresented(context)) {
                adapter.notifyDataSetChanged();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            } else {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                FragmentSwitcherHolder.getFragmentSwitcher().showUploadedPhotosFragment();
            }

        } else {

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            CharSequence text = context.getString(R.string.postponedPhotosDeletingError);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
}
