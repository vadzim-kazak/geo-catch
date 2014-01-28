package com.jrew.geocatch.mobile.activity;

import android.content.res.Resources;
import android.os.Bundle;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.util.ActionBarHolder;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(theme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        ActionBar actionBar = getSupportActionBar();
        ActionBarHolder.setActionBar(actionBar);
        Resources resources = getResources();

        // Set app icon and name to action bar
        actionBar.setIcon(resources.getDrawable(R.drawable.ic_action_location));
        actionBar.setTitle(resources.getString(R.string.app_name));

        FragmentSwitcherHolder.initFragmentSwitcher(getSupportFragmentManager());

        // Set default fragment
        FragmentSwitcherHolder.getFragmentSwitcher().handleActivityCreation();
    }
}
