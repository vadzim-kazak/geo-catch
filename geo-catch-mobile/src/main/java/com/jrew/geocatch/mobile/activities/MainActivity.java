package com.jrew.geocatch.mobile.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;
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
public class MainActivity extends SherlockActivity implements ActionBar.TabListener {

    private static int theme = R.style.Theme_Sherlock_Light;

    public static int THEME = R.style.Theme_Sherlock_Light_DarkActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(theme); //Used for theme switching in samples
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        ActionBar actionBar = getSupportActionBar();

        //The following two options trigger the collapsing of the main action bar view.
        //See the parent activity for the rest of the implementation
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        actionBar.setHomeButtonEnabled(false);


        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

//        for (int i = 1; i <= 3; i++) {
//            ActionBar.Tab tab = actionBar.newTab();
//            tab.setText("Tab " + i);
//            tab.setIcon(R.drawable.ic_action_location);
//            tab.setTabListener(this);
//            actionBar.addTab(tab);
//        }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getSupportMenuInflater().inflate(R.menu.menu_main, menu);
//        return super.onCreateOptionsMenu(menu);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);

//        boolean isLight = THEME == R.style.Theme_Sherlock_Light;
//
//        menu.add("Save")
//                .setIcon(isLight ? R.drawable.ic_compose_inverse : R.drawable.ic_compose)
//                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
//
//        menu.add("Search")
//                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//
//        menu.add("Refresh")
//                .setIcon(isLight ? R.drawable.ic_refresh_inverse : R.drawable.ic_refresh)
//                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//
//        return true;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
