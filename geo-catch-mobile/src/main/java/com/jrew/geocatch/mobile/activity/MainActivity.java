package com.jrew.geocatch.mobile.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.dao.DomainDatabaseManager;
import com.jrew.geocatch.mobile.dao.PostponedImageManager;
import com.jrew.geocatch.mobile.reciever.DomainInfoServiceResultReceiver;
import com.jrew.geocatch.mobile.service.DomainInfoService;
import com.jrew.geocatch.mobile.util.ActionBarHolder;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;
import com.jrew.geocatch.mobile.util.SharedPreferencesHelper;

import java.util.Date;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 28.10.13
 * Time: 19:02
 * To change this template use File | Settings | File Templates.
 */
public class MainActivity extends SherlockFragmentActivity implements
        GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener {

    /** **/
    private static int theme = R.style.Theme_Sherlock_Light;

    /** **/
    private LocationClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(theme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        ActionBar actionBar = getSupportActionBar();
        ActionBarHolder.setActionBar(actionBar);
        Resources resources = getResources();

        // Set app icon and name to action bar
        actionBar.setIcon(resources.getDrawable(R.drawable.icon));
        actionBar.setTitle(resources.getString(R.string.appName));

        if (isGooglePlayServicesInstalled()) {

            FragmentSwitcherHolder.initFragmentSwitcher(getSupportFragmentManager());

            syncDomainsInfo();

            // Set default fragment
            FragmentSwitcherHolder.getFragmentSwitcher().handleActivityCreation();

        /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
            locationClient = new LocationClient(this, this, this);

        }

        DomainDatabaseManager.loadDomainProperties(this);
    }

    /**
     *
     * @return
     */
    private void syncDomainsInfo() {

        Date lastSyncDate = SharedPreferencesHelper.getLastSyncDate(this);
        if (lastSyncDate != null) {

            String syncPeriodConfig = getResources().getString(R.config.domainInfoSyncPeriodInHours);
            int syncPeriod = Integer.parseInt(syncPeriodConfig);

            Date currentDate = new Date();
            if (currentDate.getTime() - lastSyncDate.getTime() >= syncPeriod * 60 * 60 * 1000 ) {
                // It's time to perform sync
                processSyncDomainsInfo();
            }

        }

        // LastSyncDate isn't set. Probably this is first app launch
        processSyncDomainsInfo();
    }

    /**
     *
     */
    private void processSyncDomainsInfo() {

        Bundle bundle = new Bundle();

        String locale = Locale.getDefault().getLanguage();
        bundle.putString(DomainInfoService.LOCALE_KEY, locale);

        final Intent intent = new Intent(Intent.ACTION_SYNC, null, this, DomainInfoService.class);
        intent.putExtra(DomainInfoService.REQUEST_KEY, bundle);
        intent.putExtra(DomainInfoService.RECEIVER_KEY, new DomainInfoServiceResultReceiver(new Handler(), this));
        startService(intent);
    }

    @Override
    public void onConnected(Bundle bundle) {}

    @Override
    public void onDisconnected() {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Couldn't get current location. Make sure that GPS sensor is turned on.",
                Toast.LENGTH_LONG).show();
    }

    /*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        locationClient.connect();
    }

    /*
         * Called when the Activity is no longer visible.
         */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        locationClient.disconnect();
        super.onStop();
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

    /**
     *
     * @return
     */
    private boolean isGooglePlayServicesInstalled() {

        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        }

        return false;
    }

    @Override
    protected void onDestroy() {
        PostponedImageManager.close();
        super.onDestroy();
    }
}
