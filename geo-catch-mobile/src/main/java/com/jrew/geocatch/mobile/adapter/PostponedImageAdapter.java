package com.jrew.geocatch.mobile.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.dao.PostponedImageManager;
import com.jrew.geocatch.mobile.model.PostponedImage;
import com.jrew.geocatch.mobile.reciever.ServiceResultReceiver;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.mobile.util.CommonUtils;
import com.jrew.geocatch.mobile.util.DialogUtil;
import com.jrew.geocatch.mobile.util.PostponedImageDescComparator;
import com.jrew.geocatch.mobile.util.ServiceUtil;

import java.util.Collections;
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

    /** **/
    private double thumbnailScaleFactor;

    /** **/
    private ProgressDialog dialog;

    /** **/
    private ServiceResultReceiver resultReceiver;

    public PostponedImageAdapter(Context context) {

        super();

        this.context = context;

        postponedImages = PostponedImageManager.loadPostponedImages(this.context);
        // Sort in desc order by id
        Collections.sort(postponedImages, new PostponedImageDescComparator());

        thumbnailScaleFactor = Double.parseDouble(
                this.context.getResources().getString(R.config.postponedPhotosThumbnailSizeScaleFactor));

        dialog = DialogUtil.createProgressDialog(this.context);

        resultReceiver = new ServiceResultReceiver(new Handler());
        resultReceiver.setReceiver(new ServiceResultReceiver.Receiver() {

            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {

                switch (resultCode) {
                    case ImageService.ResultStatus.UPLOAD_IMAGE_STARTED:
                        break;

                    case ImageService.ResultStatus.UPLOAD_IMAGE_FINISHED:
                        dialog.hide();
                        long uploadedPostponedId = resultData.getLong(ImageService.POSTPONED_IMAGE_ID_KEY);
                        removePostponedImage(uploadedPostponedId);
                        PostponedImageAdapter.this.notifyDataSetChanged();
                        break;

                    case ImageService.ResultStatus.ERROR:
                        dialog.hide();
                        break;
                }
            }

        });
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
        row.setClickable(false);

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

                dialog.show();

                Bundle bundle = new Bundle();
                bundle.putSerializable(ImageService.IMAGE_KEY, postponedImage.getUploadImage());
                bundle.putLong(ImageService.POSTPONED_IMAGE_ID_KEY, postponedImage.getId());
                ServiceUtil.callUploadImageService(bundle, resultReceiver, (Activity) context);
            }
        });

        ImageView cancelUpload = (ImageView) row.findViewById(R.id.cancelUpload);
        cancelUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();

                PostponedImageManager.deletePostponedImage(context, postponedImage);
                postponedImages.remove(postponedImage);
                PostponedImageAdapter.this.notifyDataSetChanged();

                dialog.hide();
            }
        });

        return row;
    }

    /**
     *
     * @param postponedImageId
     */
    private void removePostponedImage(long postponedImageId) {

        for (PostponedImage postponedImage : postponedImages) {
            if (postponedImage.getId() == postponedImageId) {
                PostponedImageManager.deletePostponedImage(PostponedImageAdapter.this.context, postponedImage);
                postponedImages.remove(postponedImage);

            }
        }
    }
}
