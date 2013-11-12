package com.jrew.geocatch.mobile.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.fragment.FragmentSwitcher;
import com.jrew.geocatch.mobile.fragment.MapFragment;
import com.jrew.geocatch.mobile.fragment.ImageViewFragment;
import com.jrew.geocatch.mobile.fragment.MapSettingsFragment;
import com.jrew.geocatch.mobile.menu.MenuHelper;

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
    private MenuHelper menuHelper;

    /** **/
    private FragmentSwitcher fragmentSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(theme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        ActionBar actionBar = getSupportActionBar();
        Resources resources = getResources();

        // Set app icon and name to action bar
        actionBar.setIcon(resources.getDrawable(R.drawable.ic_action_location));
        actionBar.setTitle(resources.getString(R.string.app_name));

        fragmentSwitcher = new FragmentSwitcher(getSupportFragmentManager());

        // Set default fragment
        fragmentSwitcher.showMapFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getSupportMenuInflater().inflate(R.menu.menu_main, menu);
        menuHelper = new MenuHelper(menu, this);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                menuHelper.init();
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        menuHelper.onOptionsItemSelected(item);
        return true;
    }

    /**
     *
     * @return
     */
    public FragmentSwitcher getFragmentSwitcher() {
        return fragmentSwitcher;
    }
}
