package com.jrew.geocatch.mobile.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.jrew.geocatch.mobile.util.RepositoryRestUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 05.02.14
 * Time: 16:41
 * To change this template use File | Settings | File Templates.
 */
public class ReloadImageTask extends AsyncTask<String, Void, Bundle> {

    /** **/
    private ImageView thumbnail;

    /** **/
    private Context context;

    /** **/
    BaseAdapter adapter;

    public ReloadImageTask(ImageView thumbnail, BaseAdapter adapter, Context context) {
        super();
        this.thumbnail = thumbnail;
        this.context = context;
        this.adapter = adapter;
    }

    @Override
    protected Bundle doInBackground(String... strings) {

        Bundle result = null;

        try {
            result = RepositoryRestUtil.loadImageFromPath(context.getResources(), strings[0]);
        } catch(Exception exception) {

        }

        return  result;
    }

    @Override
    protected void onPostExecute(Bundle bundle) {
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable(ImageService.RESULT_KEY);
            thumbnail.setImageBitmap(bitmap);
           // adapter.notifyDataSetChanged();
        }
    }
}
