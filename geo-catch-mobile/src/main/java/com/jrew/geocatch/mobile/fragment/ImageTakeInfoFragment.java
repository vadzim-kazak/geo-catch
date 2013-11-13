package com.jrew.geocatch.mobile.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.activity.MainActivity;
import com.jrew.geocatch.mobile.reciever.ServiceResultReceiver;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.mobile.util.CommonUtils;
import com.jrew.geocatch.mobile.util.ImageUploadKeys;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 13.11.13
 * Time: 15:06
 * To change this template use File | Settings | File Templates.
 */
public class ImageTakeInfoFragment extends Fragment implements LocationListener {

    /** **/
    private double latitude;

    /** **/
    private double longitude;

    /** **/
    private boolean isLocationDetected;

    /** **/
    public ServiceResultReceiver resultReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        isLocationDetected = false;

        final View result = inflater.inflate(R.layout.image_take_info_fragment, container, false);

        ImageView imageThumbnail = (ImageView) result.findViewById(R.id.imageThumbnail);

        final Bundle fragmentData = getArguments();

        if (fragmentData != null && !fragmentData.isEmpty()) {
            Bitmap image = (Bitmap) fragmentData.getParcelable("bmp");
            imageThumbnail.setImageBitmap(image);
        }

        Button uploadButton = (Button) result.findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (!isLocationDetected) {
                    Context context = getActivity();
                    CharSequence text = "Wait for location loading...";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                } else {
                    Bitmap image = (Bitmap) fragmentData.getParcelable("bmp");
                    Bundle imageBundle = collectInfo(result, image);
                    uploadImage(imageBundle);
                }
            }
        });

        resultReceiver = new ServiceResultReceiver(new Handler());
        resultReceiver.setReceiver(new ServiceResultReceiver.Receiver() {

            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {

                MainActivity activity = (MainActivity) getActivity();

                switch (resultCode) {
                    case ImageService.ResultStatus.UPLOAD_IMAGE_FINISHED:
                        activity.getFragmentSwitcher().showMapFragment();
                        break;

                    case ImageService.ResultStatus.ERROR:
                        CharSequence text = "Image uploading error...";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(activity, text, duration);
                        toast.show();
                        activity.getFragmentSwitcher().showMapFragment();
                    break;
                }
            }

        });

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f, this);

        return result;
    }

    public Bundle collectInfo(View layout, Bitmap image) {

        Bundle bundle = new Bundle();

        try {

            //Image path
            File cacheDir = getActivity().getCacheDir();
            File uploadingImage = new File(cacheDir, "geoCatchUpload.png");
            if (uploadingImage.exists()) {
                uploadingImage.delete();
            }

            FileOutputStream out = new FileOutputStream(uploadingImage);
            image.compress(Bitmap.CompressFormat.PNG, 70, out);
            out.flush();
            out.close();
            bundle.putString(ImageUploadKeys.FILE, uploadingImage.getAbsolutePath());


            // User Id
            String deviceId = Settings.Secure.getString(getActivity().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            bundle.putString(ImageUploadKeys.USER_ID, deviceId);

            // Description
            AutoCompleteTextView autoCompleteTextView =
                    (AutoCompleteTextView) layout.findViewById(R.id.autoCompleteTextView1);
            bundle.putString(ImageUploadKeys.DESCRIPTION, autoCompleteTextView.getText().toString());

            // Latitude
            bundle.putString(ImageUploadKeys.LATITUDE, Double.toString(latitude));

            // Longitude
            bundle.putString(ImageUploadKeys.LONGITUDE, Double.toString(longitude));

            // Date
            DateFormat formatter = new SimpleDateFormat(getResources()
                    .getString(R.config.repositoryUploadImagesDateFormat));
            Date currentDate = new Date();
            bundle.putString(ImageUploadKeys.DATE, formatter.format(currentDate));

        } catch (Exception exception) {
            Log.e(CommonUtils.getDebugTag(getResources()), exception.getMessage());
        }

        return bundle;
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
        isLocationDetected = true;
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
}
