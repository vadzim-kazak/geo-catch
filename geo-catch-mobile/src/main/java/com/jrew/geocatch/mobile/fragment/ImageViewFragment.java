package com.jrew.geocatch.mobile.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.mobile.reciever.ServiceResultReceiver;
import com.jrew.geocatch.web.model.ClientImage;
import com.jrew.geocatch.web.model.ClientImagePreview;
import com.jrew.geocatch.web.model.DomainProperty;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

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

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Loading image...");

        View imageViewFragmentLayout = inflater.inflate(R.layout.image_view_fragment, container, false);

        final DateFormat dateFormat = new SimpleDateFormat(getResources().getString(R.config.repositoryUploadImagesDateFormat));

        final ImageView imageView = (ImageView) imageViewFragmentLayout.findViewById(R.id.imageView);

        final TextView description = (TextView) imageViewFragmentLayout.findViewById(R.id.imageDescription);

        final TextView date = (TextView) imageViewFragmentLayout.findViewById(R.id.imageDate);

        final LinearLayout fishTypeRow = (LinearLayout) imageViewFragmentLayout.findViewById(R.id.fishTypeRow);
        final TextView fishType = (TextView) imageViewFragmentLayout.findViewById(R.id.fishTypeDescription);

        final LinearLayout fishingToolRow = (LinearLayout) imageViewFragmentLayout.findViewById(R.id.fishingToolRow);
        final TextView fishingTool = (TextView) imageViewFragmentLayout.findViewById(R.id.fishingToolDescription);

        final LinearLayout fishingBaitRow = (LinearLayout) imageViewFragmentLayout.findViewById(R.id.fishingBaitRow);
        final TextView fishingBait = (TextView) imageViewFragmentLayout.findViewById(R.id.fishingBaitDescription);

        imageResultReceiver = new ServiceResultReceiver(new Handler());
        imageResultReceiver.setReceiver(new ServiceResultReceiver.Receiver() {

            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {

                switch (resultCode) {

                    case ImageService.ResultStatus.LOAD_IMAGE_DATA_FINISHED:
                        ClientImage clientImage = (ClientImage) resultData.getSerializable(ImageService.RESULT_KEY);

                        // Populate image data
                        // description
                        description.setText(clientImage.getDescription());

                        // date
                        String dateLabel = date.getText().toString();
                        date.setText(dateLabel + dateFormat.format(clientImage.getDate()));

                        // domain properties
                        List<DomainProperty> domainProperties = clientImage.getDomainProperties();
                        for (DomainProperty domainProperty : domainProperties) {
                            long domainPropertyType = domainProperty.getType();
                            if (domainPropertyType == 1) {
                                fishTypeRow.setVisibility(View.VISIBLE);
                                fishType.setText(domainProperty.getValue());
                            } else if (domainPropertyType == 2) {
                                fishingToolRow.setVisibility(View.VISIBLE);
                                fishingTool.setText(domainProperty.getValue());
                            } else if (domainPropertyType == 3) {
                                fishingBaitRow.setVisibility(View.VISIBLE);
                                fishingBait.setText(domainProperty.getValue());
                            }
                        }

                        // Load image
                        loadImage(clientImage);
                        break;

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
            ClientImagePreview image = (ClientImagePreview) fragmentData.getSerializable(ImageService.IMAGE_KEY);
            loadClientImage(image);
            progress.show();
        }

        return imageViewFragmentLayout;
    }

    /**
     *
     * @param image
     */
    private void loadClientImage(ClientImagePreview image) {
        final Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), ImageService.class);
        intent.putExtra(ImageService.RECEIVER_KEY, imageResultReceiver);
        intent.putExtra(ImageService.COMMAND_KEY, ImageService.Commands.LOAD_IMAGE_DATA);
        intent.putExtra(ImageService.REQUEST_KEY, image.getId());
        getActivity().startService(intent);
    }

    /**
     *
     * @param image
     */
    private void loadImage(ClientImage image) {
        final Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), ImageService.class);
        intent.putExtra(ImageService.RECEIVER_KEY, imageResultReceiver);
        intent.putExtra(ImageService.COMMAND_KEY, ImageService.Commands.LOAD_IMAGE);
        intent.putExtra(ImageService.IMAGE_KEY, image);
        getActivity().startService(intent);
    }
}
