package com.jrew.geocatch.mobile.service;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 12.02.14
 * Time: 16:48
 * To change this template use File | Settings | File Templates.
 */
public class LocationManager {

    /** **/
    private LocationClient locationClient;

    /** **/
    private boolean isUsed = false;

    /** **/
    private Activity activity;

    /**
     *
     * @param activity
     */
    public LocationManager(Activity activity) {
        this.activity = activity;
    }

    /**
     *
     * @param connectionCallback
     * @param connectionFailedListener
     */
    public void start(GooglePlayServicesClient.ConnectionCallbacks connectionCallback,
                      GooglePlayServicesClient.OnConnectionFailedListener connectionFailedListener) {

        if (locationClient == null) {
            locationClient = new LocationClient(activity, connectionCallback, connectionFailedListener);
        } else {
            locationClient.registerConnectionCallbacks(connectionCallback);
            locationClient.registerConnectionFailedListener(connectionFailedListener);
        }
        isUsed = true;
        connect();
    }

    /**
     *
     * @param connectionCallback
     * @param connectionFailedListener
     */
    public void stop(GooglePlayServicesClient.ConnectionCallbacks connectionCallback,
                     GooglePlayServicesClient.OnConnectionFailedListener connectionFailedListener) {
        isUsed = false;
        disconnect();
        locationClient.unregisterConnectionCallbacks(connectionCallback);
        locationClient.unregisterConnectionFailedListener(connectionFailedListener);
    }

    /**
     *
     */
    public void connect() {
        if (locationClient != null &&
            !locationClient.isConnected() &&
            isUsed) {
            locationClient.connect();
        }
    }

    /**
     *
     */
    public void disconnect() {
        if (locationClient != null &&
            (locationClient.isConnected() || locationClient.isConnecting())) {
            locationClient.disconnect();
        }
    }

    /**
     *
     * @return
     */
    public Location getCurrentLocation() {

        if (locationClient != null && locationClient.isConnected()) {
            return locationClient.getLastLocation();
        }

        return null;
    }

}
