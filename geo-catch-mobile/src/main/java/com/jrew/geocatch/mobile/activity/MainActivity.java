package com.jrew.geocatch.mobile.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.reciever.DomainInfoServiceResultReceiver;
import com.jrew.geocatch.mobile.service.DomainInfoService;
import com.jrew.geocatch.mobile.util.ActionBarHolder;
import com.jrew.geocatch.mobile.util.CommonUtils;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;
import com.jrew.geocatch.mobile.util.SharedPreferencesHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 28.10.13
 * Time: 19:02
 * To change this template use File | Settings | File Templates.
 */
public class MainActivity extends SherlockFragmentActivity {

    /** **/
    private static int theme = R.style.Theme_Sherlock_Light;

    /** **/
    private ProgressDialog progressDialog;

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

        FragmentSwitcherHolder.initFragmentSwitcher(getSupportFragmentManager());

        syncDomainsInfo();

        // Set default fragment
        FragmentSwitcherHolder.getFragmentSwitcher().handleActivityCreation();
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
            if ((currentDate.getTime() - syncPeriod * 60 * 60 * 1000) <=  lastSyncDate.getTime()) {
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
}
