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
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.dao.PostponedImageManager;
import com.jrew.geocatch.mobile.model.PostponedImage;
import com.jrew.geocatch.mobile.reciever.ServiceResultReceiver;
import com.jrew.geocatch.mobile.service.DeletePostponedPhotoTask;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.mobile.util.CommonUtil;
import com.jrew.geocatch.mobile.util.DialogUtil;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;
import com.jrew.geocatch.mobile.util.ServiceUtil;

import java.util.List;
import java.util.ListIterator;

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
    private ProgressDialog uploadingPhotoDialog;

    /** **/
    private ServiceResultReceiver resultReceiver;

    public PostponedImageAdapter(Context context, List<PostponedImage> postponedImages) {

        super();

        this.context = context;

        this.postponedImages = postponedImages;

        thumbnailScaleFactor = Double.parseDouble(
                this.context.getResources().getString(R.config.postponedPhotosThumbnailSizeScaleFactor));

        resultReceiver = new ServiceResultReceiver(new Handler());
        resultReceiver.setReceiver(new ServiceResultReceiver.Receiver() {

            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {

                switch (resultCode) {

                    case ImageService.ResultStatus.UPLOAD_IMAGE_FINISHED:
                        if (resultData.containsKey(ImageService.POSTPONED_IMAGE_ID_KEY)) {
                            long uploadedPostponedId = resultData.getLong(ImageService.POSTPONED_IMAGE_ID_KEY);
                            removePostponedImage(uploadedPostponedId);
                            if (PostponedImageManager.isPostponedImagesPresented(PostponedImageAdapter.this.context)) {
                                PostponedImageAdapter.this.notifyDataSetChanged();
                                if (uploadingPhotoDialog.isShowing()) {
                                    uploadingPhotoDialog.dismiss();
                                }
                            } else {
                                if (uploadingPhotoDialog.isShowing()) {
                                    uploadingPhotoDialog.dismiss();
                                }
                                FragmentSwitcherHolder.getFragmentSwitcher().showUploadedPhotosFragment();
                            }
                        } else {
                            showImageProcessingMessage(R.string.postponedPhotosUploadingError);
                        }
                        break;

                    case ImageService.ResultStatus.ERROR:
                        showImageProcessingMessage(R.string.postponedPhotosUploadingError);
                        break;

                    case ImageService.ResultStatus.ABORTED:
                        showImageProcessingMessage(R.string.postponedPhotosUploadingCancelled);
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

        View row = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.postponed_image_adapter_row, null);
        } else {
            row = view;
        }


        row.setClickable(false);

        final PostponedImage postponedImage = postponedImages.get(i);

        ImageView photoThumbnail = (ImageView) row.findViewById(R.id.photoThumbnail);
        photoThumbnail.setImageBitmap(postponedImage.getBitmap());

        int displaySize = CommonUtil.getDisplayLargerSideSize((Activity) context);
        int thumbnailSize = (int) (displaySize * thumbnailScaleFactor);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(thumbnailSize, thumbnailSize);
        photoThumbnail.setLayoutParams(params);

        ImageView retryUpload = (ImageView) row.findViewById(R.id.retryUpload);

        retryUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DialogInterface.OnCancelListener listener = new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        ServiceUtil.abortImageService(context);
                    }
                };

                uploadingPhotoDialog = DialogUtil.createProgressDialog(context, R.string.postponedPhotosUploadingMessage, listener);
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

                DeletePostponedPhotoTask task = new DeletePostponedPhotoTask(context, PostponedImageAdapter.this);
                task.execute(postponedImage);
            }
        });

        return row;
    }

    /**
     *
     * @param postponedImageId
     */
    private void removePostponedImage(long postponedImageId) {

        ListIterator<PostponedImage> iterator = postponedImages.listIterator();
        while (iterator.hasNext()) {
            PostponedImage postponedImage = iterator.next();
            if (postponedImage.getId() == postponedImageId) {
                PostponedImageManager.deletePostponedImage(PostponedImageAdapter.this.context, postponedImage);
                postponedImages.remove(postponedImage);
            }
        }
    }

    /**
     *
     * @param postponedImage
     */
    public void removePostponedImage(PostponedImage postponedImage) {
        postponedImages.remove(postponedImage);
    }

    /**
     *
     */
    public void showImageProcessingMessage(int messageId) {
        if (uploadingPhotoDialog.isShowing()) {
            uploadingPhotoDialog.dismiss();
        }

        CharSequence text =  PostponedImageAdapter.this.context.getString(messageId);
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(PostponedImageAdapter.this.context, text, duration);
        toast.show();
    }

}
