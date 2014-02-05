package com.jrew.geocatch.mobile.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.*;
import android.widget.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.activity.MainActivity;
import com.jrew.geocatch.mobile.dao.PostponedImageManager;
import com.jrew.geocatch.mobile.model.PostponedImage;
import com.jrew.geocatch.mobile.model.UploadImage;
import com.jrew.geocatch.mobile.reciever.ServiceResultReceiver;
import com.jrew.geocatch.mobile.service.DomainInfoService;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.mobile.util.*;
import com.jrew.geocatch.mobile.view.DomainPropertyView;
import com.jrew.geocatch.mobile.view.PrePopulatedEditText;
import com.jrew.geocatch.web.model.DomainProperty;
import org.apache.commons.codec.binary.Base64;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 13.11.13
 * Time: 15:06
 * To change this template use File | Settings | File Templates.
 */
public class PopulatePhotoInfoFragment extends SherlockFragment {

    /** **/
    private static final Double IMAGE_VIEW_SCALE_SIZE = 0.15d;

    /** **/
    private ServiceResultReceiver resultReceiver;

    /** **/
    private DomainPropertyView fishTypeView, fishingToolView, fishingBaitView;

    /** **/
    private View layout;

    /** **/
    private Location currentLocation;

    /** **/
    private ProgressDialog progressDialog;

    /** **/
    private Bundle imageBundle;

    /** **/
    private Bitmap bitmap;

    /** **/
    private UploadImage uploadImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Action bar subtitle
        ActionBar actionBar = ActionBarHolder.getActionBar();
        actionBar.setSubtitle(getResources().getString(R.string.populatePhotoInfoFragmentLabel));

        layout = inflater.inflate(R.layout.populate_photo_info_fragment, container, false);

        ImageView imageThumbnail = (ImageView) layout.findViewById(R.id.imageThumbnail);

        final Bundle fragmentData = getArguments();

        if (fragmentData != null && !fragmentData.isEmpty()) {
            bitmap = (Bitmap) fragmentData.getParcelable("bmp");

            // Resize bitmap view
            WindowManager windowManager = getSherlockActivity().getWindowManager();
            Display display = windowManager.getDefaultDisplay();

            int imageThumbnailViewHeight = (int) (display.getHeight() * IMAGE_VIEW_SCALE_SIZE);
            int imageThumbnailViewWidth = (int) ((double) bitmap.getWidth() / bitmap.getHeight() * imageThumbnailViewHeight);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(imageThumbnailViewWidth, imageThumbnailViewHeight);
            imageThumbnail.setLayoutParams(layoutParams);

            imageThumbnail.setImageBitmap(bitmap);
        }

        resultReceiver = new ServiceResultReceiver(new Handler());
        resultReceiver.setReceiver(new ServiceResultReceiver.Receiver() {

            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {

                switch (resultCode) {
                    case ImageService.ResultStatus.UPLOAD_IMAGE_STARTED:
                        progressDialog = DialogUtil.createProgressDialog(getActivity());
                        progressDialog.show();
                        break;

                    case ImageService.ResultStatus.UPLOAD_IMAGE_FINISHED:
                        progressDialog.hide();
                        FragmentSwitcherHolder.getFragmentSwitcher().showOwnPhotosFragment();
                        break;

                    case ImageService.ResultStatus.ERROR:
                        progressDialog.hide();
                        AlertDialog alert = buildUploadingErrorAlert();
                        alert.show();
                        break;
                }
            }

        });

        //Populate
        fishTypeView = (DomainPropertyView) layout.findViewById(R.id.fishTypeView);
        fishTypeView.loadDomainProperties(DomainInfoService.DomainInfoType.FISH);

        fishingToolView = (DomainPropertyView) layout.findViewById(R.id.fishingToolView);
        fishingToolView.loadDomainProperties(DomainInfoService.DomainInfoType.FISHING_TOOL);

        fishingBaitView = (DomainPropertyView) layout.findViewById(R.id.fishingBaitView);
        fishingBaitView.loadDomainProperties(DomainInfoService.DomainInfoType.BAIT);

        MainActivity parentActivity = (MainActivity) getActivity();
        currentLocation = parentActivity.getCurrentLocation();

        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_populate_photo_info, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int pressedMenuItemId = item.getItemId();
        FragmentSwitcher fragmentSwitcher = FragmentSwitcherHolder.getFragmentSwitcher();
        switch (pressedMenuItemId) {
            case R.id.backMenuOption:
                getSherlockActivity().getSupportFragmentManager().popBackStack();
                break;

            case R.id.uploadPhotoMenuOption:
                if (currentLocation == null) {
                    Context context = getActivity();
                    CharSequence text = getResources().getString(R.string.locationLoadingWarning);
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    imageBundle = prepareUploadData(layout, bitmap);
                    ServiceUtil.callUploadImageService(imageBundle, resultReceiver, getActivity());
                }
                break;
        }

        return true;
    }

    /**
     *
     * @param layout
     * @param image
     * @return
     */
    public Bundle prepareUploadData(View layout, Bitmap image) {

        Bundle bundle = new Bundle();

        uploadImage = prepareImageData(layout);

        byte[] fileData = ConversionUtil.marshallBitmap(image);

        String file = new String(Base64.encodeBase64(fileData));//.encodeToString(, Base64.URL_SAFE);
        uploadImage.setFile(file);

        bundle.putSerializable(ImageService.IMAGE_KEY, uploadImage);

        return bundle;
    }

    /**
     *
     * @param layout
     * @return
     */
    private UploadImage prepareImageData(View layout) {
        UploadImage uploadImage = new UploadImage();

        // Device Id
        String deviceId = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        uploadImage.setDeviceId(deviceId);

        // Description
        PrePopulatedEditText descriptionView = (PrePopulatedEditText) layout.findViewById(R.id.uploadImageDescription);
        uploadImage.setDescription(descriptionView.getValue());

        // Latitude
        uploadImage.setLatitude(currentLocation.getLatitude());

        // Longitude
        uploadImage.setLongitude(currentLocation.getLongitude());

        // Privacy level
        uploadImage.setPrivacyLevel(getSelectedPrivacyLevel(layout));

        // Domain properties
        uploadImage.setDomainProperties(getPopulatedDomainProperties());

        // Date
        DateFormat formatter = new SimpleDateFormat(getResources()
                .getString(R.config.repositoryUploadImagesDateFormat));
        Date currentDate = new Date();
        uploadImage.setDate(formatter.format(currentDate));

        return uploadImage;
    }

    /**
     *
     * @param layout
     * @return
     */
    private UploadImage.PrivacyLevel getSelectedPrivacyLevel(View layout) {

        RadioGroup radioGroup = (RadioGroup) layout.findViewById(R.id.privacyLevel);
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) radioGroup.findViewById(checkedRadioButtonId);

        UploadImage.PrivacyLevel privacyLevel;
        if (radioButton.getId() == R.id.sharePublic) {
            privacyLevel = UploadImage.PrivacyLevel.PUBLIC;
        } else {
            privacyLevel = UploadImage.PrivacyLevel.PRIVATE;
        }

        return privacyLevel;
    }

    /**
     *
     * @return
     */
    List<DomainProperty> getPopulatedDomainProperties() {

        List<DomainProperty> domainProperties = new ArrayList<DomainProperty>();

        DomainProperty selectedDomainProperty = fishTypeView.getSelectedDomainProperty(true);
        if (selectedDomainProperty != null) {
            domainProperties.add(selectedDomainProperty);
        }

        selectedDomainProperty = fishingToolView.getSelectedDomainProperty(true);
        if (selectedDomainProperty != null) {
            domainProperties.add(selectedDomainProperty);
        }

        selectedDomainProperty = fishingBaitView.getSelectedDomainProperty(true);
        if (selectedDomainProperty != null) {
            domainProperties.add(selectedDomainProperty);
        }

        return domainProperties;
    }

    /**
     *
     * @return
     */
    private AlertDialog buildUploadingErrorAlert() {

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setIcon(R.drawable.emo_im_sad)
                .setTitle(R.string.imageUploadingError);

        // Add the buttons
        builder.setNegativeButton(R.string.imageUploadingRetry, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ServiceUtil.callUploadImageService(imageBundle, resultReceiver, getActivity());
            }
        });

        builder.setPositiveButton(R.string.imageUploadingLater, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                PostponedImage postponedImage = new PostponedImage();
                postponedImage.setBitmap(bitmap);
                postponedImage.setUploadImage(uploadImage);
                PostponedImageManager.persistPostponedImage(getActivity(), postponedImage);
                FragmentSwitcherHolder.getFragmentSwitcher().showOwnPhotosFragment();
            }
        });

        return  builder.create();
    }
}
