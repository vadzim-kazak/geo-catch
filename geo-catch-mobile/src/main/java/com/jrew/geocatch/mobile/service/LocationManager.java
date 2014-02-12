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
public class LocationManager implements
        GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    /** **/
    private LocationClient locationClient;

    /** **/
    private boolean isUsed = false;

    /**
     *
     * @param activity
     */
    public LocationManager(Activity activity) {
        locationClient = new LocationClient(activity, this, this);
    }

    /**
     *
     */
    public void start() {
        isUsed = true;
        connect();
    }

    /**
     *
     */
    public void stop() {
        isUsed = false;
        disconnect();
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

    @Override
    public void onConnected(Bundle bundle) {}

    @Override
    public void onDisconnected() {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}
}
