package com.jrew.geocatch.mobile.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.fragment.PhotoBrowsingFragment;
import com.jrew.geocatch.mobile.reciever.ServiceResultReceiver;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.mobile.service.cache.ImageCache;
import com.jrew.geocatch.mobile.util.*;
import com.jrew.geocatch.web.model.ClientImagePreview;
import com.jrew.geocatch.web.model.criteria.SearchCriteria;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 05.02.14
 * Time: 16:11
 * To change this template use File | Settings | File Templates.
 */
public class UploadedPhotosAdapter extends BaseAdapter {

    /** **/
    private List<ClientImagePreview> images;

    /** **/
    private final Activity activity;

    /** **/
    private double thumbnailScaleFactor;

    /**
     *
     * @param activity
     */
    public UploadedPhotosAdapter(Activity activity) {

        super();

        this.activity = activity;

        thumbnailScaleFactor = Double.parseDouble(
                this.activity.getResources().getString(R.config.gridPhotosThumbnailSizeScaleFactor));

    }

    @Override
    public int getCount() {
        if (images != null) {
           return images.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (images != null) {
            return images.get(i);
        }
        return 0;
    }

    @Override
    public long getItemId(int i) {
        if (images != null) {
            return i;
        }
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = null;
        if (view == null) {
            row = inflater.inflate(R.layout.photo_grid_view_cell, null);
        } else {
            row = view;
        }

        ImageView thumbnailImageView = (ImageView) row.findViewById(R.id.thumbnailImageView);
        thumbnailImageView.setImageResource(R.drawable.fish_frame);

        int displaySize = CommonUtil.getDisplayLargerSideSize((Activity) activity);
        int thumbnailSize = (int) (displaySize * thumbnailScaleFactor);
        thumbnailImageView.setLayoutParams(new LinearLayout.LayoutParams(thumbnailSize, thumbnailSize));

        final ClientImagePreview clientImagePreview = images.get(i);

        // Load thumbnail image
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(clientImagePreview.getThumbnailPath(), thumbnailImageView);

        thumbnailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(ImageService.IMAGE_KEY, clientImagePreview);
                bundle.putBoolean(PhotoBrowsingFragment.BrowsingMode.OWN_PHOTO_BROWSING.toString(), true);
                FragmentSwitcherHolder.getFragmentSwitcher().showPhotoBrowsingFragment(bundle);
            }
        });

        return row;
    }

    /**
     *
     * @param images
     */
    public void setImages(List<ClientImagePreview> images) {
        this.images = images;
    }

    /**
     *
     * @return
     */
    public int getImagesCount() {
        if (images != null) {
            return images.size();
        }

        return 0;
    }
}
