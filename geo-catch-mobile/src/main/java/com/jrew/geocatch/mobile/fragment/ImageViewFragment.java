package com.jrew.geocatch.mobile.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.model.Image;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.mobile.reciever.ServiceResultReceiver;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 11/11/13
 * Time: 4:16 PM
 */
public class ImageViewFragment extends Fragment {

    /** **/
    public ServiceResultReceiver imageResultReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Loading image...");


        View imageViewFragmentLayout = inflater.inflate(R.layout.image_view_fragment, container, false);

        final ImageView imageView = (ImageView) imageViewFragmentLayout.findViewById(R.id.imageView);

        imageResultReceiver = new ServiceResultReceiver(new Handler());
        imageResultReceiver.setReceiver(new ServiceResultReceiver.Receiver() {

            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {

                switch (resultCode) {
                    case ImageService.ResultStatus.LOAD_IMAGE_FINISHED:
                         Bitmap image = (Bitmap) resultData.get(ImageService.RESULT_KEY);
                         imageView.setImageBitmap(image);
                         progress.dismiss();
                         break;
                }
            }

        });

        Bundle fragmentData = getArguments();
        if (fragmentData != null && !fragmentData.isEmpty()) {
            Image image = (Image) fragmentData.getSerializable(ImageService.IMAGE_KEY);
            loadImagePicture((Image) image);
            progress.show();
        }

        return imageViewFragmentLayout;
    }

    /**
     *
     * @param image
     */
    private void loadImagePicture(Image image) {
        final Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), ImageService.class);
        intent.putExtra(ImageService.RECEIVER_KEY, imageResultReceiver);
        intent.putExtra(ImageService.COMMAND_KEY, ImageService.Commands.LOAD_IMAGE);
        intent.putExtra(ImageService.IMAGE_KEY, image);
        getActivity().startService(intent);
    }
}