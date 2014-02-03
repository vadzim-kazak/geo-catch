package com.jrew.geocatch.mobile.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.mobile.reciever.ServiceResultReceiver;
import com.jrew.geocatch.mobile.util.ActionBarHolder;
import com.jrew.geocatch.mobile.util.DialogUtil;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;
import com.jrew.geocatch.mobile.util.LayoutUtil;
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
public class PhotoBrowsingFragment extends SherlockFragment {

    /** **/
    private ServiceResultReceiver imageResultReceiver;

    /** **/
    private ProgressDialog progressDialog;

    /** **/
    private ClientImage clientImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Action bar subtitle
        ActionBar actionBar = ActionBarHolder.getActionBar();
        actionBar.setSubtitle(getResources().getString(R.string.photoBrowsingFragmentLabel));

        setHasOptionsMenu(true);

        progressDialog = DialogUtil.createProgressDialog(getActivity());

        View photoBrowsingLayout = inflater.inflate(R.layout.photo_browsing_fragment, container, false);

        final ImageView imageView = (ImageView) photoBrowsingLayout.findViewById(R.id.imageView);

        // Uploading photo date
        final LinearLayout uploadingDateLayout = (LinearLayout) photoBrowsingLayout.findViewById(R.id.uploadingDateLayout);
        final TextView date = (TextView) photoBrowsingLayout.findViewById(R.id.uploadingDateTextView);
        final DateFormat browsingDateFormat = new SimpleDateFormat(getResources().getString(R.config.mobileDateFormat));

        // Photo description
        final LinearLayout descriptionLayout = (LinearLayout) photoBrowsingLayout.findViewById(R.id.descriptionLayout);
        final TextView descriptionView = (TextView) photoBrowsingLayout.findViewById(R.id.photoDescription);

        final LinearLayout fishDomainPropertyTag = (LinearLayout) photoBrowsingLayout.findViewById(R.id.fishDomainPropertyTag);
        final TextView fishTextView = (TextView) photoBrowsingLayout.findViewById(R.id.fishTextView);

        final LinearLayout toolDomainPropertyTag = (LinearLayout) photoBrowsingLayout.findViewById(R.id.toolDomainPropertyTag);
        final TextView toolTextView = (TextView) photoBrowsingLayout.findViewById(R.id.toolTextView);

        final LinearLayout baitDomainPropertyTag = (LinearLayout) photoBrowsingLayout.findViewById(R.id.baitDomainPropertiesView);
        final TextView baitTextView = (TextView) photoBrowsingLayout.findViewById(R.id.baitTextView);

        imageResultReceiver = new ServiceResultReceiver(new Handler());
        imageResultReceiver.setReceiver(new ServiceResultReceiver.Receiver() {

            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {

                switch (resultCode) {

                    case ImageService.ResultStatus.LOAD_IMAGE_DATA_FINISHED:

                        clientImage = (ClientImage) resultData.getSerializable(ImageService.RESULT_KEY);

                        // Load image
                        loadImage(clientImage);
                        break;

                    case ImageService.ResultStatus.LOAD_IMAGE_FINISHED:

                        // domain properties
                        List<DomainProperty> domainProperties = clientImage.getDomainProperties();
                        for (DomainProperty domainProperty : domainProperties) {
                            long domainPropertyType = domainProperty.getType();
                            if (domainPropertyType == 1) {
                                fishDomainPropertyTag.setVisibility(View.VISIBLE);
                                fishTextView.setText(domainProperty.getValue());
                            } else if (domainPropertyType == 2) {
                                toolDomainPropertyTag.setVisibility(View.VISIBLE);
                                toolTextView.setText(domainProperty.getValue());
                            } else if (domainPropertyType == 3) {
                                baitDomainPropertyTag.setVisibility(View.VISIBLE);
                                baitTextView.setText(domainProperty.getValue());
                            }
                        }


                        // Photo
                        Bitmap image = (Bitmap) resultData.get(ImageService.RESULT_KEY);
                        imageView.setImageBitmap(image);

                        Display display = getActivity().getWindowManager().getDefaultDisplay();
                        double scaleFactor = LayoutUtil.getViewWidthScaleFactor(display.getWidth(), image.getWidth(), 0);
                        imageView.setLayoutParams(new LinearLayout.LayoutParams((int)
                            (image.getWidth() * scaleFactor), (int) (image.getHeight() * scaleFactor)));

                        // date
                        date.setText(browsingDateFormat.format(clientImage.getDate()));
                        uploadingDateLayout.setVisibility(View.VISIBLE);

                        // description
                        String description = clientImage.getDescription();
                        if (description != null && description.length() > 0) {
                            descriptionView.setText(description);
                            descriptionLayout.setVisibility(View.VISIBLE);
                        }

                        progressDialog.dismiss();
                        break;
                }
            }

        });

        Bundle fragmentData = getArguments();
        if (fragmentData != null && !fragmentData.isEmpty()) {
            ClientImagePreview image = (ClientImagePreview) fragmentData.getSerializable(ImageService.IMAGE_KEY);
            loadClientImage(image);
        }

        return photoBrowsingLayout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_photo_browsing, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int pressedMenuItemId = item.getItemId();

        FragmentSwitcher fragmentSwitcher = FragmentSwitcherHolder.getFragmentSwitcher();
        switch (pressedMenuItemId) {
            case R.id.proceedToMapMenuOption:
                fragmentSwitcher.showMapFragment();
                break;

        }

        return true;
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