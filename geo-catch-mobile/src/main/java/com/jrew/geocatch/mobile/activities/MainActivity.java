package com.jrew.geocatch.mobile.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import com.jrew.geocatch.mobile.R;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 28.10.13
 * Time: 19:02
 * To change this template use File | Settings | File Templates.
 */
public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_layout);

        //Fragment mapFragment = getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        //Fragment bottomBarFragment = getSupportFragmentManager().findFragmentById(R.id.bottomBarFragment);
    }
}
