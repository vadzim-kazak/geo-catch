package com.jrew.geocatch.mobile.activity;

import android.content.res.Resources;
import android.os.Bundle;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.menu.MenuHelper;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 28.10.13
 * Time: 19:02
 * To change this template use File | Settings | File Templates.
 */
public class MainActivity extends SherlockActivity {

    private static int theme = R.style.Theme_Sherlock_Light;

    private MenuHelper menuHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(theme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        ActionBar actionBar = getSupportActionBar();
        Resources resources = getResources();

        //actionBar.setHomeButtonEnabled(false);

        // Set app icon and name to action bar
        actionBar.setIcon(resources.getDrawable(R.drawable.ic_action_location));
        actionBar.setTitle(resources.getString(R.string.app_name));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getSupportMenuInflater().inflate(R.menu.menu_main, menu);
        menuHelper = new MenuHelper(menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final SherlockActivity activity = this;
        final ActionBar actionBar = getSupportActionBar();
        final Resources resources = getResources();

        menuHelper.processMenuItemBackgroundSelection(item, activity);

        switch (item.getItemId()) {

            case R.id.viewMapMenuOption:
                actionBar.setSubtitle(resources.getString(R.string.mapLabel));
                break;

            case R.id.viewSettingsMenuOption:
                actionBar.setSubtitle(resources.getString(R.string.viewSettingsLabel));
                break;

            case R.id.viewImageMenuOption:
                actionBar.setSubtitle(resources.getString(R.string.viewImageLabel));
                break;

            case R.id.takeImageMenuOption:
                actionBar.setSubtitle(resources.getString(R.string.takeImageLabel));
                break;

            case R.id.ownImagesMenuOption:
                actionBar.setSubtitle(resources.getString(R.string.ownImagesLabel));
                break;

            default:
                break;
        }

        return true;
    }
}
