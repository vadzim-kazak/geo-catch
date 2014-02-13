package com.jrew.geocatch.mobile.fragment;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Watson;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.activity.MainActivity;
import com.jrew.geocatch.mobile.listener.MarkerOnClickListener;
import com.jrew.geocatch.mobile.model.ImageMarkerPair;
import com.jrew.geocatch.mobile.reciever.ImageServiceResultReceiver;
import com.jrew.geocatch.mobile.util.*;
import com.jrew.geocatch.web.model.ClientImagePreview;
import com.jrew.geocatch.web.model.ViewBounds;
import com.jrew.geocatch.web.model.criteria.SearchCriteria;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 28.10.13
 * Time: 18:03
 * To change this template use File | Settings | File Templates.
 *
 */
public class MapFragment extends SupportMapFragment implements Watson.OnCreateOptionsMenuListener,
        Watson.OnOptionsItemSelectedListener {

    /** **/
    private GoogleMap googleMap;

    /** **/
    private Map<Long, ImageMarkerPair> imageMarkerPairs;

    /** **/
    public ImageServiceResultReceiver imageResultReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        ActionBarUtil.initPopulatePhotoInfoActionBar(ActionBar.NAVIGATION_MODE_STANDARD, getActivity());
        ActionBarUtil.setActionBarSubtitle(R.string.mapFragmentLabel, getActivity());

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        View result = super.onCreateView(inflater, container, savedInstanceState);

        if (WebUtil.isNetworkAvailable(getActivity())) {

            LayoutUtil.showFragmentContainer(getActivity());

            if (googleMap == null) {

                googleMap = getMap();

                int mapType = Integer.parseInt(getResources().getString(R.config.mapType));
                googleMap.setMapType(mapType);

                MainActivity parentActivity = (MainActivity) getActivity();
                Location currentLocation = null; //parentActivity.getCurrentLocation();
                if (currentLocation != null) {

                    CameraUpdate center =  CameraUpdateFactory.newLatLng(new LatLng(currentLocation.getLatitude(),
                            currentLocation.getLongitude()));

                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(
                            Integer.parseInt(getResources().getString(R.config.cameraInitialZoom)));

                    googleMap.moveCamera(center);
                    googleMap.animateCamera(zoom);
                }

                imageMarkerPairs = new HashMap<Long, ImageMarkerPair>();

                googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

                    @Override
                    public void onCameraChange(CameraPosition cameraPosition) {

                        // Get current view bounds
                        LatLngBounds latLngBounds = getLatLngBounds();

                        // Remove invisible markers
                        //removeInvisibleMarkers(latLngBounds);

                        // Load new images for view bounds
                        loadImages(latLngBounds);
                    }
                });

                /** Set custom on marker click listener  **/
                MarkerOnClickListener markerOnclickListener = new MarkerOnClickListener(imageMarkerPairs, this);
                googleMap.setOnMarkerClickListener(markerOnclickListener);

                imageResultReceiver = new ImageServiceResultReceiver(new Handler(), this);

            } else {
                clearMarkers();
                loadImages(getLatLngBounds());
            }
        } else {
            LayoutUtil.showNoConnectionLayout(getActivity(), R.string.noNetworkConnectionError);
        }

        return result;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_map, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int pressedMenuItemId = item.getItemId();

        FragmentSwitcher fragmentSwitcher = FragmentSwitcherHolder.getFragmentSwitcher();
        switch (pressedMenuItemId) {
            case R.id.viewSettingsMenuOption:
                fragmentSwitcher.showMapSettingsFragment();
                break;

            case R.id.takeImageMenuOption:
                fragmentSwitcher.showGetPhotoFragment();
                break;

            case R.id.ownImagesMenuOption:
                fragmentSwitcher.showUploadedPhotosFragment();
                break;
        }

        return true;
    }

    /**
     *
     * @param latLngBounds
     */
    private void removeInvisibleMarkers(LatLngBounds latLngBounds) {

        for (Map.Entry<Long, ImageMarkerPair> entry : imageMarkerPairs.entrySet()) {
            ImageMarkerPair imageMarkerPair = entry.getValue();
            Marker currentMarker = imageMarkerPair.getMarker();
            if (latLngBounds.contains(currentMarker.getPosition())) {
                currentMarker.remove();
                ClientImagePreview imagePreview = imageMarkerPair.getImage();
                imageMarkerPairs.remove(imagePreview.getId());
            }
        }
    }

    /**
     *
     * @param latLngBounds
     */
    private void loadImages(LatLngBounds latLngBounds) {

        SearchCriteria searchCriteria = SearchCriteriaHolder.getSearchCriteria();

        // DeviceId
        String deviceId = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        searchCriteria.setDeviceId(deviceId);

        ViewBounds viewBounds = new ViewBounds(latLngBounds.northeast.latitude,
                latLngBounds.northeast.longitude,
                latLngBounds.southwest.latitude,
                latLngBounds.southwest.longitude);

        searchCriteria.setViewBounds(viewBounds);

        ServiceUtil.callLoadImagesService(searchCriteria, imageResultReceiver, getActivity());
    }

    /**
     *
     */
    private void clearMarkers() {
        googleMap.clear();
        imageMarkerPairs.clear();
    }

    /**
     *
     * @return
     */
    public Map<Long, ImageMarkerPair> getImageMarkerPairs() {
        return imageMarkerPairs;
    }

    /**
     *
     * @return
     */
    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    /**
     *
     * @return
     */
    private LatLngBounds getLatLngBounds() {
        return googleMap.getProjection().getVisibleRegion().latLngBounds;
    }
}
