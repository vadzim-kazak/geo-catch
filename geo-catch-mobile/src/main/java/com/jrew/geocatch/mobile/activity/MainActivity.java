package com.jrew.geocatch.mobile.activity;

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
import com.jrew.geocatch.mobile.util.*;

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
    private static int theme = R.style.Theme_Styled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(theme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        // Update app locale
        String appLocale = SharedPreferencesHelper.loadLocale(this);
        if (appLocale.length() > 0) {
            LocalizationUtil.switchToLocale(appLocale, this);
        }

        ActionBar actionBar = getSupportActionBar();
        ActionBarHolder.setActionBar(actionBar);

        if (isGooglePlayServicesInstalled()) {

            FragmentSwitcherHolder.initFragmentSwitcher(getSupportFragmentManager(), this);
            FragmentSwitcherHolder.getFragmentSwitcher().handleActivityCreation();


            if (WebUtil.isNetworkAvailable(this)) {
                syncDomainsInfo();
            }

            /*
             * Create a new location client, using the enclosing class to
             * handle callbacks.
             */
            LocationManagerHolder.initLocationHolder(this);
        }
    }

    /**
     *
     * @return
     */
    private void syncDomainsInfo() {

        Date lastSyncDate = SharedPreferencesHelper.loadLastSyncDate(this);
        if (lastSyncDate != null) {

            int syncPeriod = getResources().getInteger(R.config.domainInfoSyncPeriodInHours);

            Date currentDate = new Date();
            if (currentDate.getTime() - lastSyncDate.getTime() >= syncPeriod * 60 * 60 * 1000 ) {
                // It's time to perform sync
                processSyncDomainsInfo();
            }

        } else {
            // LastSyncDate isn't set. Probably this is first app launch
            processSyncDomainsInfo();
        }
    }

    /**
     *
     */
    private void processSyncDomainsInfo() {

        Bundle bundle = new Bundle();

        String locale = Locale.getDefault().getLanguage();
        bundle.putString(DomainInfoService.LOCALE_KEY, locale);

        ServiceUtil.callLoadDomainInfoService(bundle, new DomainInfoServiceResultReceiver(new Handler(), this), this);
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
        LocationManagerHolder.getLocationManager().connect();
    }

    /*
         * Called when the Activity is no longer visible.
         */
    @Override
    protected void onStop() {
        LocationManagerHolder.getLocationManager().disconnect();
        HttpClientHolder.release();
        super.onStop();
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
