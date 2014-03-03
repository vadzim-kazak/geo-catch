package com.jrew.geocatch.mobile.fragment;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Watson;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.Marker;
import com.androidmapsextensions.MarkerOptions;
import com.androidmapsextensions.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.reciever.ServiceResultReceiver;
import com.jrew.geocatch.mobile.service.ImageService;
import com.jrew.geocatch.mobile.service.cache.ImageCache;
import com.jrew.geocatch.mobile.util.*;
import com.jrew.geocatch.web.model.ClientImagePreview;
import com.jrew.geocatch.web.model.ViewBounds;
import com.jrew.geocatch.web.model.criteria.SearchCriteria;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 28.10.13
 * Time: 18:03
 * To change this template use File | Settings | File Templates.
 *
 */
public class MapFragment extends SupportMapFragment implements Watson.OnCreateOptionsMenuListener,
        Watson.OnOptionsItemSelectedListener, LocationListener {

    /**
     *
     */
    private class ImageLoadingTarget implements Target {

        /** **/
        private MarkerOptions markerOptions;

        /** **/
        private ClientImagePreview imagePreview;

        /**
         *
         * @param markerOptions
         * @param imagePreview
         */
        ImageLoadingTarget(MarkerOptions markerOptions, ClientImagePreview imagePreview) {
            this.markerOptions = markerOptions;
            this.imagePreview = imagePreview;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
            targets.remove(imagePreview.getId());

            Bitmap markerImage = BitmapUtil.createIconWithBorder(bitmap, getActivity());
            ImageCache.getInstance().addMarker(imagePreview.getId(), markerImage);
            addMarker(markerImage, markerOptions, imagePreview);
        }

        @Override
        public void onBitmapFailed(Drawable drawable) {
            targets.remove(imagePreview.getId());
        }

        @Override
        public void onPrepareLoad(Drawable drawable) {}
    }

    /** **/
    private GoogleMap googleMap;

    /** **/
    private Map<Long, Marker> markers;

    /** **/
    private boolean isLocationSet;

    /** **/
    private LocationManager locationManager;

    /** **/
    private ServiceResultReceiver imageResultReceiver;

    /** **/
    private Map<Long, Target> targets;

    /** **/
    private boolean[][] mapOccupancyMatrix;

    /** **/
    private double imagesFilteringAreasNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        ActionBarUtil.initActionBar(ActionBar.NAVIGATION_MODE_STANDARD, getActivity());
        ActionBarUtil.setActionBarSubtitle(R.string.mapFragmentLabel, getActivity());

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        View result = super.onCreateView(inflater, container, savedInstanceState);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (WebUtil.isNetworkAvailable(getActivity())) {

            LayoutUtil.showFragmentContainer(getActivity());

            if (googleMap == null) {

                imageResultReceiver = new ServiceResultReceiver(new Handler());
                imageResultReceiver.setReceiver(new ServiceResultReceiver.Receiver() {

                    @Override
                    public void onReceiveResult(int resultCode, Bundle resultData) {

                        switch (resultCode) {

                            case ImageService.ResultStatus.LOAD_IMAGES_FINISHED:

                                List<ClientImagePreview> images =
                                        (List<ClientImagePreview>) resultData.getSerializable(ImageService.RESULT_KEY);

                                if (images != null && !images.isEmpty()) {

                                    ImageCache.getInstance().addClientImagesPreview(images);

                                    if(!images.isEmpty()) {

                                        // 1) Clean occupancy matrix
                                        for (int i = 0; i < mapOccupancyMatrix.length; i++) {
                                            for (int j = 0; j < mapOccupancyMatrix[i].length; j++) {
                                                mapOccupancyMatrix[i][j] = false;
                                            }
                                        }

                                        //2) Prepare iteration data
                                        LatLngBounds latLngBounds = getLatLngBounds();
                                        double latitudeRange = latLngBounds.northeast.latitude - latLngBounds.southwest.latitude;
                                        double longitudeRange = latLngBounds.northeast.longitude - latLngBounds.southwest.longitude;
                                        double minDegreeRange = Math.min(latitudeRange, longitudeRange);
                                        double initialLatitude =  latLngBounds.southwest.latitude;
                                        double initialLongitude =  latLngBounds.southwest.longitude;
                                        double areaSize = minDegreeRange / imagesFilteringAreasNumber;
                                        int latitudeAreasNumber = (int)(latitudeRange / areaSize);
                                        int longitudeAreasNumber = (int)(longitudeRange / areaSize);


                                        // 3) Iterate through displayed markers
                                        Iterator<Map.Entry<Long, Marker>> iterator = markers.entrySet().iterator();
                                        while(iterator.hasNext()) {
                                            Map.Entry<Long, Marker> entry = iterator.next();
                                            Marker marker = entry.getValue();
                                            ClientImagePreview image = marker.getData();

                                            for (int i = 0; i < latitudeAreasNumber; i++) {
                                                for (int j = 0; j < longitudeAreasNumber; j++ ) {

                                                    if (image.getLatitude() >= i * areaSize + initialLatitude &&
                                                        image.getLatitude() < (i + 1) * areaSize + initialLatitude &&
                                                        image.getLongitude() >=  j * areaSize + initialLongitude &&
                                                        image.getLongitude() < (j + 1) * areaSize + initialLongitude) {

                                                        if (mapOccupancyMatrix[i][j] == false ) {
                                                            mapOccupancyMatrix[i][j] = true;
                                                        } else {
                                                            marker.remove();
                                                            iterator.remove();
                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                        }

                                        // 4) Add new images to free cells
                                        final MarkerOptions markerOptions = new MarkerOptions();
                                        for (final ClientImagePreview image : images) {

                                            if (!markers.containsKey(image.getId())) {

                                                for (int i = 0; i < latitudeAreasNumber; i++) {
                                                    for (int j = 0; j < longitudeAreasNumber; j++ ) {

                                                        if (image.getLatitude() >= i * areaSize + initialLatitude &&
                                                            image.getLatitude() < (i + 1) * areaSize + initialLatitude &&
                                                            image.getLongitude() >=  j * areaSize + initialLongitude &&
                                                            image.getLongitude() < (j + 1) * areaSize + initialLongitude) {

                                                            if (mapOccupancyMatrix[i][j] == false ) {
                                                                mapOccupancyMatrix[i][j] = true;

                                                                Bitmap markerImage = ImageCache.getInstance().getMarker(image.getId());
                                                                if (markerImage != null) {
                                                                    addMarker(markerImage, markerOptions, image);
                                                                } else {
                                                                    loadRemoteImage(image, markerOptions);
                                                                }
                                                            }
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            break;
                        }
                    }
                });


                googleMap = getExtendedMap();

                int mapType = Integer.parseInt(getResources().getString(R.config.mapType));
                googleMap.setMapType(mapType);

                markers = Collections.synchronizedMap(new HashMap<Long, Marker>());
                targets = Collections.synchronizedMap(new HashMap<Long, Target>());

                googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

                    @Override
                    public void onCameraChange(CameraPosition cameraPosition) {

                        // Remove invisible markers
                        removeInvisibleMarkers();

                        // Load new images for view bounds
                        loadImages();
                    }
                });

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        ClientImagePreview clientImagePreview = marker.getData();

                        Bundle fragmentData = new Bundle();
                        fragmentData.putSerializable(ImageService.IMAGE_KEY, clientImagePreview);
                        FragmentSwitcherHolder.getFragmentSwitcher().showPhotoBrowsingFragment(fragmentData);

                        return true;
                    }
                });

                isLocationSet = false;

                imagesFilteringAreasNumber = Double.parseDouble(
                        getResources().getString(R.config.imagesFilteringAreasNumber));

                loadImages();

            } else {
                clearMarkers();
                loadImages();
            }

        } else {
            LayoutUtil.showRefreshLayout(getActivity(), R.string.noNetworkConnectionError);
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
     */
    private void removeInvisibleMarkers() {

        LatLngBounds latLngBounds = getLatLngBounds();

        Iterator<Map.Entry<Long, Marker>> iterator = markers.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<Long, Marker> entry = iterator.next();
            Marker marker = entry.getValue();
            if (!latLngBounds.contains(marker.getPosition())) {
                iterator.remove();
                marker.remove();
            }
        }
    }

    /**
     *
     */
    private void loadImages() {

        LatLngBounds latLngBounds = getLatLngBounds();

        if (!(latLngBounds.northeast.latitude == 0 &&
            latLngBounds.northeast.longitude == 0 &&
            latLngBounds.southwest.latitude == 0 &&
            latLngBounds.southwest.longitude == 0)) {

            // init occupancy matrix
            if (mapOccupancyMatrix == null) {

                double latitudeRange = latLngBounds.northeast.latitude - latLngBounds.southwest.latitude;
                double longitudeRange = latLngBounds.northeast.longitude - latLngBounds.southwest.longitude;
                double minRange = Math.min(latitudeRange, longitudeRange);

                double areaRange = minRange / imagesFilteringAreasNumber;
                int latitudeAreasNumber = (int) (latitudeRange / areaRange);
                int longitudeAreasNumber = (int) (longitudeRange / areaRange);

                mapOccupancyMatrix = new boolean[latitudeAreasNumber][longitudeAreasNumber];
            }

            ViewBounds viewBounds = new ViewBounds(latLngBounds.northeast.latitude,
                    latLngBounds.northeast.longitude,
                    latLngBounds.southwest.latitude,
                    latLngBounds.southwest.longitude);

            SearchCriteria searchCriteria = SearchCriteriaHolder.getSearchCriteria();

            // DeviceId
            String deviceId = Settings.Secure.getString(getActivity().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            searchCriteria.setDeviceId(deviceId);
            searchCriteria.setViewBounds(viewBounds);

            ServiceUtil.callLoadImagesService(searchCriteria, imageResultReceiver, getActivity());
        }
    }

    /**
     *
     */
    private void clearMarkers() {
        googleMap.clear();
        markers.clear();
    }

    /**
     *
     * @return
     */
    private LatLngBounds getLatLngBounds() {
        return googleMap.getProjection().getVisibleRegion().latLngBounds;
    }

    @Override
    public void onLocationChanged(Location location) {

        if (googleMap != null && !isLocationSet && getActivity() != null) {
            CameraUpdate center =  CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),
                    location.getLongitude()));

            CameraUpdate zoom = CameraUpdateFactory.zoomTo(
                    Integer.parseInt(getResources().getString(R.config.cameraInitialZoom)));

            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);

            locationManager.removeUpdates(this);
            isLocationSet = true;
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {}

    @Override
    public void onProviderDisabled(String s) {}

    @Override
    public void onStop() {
        locationManager.removeUpdates(this);
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        if (googleMap != null) {
            loadImages();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Iterator<Map.Entry<Long, Target>> iterator = targets.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<Long, Target> entry = iterator.next();
            Target target = entry.getValue();
            PicassoHolder.getPicasso().cancelRequest(target);
            iterator.remove();
        }
    }

    /**
     *
     * @param imagePreview
     * @param markerOptions
     */
    private void loadRemoteImage(final ClientImagePreview imagePreview, final MarkerOptions markerOptions) {
        if (!targets.containsKey(imagePreview.getId())) {
            ImageLoadingTarget imageLoadingTarget = new ImageLoadingTarget(markerOptions, imagePreview);
            targets.put(imagePreview.getId(), imageLoadingTarget);
            PicassoHolder.getPicasso().load(imagePreview.getThumbnailPath()).into(imageLoadingTarget);
        }
    }

    /**
     *
     * @param markerBitmap
     * @param markerOptions
     * @param imagePreview
     */
    private void addMarker(Bitmap markerBitmap,
                           MarkerOptions markerOptions,
                           ClientImagePreview imagePreview) {

        markerOptions.position(new LatLng(imagePreview.getLatitude(), imagePreview.getLongitude()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(markerBitmap));
        synchronized (markers) {
            if (!markers.containsKey(imagePreview.getId())) {
                Marker marker = googleMap.addMarker(markerOptions);
                marker.setData(imagePreview);
                markers.put(imagePreview.getId(), marker);
            }
        }

    }
}
