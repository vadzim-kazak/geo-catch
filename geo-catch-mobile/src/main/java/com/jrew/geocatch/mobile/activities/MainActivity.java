package com.jrew.geocatch.mobile.activities;

import android.content.res.Resources;
import android.os.Bundle;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jrew.geocatch.mobile.R;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 28.10.13
 * Time: 19:02
 * To change this template use File | Settings | File Templates.
 */
public class MainActivity extends SherlockActivity {

    private static int theme = R.style.Theme_Sherlock_Light;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        ActionBar actionBar = getSupportActionBar();
        Resources resources = getResources();

        actionBar.setHomeButtonEnabled(false);

        actionBar.setIcon(resources.getDrawable(R.drawable.ic_action_location));
        actionBar.setTitle(resources.getString(R.string.app_name));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getSupportMenuInflater().inflate(R.menu.menu_main, menu);

        final ActionBar actionBar = getSupportActionBar();
        final Resources resources = getResources();

        // View Map Menu Option click listener
        menu.findItem(R.id.viewMapMenuOption).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                actionBar.setSubtitle(resources.getString(R.string.mapLabel));
                return false;
            }
        });

        // View Settings Menu Option click listener
        menu.findItem(R.id.viewSettingsMenuOption).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                actionBar.setSubtitle(resources.getString(R.string.viewSettingsLabel));
                return false;
            }
        });

        // View Image Menu Option click listener
        menu.findItem(R.id.viewImageMenuOption).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                actionBar.setSubtitle(resources.getString(R.string.viewImageLabel));
                return false;
            }
        });

        // Take Image Menu Option click listener
        menu.findItem(R.id.takeImageMenuOption).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                actionBar.setSubtitle(resources.getString(R.string.takeImageLabel));
                return false;
            }
        });

        // Take Images Menu Option click listener
        menu.findItem(R.id.ownImagesMenuOption).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                actionBar.setSubtitle(resources.getString(R.string.ownImagesLabel));
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }
}
