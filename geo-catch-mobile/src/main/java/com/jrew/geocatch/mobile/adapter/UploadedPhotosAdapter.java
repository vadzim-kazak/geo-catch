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
import android.widget.LinearLayout;
import android.widget.Toast;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.fragment.PhotoBrowsingFragment;
import com.jrew.geocatch.mobile.reciever.ServiceResultReceiver;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.mobile.util.CommonUtil;
import com.jrew.geocatch.mobile.util.DialogUtil;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;
import com.jrew.geocatch.mobile.util.ServiceUtil;
import com.jrew.geocatch.web.model.ClientImagePreview;
import com.jrew.geocatch.web.model.criteria.SearchCriteria;
import com.nostra13.universalimageloader.core.ImageLoader;

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
    private final Context context;

    /** **/
    private ProgressDialog loadingDialog;

    /** **/
    private ServiceResultReceiver resultReceiver;

    /** **/
    private double thumbnailScaleFactor;

    /**
     *
     * @param context
     */
    public UploadedPhotosAdapter(Context context) {

        super();

        this.context = context;

        loadingDialog = DialogUtil.createProgressDialog(this.context, R.string.uploadedPhotosLoadingMessage);

        resultReceiver = new ServiceResultReceiver(new Handler());
        resultReceiver.setReceiver(new ServiceResultReceiver.Receiver() {

            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {

                switch (resultCode) {
                    case ImageService.ResultStatus.LOAD_IMAGES_FINISHED:
                        if (resultData.containsKey(ImageService.RESULT_KEY)) {
                            images = (List<ClientImagePreview>) resultData.getSerializable(ImageService.RESULT_KEY);
                            notifyDataSetChanged();
                            if (loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                            }
                        } else {
                            showUploadedImagesLoadingError();
                        }

                        break;

                    case ImageService.ResultStatus.ERROR:
                        showUploadedImagesLoadingError();
                        break;
                }
            }

        });

        thumbnailScaleFactor = Double.parseDouble(
                this.context.getResources().getString(R.config.gridPhotosThumbnailSizeScaleFactor));

        loadImagesServiceCall();
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

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = null;
        if (view == null) {
            row = inflater.inflate(R.layout.photo_grid_view_cell, null);
        } else {
            row = view;
        }

        ImageView thumbnailImageView = (ImageView) row.findViewById(R.id.thumbnailImageView);

        int displaySize = CommonUtil.getDisplayLargerSideSize((Activity) context);
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
     */
    public void loadImagesServiceCall() {

        SearchCriteria searchCriteria = new SearchCriteria();

        // Device Id
        searchCriteria.setDeviceId(CommonUtil.getDeviceId(context));
        searchCriteria.setLoadOwnImages(true);

        ServiceUtil.callLoadImagesService(searchCriteria, resultReceiver, (Activity) context);
    }

    /**
     *
     */
    public void showUploadedImagesLoadingError() {

        if (loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }

        CharSequence text = UploadedPhotosAdapter.this.context.getResources().getString(R.string.uploadedPhotosLoadingError);
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(UploadedPhotosAdapter.this.context, text, duration);
        toast.show();
    }
}
