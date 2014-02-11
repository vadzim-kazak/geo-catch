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
import com.jrew.geocatch.mobile.util.*;
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

    /**
     *
     */
    public enum BrowsingMode {

       /** **/
       FOREIGN_PHOTO_BROWSING,

       /** **/
       OWN_PHOTO_BROWSING
    }

    /** **/
    private ServiceResultReceiver imageResultReceiver;

    /** **/
   // private ProgressDialog progressDialog;

    /** **/
    private ClientImage clientImage;

    /** **/
    private BrowsingMode browsingMode = BrowsingMode.FOREIGN_PHOTO_BROWSING;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Action bar subtitle
        ActionBarUtil.initActionBar(ActionBar.NAVIGATION_MODE_STANDARD, getActivity());
        ActionBarUtil.setActionBarSubtitle(R.string.photoBrowsingFragmentLabel, getActivity());

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

      //  progressDialog = DialogUtil.createProgressDialog(getActivity());

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
                        ServiceUtil.callLoadImageService(clientImage, imageResultReceiver, getActivity());
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

                        //progressDialog.dismiss();
                        break;

                    case ImageService.ResultStatus.DELETE_IMAGE_FINISHED:
                        //progressDialog.hide();
                        FragmentSwitcherHolder.getFragmentSwitcher().showUploadedPhotosFragment();
                        break;

                    case ImageService.ResultStatus.ERROR:
                       // progressDialog.hide();
                        break;
                }
            }

        });

        Bundle fragmentData = getArguments();
        if (fragmentData != null && !fragmentData.isEmpty()) {

            if (fragmentData.containsKey(BrowsingMode.OWN_PHOTO_BROWSING.toString())) {
                browsingMode = BrowsingMode.OWN_PHOTO_BROWSING;
            }

            ClientImagePreview image = (ClientImagePreview) fragmentData.getSerializable(ImageService.IMAGE_KEY);
            ServiceUtil.callLoadImageDataService(image, imageResultReceiver, getActivity());
        }

        return photoBrowsingLayout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_photo_browsing, menu);

        switch (browsingMode) {
            case FOREIGN_PHOTO_BROWSING:
                // Remove delete photo menu option
                menu.removeItem(R.id.deleteImageMenuOption);
                break;

            case  OWN_PHOTO_BROWSING:
                // Remove report photo menu option
                menu.removeItem(R.id.reportImageMenuOption);
                break;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int pressedMenuItemId = item.getItemId();
        switch (pressedMenuItemId) {
            case R.id.backMenuOption:
                FragmentSwitcherHolder.getFragmentSwitcher().popBackStack();
                break;

            case R.id.deleteImageMenuOption:
                if (clientImage != null) {
                   // progressDialog.show();

                    Bundle requestBundle = new Bundle();
                    requestBundle.putLong(ImageService.IMAGE_ID_KEY, clientImage.getId());
                    requestBundle.putString(ImageService.DEVICE_ID_KEY, CommonUtils.getDeviceId(getActivity()));
                    ServiceUtil.callDeleteImageService(requestBundle, imageResultReceiver, getActivity());
                }
                break;

            case R.id.reportImageMenuOption:

                break;
        }

        return true;
    }
}
