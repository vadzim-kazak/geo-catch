package com.jrew.geocatch.mobile.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.dao.PostponedImageManager;
import com.jrew.geocatch.mobile.model.PostponedImage;
import com.jrew.geocatch.mobile.util.CommonUtils;
import com.jrew.geocatch.mobile.util.DialogUtil;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 2/4/14
 * Time: 1:47 PM
 */
public class PostponedImageAdapter extends BaseAdapter {

    /** **/
    private List<PostponedImage> postponedImages;

    /** **/
    private Context context;

    private double thumbnailScaleFactor;

    public PostponedImageAdapter(Context context) {
        super();
        this.context = context;
        postponedImages = PostponedImageManager.loadPostponedImages(context);
        thumbnailScaleFactor = Double.parseDouble(
                context.getResources().getString(R.config.postponedPhotosThumbnailSizeScaleFactor));
    }

    @Override
    public int getCount() {
        return postponedImages.size();
    }

    @Override
    public Object getItem(int i) {
        return postponedImages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.postponed_image_adapter_row, null);

        final PostponedImage postponedImage = postponedImages.get(i);

        ImageView photoThumbnail = (ImageView) row.findViewById(R.id.photoThumbnail);
        photoThumbnail.setImageBitmap(postponedImage.getBitmap());

        int displaySize = CommonUtils.getDisplayLargerSideSize((Activity) context);
        int thumbnailSize = (int) (displaySize * thumbnailScaleFactor);
        photoThumbnail.setLayoutParams(new RelativeLayout.LayoutParams(thumbnailSize, thumbnailSize));


        ImageView retryUpload = (ImageView) row.findViewById(R.id.retryUpload);
        retryUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        final PostponedImageAdapter adapter = this;

        ImageView cancelUpload = (ImageView) row.findViewById(R.id.cancelUpload);
        cancelUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressDialog dialog = DialogUtil.createProgressDialog(context);

                PostponedImageManager.deletePostponedImage(context, postponedImage);
                postponedImages.remove(postponedImage);
                adapter.notifyDataSetChanged();

                dialog.hide();
            }
        });

        return row;
    }
}
