package com.jrew.geocatch.mobile.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.adapter.PostponedImageAdapter;
import com.jrew.geocatch.mobile.dao.PostponedImageManager;
import com.jrew.geocatch.mobile.model.PostponedImage;
import com.jrew.geocatch.mobile.util.DialogUtil;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 24.02.14
 * Time: 15:14
 * To change this template use File | Settings | File Templates.
 */
public class LoadPostponedPhotosTask extends AsyncTask<Void, Void, List<PostponedImage>> {

    /** **/
    private Context context;

    /** **/
    private ProgressDialog dialog;

    /** **/
    private View layout;

    /**
     *
     * @param context
     * @param layout
     */
    public LoadPostponedPhotosTask(Context context, View layout) {
        this.context = context;
        this.layout = layout;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = DialogUtil.createProgressDialog(context, R.string.postponedPhotosLoadingMessage);
    }

    @Override
    protected List<PostponedImage> doInBackground(Void... voids) {
        return PostponedImageManager.loadPostponedImages(context);
    }

    @Override
    protected void onPostExecute(List<PostponedImage> postponedImages) {
        super.onPostExecute(postponedImages);

        if (postponedImages != null && postponedImages.size() > 0) {

            ListView postponedPhotos = (ListView) layout.findViewById(R.id.postponedPhotosListView);
            postponedPhotos.setAdapter(new PostponedImageAdapter(context, postponedImages));

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        } else {

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            CharSequence text = context.getString(R.string.postponedPhotosLoadingError);
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }
}
