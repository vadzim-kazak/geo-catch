package com.jrew.geocatch.mobile.activity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
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
public class MainActivity extends SherlockFragmentActivity {

    /** **/
    private static int theme = R.style.Theme_Sherlock_Light;

    /** **/
    private MenuHelper menuHelper;

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

}
