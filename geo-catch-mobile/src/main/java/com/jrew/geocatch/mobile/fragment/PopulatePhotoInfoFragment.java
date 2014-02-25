package com.jrew.geocatch.mobile.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import com.jrew.geocatch.mobile.model.PostponedImage;
import com.jrew.geocatch.mobile.model.UploadImage;
import com.jrew.geocatch.mobile.reciever.ServiceResultReceiver;
import com.jrew.geocatch.mobile.service.DomainInfoService;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.mobile.service.SavePostponedPhotoTask;
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
public class PopulatePhotoInfoFragment extends SherlockFragment implements LocationListener {

    /**
     *
     */
    private interface ShareSectionPositions {

        /** **/
        public static final int PUBLIC_VALUE_POSITION = 0;

        /** **/
        public static final int PRIVATE_VALUE_POSITION = 1;
    }

    /** **/
    private static final Double IMAGE_VIEW_SCALE_SIZE = 0.15d;

    /** **/
    private ServiceResultReceiver resultReceiver;

    /** **/
    private DomainPropertyView fishTypeView, fishingToolView, fishingBaitView;

    /** **/
    private View layout;

    /** **/
    private PrePopulatedEditText descriptionView;

    /** **/
    private ProgressDialog dialog;

    /** **/
    private Bundle imageBundle;

    /** **/
    private Bitmap bitmap;

    /** **/
    private LocationManager locationManager;

    /** **/
    private Location currentLocation;

    /** **/
    private Spinner shareSection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Action bar subtitle
        ActionBarUtil.initActionBar(ActionBar.NAVIGATION_MODE_STANDARD, getActivity());
        ActionBarUtil.setActionBarSubtitle(R.string.populatePhotoInfoFragmentLabel, getActivity());

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        currentLocation = null;
        ActionBarUtil.initLocationProcessingStatusArea(getActivity());

        LayoutUtil.showFragmentContainer(getActivity());

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

                    case ImageService.ResultStatus.UPLOAD_IMAGE_FINISHED:

                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }

                        if (resultData.getBoolean(ImageService.RESULT_KEY)) {
                            FragmentSwitcherHolder.getFragmentSwitcher().showUploadedPhotosFragment();
                        } else {
                            AlertDialog alert = buildUploadingErrorAlert();
                            alert.show();
                        }

                        break;

                    case ImageService.ResultStatus.ERROR:
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }

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

        shareSection = (Spinner) layout.findViewById(R.id.shareSectionSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, getResources().getStringArray(R.array.shareSectionOptions));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shareSection.setAdapter(adapter);
        shareSection.setSaveEnabled(false);

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();

        //currentLocation = LocationManagerHolder.getLocationManager().getCurrentLocation();
        if (!CommonUtil.isGPSEnabled(getActivity())) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getResources().getString(R.string.noGPSConnectionWarning))
                    .setCancelable(false)
                    .setNegativeButton(getResources().getString(R.string.noGPSConnectionLaterChoice), new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                        }
                    })
                    .setPositiveButton(getResources().getString(R.string.noGPSConnectionYesChoice), new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    });

            final AlertDialog alert = builder.create();
            alert.show();
        } else {
            if (currentLocation == null) {
                ActionBarUtil.statusAreaHandleLocationLoading(getActivity());
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_populate_photo_info, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int pressedMenuItemId = item.getItemId();
        switch (pressedMenuItemId) {
            case R.id.backMenuOption:
                FragmentSwitcherHolder.getFragmentSwitcher().popBackStack();
                break;

            case R.id.uploadPhotoMenuOption:
                if (!fishTypeView.isTextPopulated()) {
                    showFishNotPopulatedWarning();
                }
                else if (currentLocation == null) {
                    showNoLocationDetectedWarning();
                } else {
                    imageBundle = prepareUploadBundle(layout, bitmap);
                    dialog =  DialogUtil.createProgressDialog(getActivity(), R.string.imageUploadingMessage);
                    ServiceUtil.callUploadImageService(imageBundle, resultReceiver, getActivity());
                }
                break;

            case R.id.postponePhotoUploadMenuOption:
                if (!fishTypeView.isTextPopulated()) {
                    showFishNotPopulatedWarning();
                } else if (currentLocation == null) {
                    showNoLocationDetectedWarning();
                } else {
                    dialog = DialogUtil.createProgressDialog(getActivity(), R.string.postponedImageSavingMessage);
                    postponePhotoUpload();
                }
                break;
        }

        return true;
    }

    public Bundle prepareUploadBundle(View layout, Bitmap image) {

        UploadImage uploadImage = prepareUploadData(layout, image);

        Bundle bundle = new Bundle();
        bundle.putSerializable(ImageService.IMAGE_KEY, uploadImage);

        return bundle;
    }

    /**
     *
     * @param layout
     * @param bitmap
     * @return
     */
    public UploadImage prepareUploadData(View layout, Bitmap bitmap) {

        UploadImage uploadImage = prepareImageData(layout);

        byte[] bitmapData = ConversionUtil.marshallBitmap(bitmap);
        String file = new String(Base64.encodeBase64(bitmapData));
        uploadImage.setFile(file);

        return uploadImage;
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
        descriptionView = (PrePopulatedEditText) layout.findViewById(R.id.uploadImageDescription);
        uploadImage.setDescription(descriptionView.getValue());

        // Latitude
        uploadImage.setLatitude(currentLocation.getLatitude());

        // Longitude
        uploadImage.setLongitude(currentLocation.getLongitude());

        // Privacy level
        uploadImage.setPrivacyLevel(getSelectedPrivacyLevel());

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
     * @return
     */
    private UploadImage.PrivacyLevel getSelectedPrivacyLevel() {

        UploadImage.PrivacyLevel privacyLevel;
        if (shareSection.getSelectedItemPosition() == ShareSectionPositions.PUBLIC_VALUE_POSITION) {
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
                postponePhotoUpload();
            }
        });

        return  builder.create();
    }

    /**
     *
     */
    private void postponePhotoUpload() {
        PostponedImage postponedImage = new PostponedImage();
        postponedImage.setBitmap(bitmap);
        UploadImage uploadImage = prepareUploadData(layout, bitmap);
        postponedImage.setUploadImage(uploadImage);
        SavePostponedPhotoTask task = new SavePostponedPhotoTask(getActivity(), dialog);
        task.execute(postponedImage);
    }

    /**
     *
     */
    private void showNoLocationDetectedWarning() {
        Context context = getActivity();
        CharSequence text = getResources().getString(R.string.locationLoadingWarning);
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     *
     */
    private void showFishNotPopulatedWarning() {
        Context context = getActivity();
        CharSequence text = getResources().getString(R.string.fishNotPopulatedWarning);
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location.getAccuracy()  < getResources().getInteger(R.config.locationDetectionAccuracy)) {
            currentLocation = location;
            ActionBarUtil.statusAreaHandleLocationDetection(getActivity());
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String providerName) {
        if (LocationManager.GPS_PROVIDER.equalsIgnoreCase(providerName)) {
            ActionBarUtil.statusAreaHandleLocationLoading(getActivity());
        }
    }

    @Override
    public void onProviderDisabled(String providerName) {
        if (LocationManager.GPS_PROVIDER.equalsIgnoreCase(providerName)) {
            ActionBarUtil.initLocationProcessingStatusArea(getActivity());
        }
    }

    @Override
    public void onStop() {
        locationManager.removeUpdates(this);
        super.onStop();
    }

    @Override
    public void onStart() {
        if (currentLocation == null) {
            super.onStart();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
    }
}
