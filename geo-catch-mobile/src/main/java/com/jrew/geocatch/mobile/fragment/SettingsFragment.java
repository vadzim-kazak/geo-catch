package com.jrew.geocatch.mobile.fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.util.ActionBarHolder;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;
import com.jrew.geocatch.mobile.view.LanguageSpinner;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 2/10/14
 * Time: 3:03 PM
 */
public class SettingsFragment extends SherlockFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Action bar subtitle
        ActionBar actionBar = ActionBarHolder.getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        View actionBarView = actionBar.getCustomView();
        ImageView settingImageView = (ImageView) actionBarView.findViewById(R.id.settingImageView);
        settingImageView.setVisibility(View.GONE);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        View settingsLayout = inflater.inflate(R.layout.settings_fragment, container, false);

        LanguageSpinner languageSpinner = (LanguageSpinner) settingsLayout.findViewById(R.id.languageSpinner);
        languageSpinner.setActivity(getActivity());

        return settingsLayout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_settings, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int pressedMenuItemId = item.getItemId();

        switch (pressedMenuItemId) {
            case R.id.forwardMenuOption:
                getSherlockActivity().getSupportFragmentManager().popBackStack();
                break;
        }

        return true;
    }

}
