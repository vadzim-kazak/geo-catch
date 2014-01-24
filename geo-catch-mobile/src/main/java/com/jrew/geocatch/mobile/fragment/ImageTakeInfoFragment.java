package com.jrew.geocatch.mobile.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.activity.MainActivity;
import com.jrew.geocatch.mobile.model.UploadImage;
import com.jrew.geocatch.mobile.reciever.DomainInfoServiceResultReceiver;
import com.jrew.geocatch.mobile.reciever.ServiceResultReceiver;
import com.jrew.geocatch.mobile.service.DomainInfoService;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.mobile.util.ActionBarHolder;
import com.jrew.geocatch.mobile.util.FileUtil;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;
import com.jrew.geocatch.mobile.util.ImageUploadKeys;
import com.jrew.geocatch.mobile.view.DomainPropertyView;
import com.jrew.geocatch.mobile.view.PrePopulatedEditText;
import com.jrew.geocatch.web.model.DomainProperty;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 13.11.13
 * Time: 15:06
 * To change this template use File | Settings | File Templates.
 */
public class ImageTakeInfoFragment extends SherlockFragment implements LocationListener {

    /** **/
    private static final Double IMAGE_VIEW_SCALE_SIZE = 0.15d;

    /** **/
    private double latitude;

    /** **/
    private double longitude;

    /** **/
    private ServiceResultReceiver resultReceiver;

    /** **/
    private DomainPropertyView fishTypeView, fishingToolView, fishingBaitView;

    /** **/
    private View layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Action bar subtitle
        ActionBar actionBar = ActionBarHolder.getActionBar();
        actionBar.setSubtitle(getResources().getString(R.string.photoInfoLabel));

        layout = inflater.inflate(R.layout.image_take_info_fragment, container, false);

        ImageView imageThumbnail = (ImageView) layout.findViewById(R.id.imageThumbnail);

        final Bundle fragmentData = getArguments();

        if (fragmentData != null && !fragmentData.isEmpty()) {
            Bitmap image = (Bitmap) fragmentData.getParcelable("bmp");

            // Resize image view
            WindowManager windowManager = getSherlockActivity().getWindowManager();
            Rect displaySize = new Rect();
            windowManager.getDefaultDisplay().getRectSize(displaySize);

            int imageThumbnailViewHeight = (int) (displaySize.height() * IMAGE_VIEW_SCALE_SIZE);
            int imageThumbnailViewWidth = (int) ((double) image.getWidth() / image.getHeight() * imageThumbnailViewHeight);
            LinearLayout.LayoutParams layoutParams =
                    new LinearLayout.LayoutParams(imageThumbnailViewWidth, imageThumbnailViewHeight);
            imageThumbnail.setLayoutParams(layoutParams);

            imageThumbnail.setImageBitmap(image);
        }

        resultReceiver = new ServiceResultReceiver(new Handler());
        resultReceiver.setReceiver(new ServiceResultReceiver.Receiver() {

            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {

                switch (resultCode) {
                    case ImageService.ResultStatus.UPLOAD_IMAGE_FINISHED:
                        FragmentSwitcherHolder.getFragmentSwitcher().showMapFragment();
                        break;

                    case ImageService.ResultStatus.ERROR:
                        CharSequence text = "Image uploading error...";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(getActivity(), text, duration);
                        toast.show();
                        FragmentSwitcherHolder.getFragmentSwitcher().showMapFragment();
                    break;
                }
            }

        });

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, this);

        //Populate
        fishTypeView = (DomainPropertyView) layout.findViewById(R.id.fishTypeView);
        loadDomainInfo(fishTypeView, DomainInfoService.DomainInfoType.FISH);

        fishingToolView = (DomainPropertyView) layout.findViewById(R.id.fishingToolView);
        loadDomainInfo(fishingToolView, DomainInfoService.DomainInfoType.FISHING_TOOL);

        fishingBaitView = (DomainPropertyView) layout.findViewById(R.id.fishingBaitView);
        loadDomainInfo(fishingBaitView, DomainInfoService.DomainInfoType.BAIT);

        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_photo_info, menu);
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
                if (!isLocationDetected()) {
                    Context context = getActivity();
                    CharSequence text = "Wait for location loading...";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    final Bundle fragmentData = getArguments();
                    Bitmap image = (Bitmap) fragmentData.getParcelable("bmp");
                    Bundle imageBundle = prepareUploadData(layout, image);
                    uploadImage(imageBundle);
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

        File cacheDir = getActivity().getCacheDir();
        bundle.putString(ImageUploadKeys.FILE, FileUtil.writeBitmapToFileSystem(image, cacheDir, getResources()));

        UploadImage uploadImage = prepareImageData(layout);
        bundle.putSerializable(ImageUploadKeys.IMAGE, uploadImage);

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
        uploadImage.setLatitude(latitude);

        // Longitude
        uploadImage.setLongitude(longitude);

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

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {}

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
    }

    /**
     *
     * @return
     */
    private boolean isLocationDetected() {
        if (latitude != 0 && longitude != 0) {
            return true;
        }

        return false;
    }

    /**
     *
     * @param bundle
     */
    public void uploadImage(Bundle bundle) {
        final Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), ImageService.class);
        intent.putExtra(ImageService.RECEIVER_KEY, resultReceiver);
        intent.putExtra(ImageService.COMMAND_KEY, ImageService.Commands.UPLOAD_IMAGE);
        intent.putExtra(ImageService.REQUEST_KEY, bundle);
        getActivity().startService(intent);
    }

    /**
     *
     */
    public void loadDomainInfo(DomainPropertyView domainPropertyView, int domainInfoType) {

        Bundle bundle = new Bundle();

        DomainInfoServiceResultReceiver receiver = new DomainInfoServiceResultReceiver(new Handler(), domainPropertyView);

        String locale = Locale.getDefault().getLanguage();
        bundle.putString(DomainInfoService.LOCALE_KEY, locale);
        bundle.putInt(DomainInfoService.DOMAIN_INFO_TYPE_KEY, domainInfoType);

        final Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), DomainInfoService.class);
        intent.putExtra(DomainInfoService.REQUEST_KEY, bundle);
        intent.putExtra(DomainInfoService.RECEIVER_KEY, receiver);
        getActivity().startService(intent);
    }
}
