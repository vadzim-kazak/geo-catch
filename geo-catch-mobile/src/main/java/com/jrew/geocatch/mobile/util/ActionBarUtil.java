package com.jrew.geocatch.mobile.util;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.util.ActionBarHolder;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 11.02.14
 * Time: 11:27
 * To change this template use File | Settings | File Templates.
 */
public class ActionBarUtil {

    /**
     *
     * @param navigationMode
     * @param activity
     */
    public static void initActionBar(int navigationMode, Activity activity) {
        initActionBar(navigationMode, true, activity);
    }

    /**
     *
     * @param navigationMode
     * @param showSettingsMenuOption
     * @param activity
     */
    public static void initActionBar(int navigationMode, boolean showSettingsMenuOption, Activity activity) {

        ActionBar actionBar = ActionBarHolder.getActionBar();

        // Navigation mode
        if (actionBar.getNavigationMode() != navigationMode) {
            ActionBarHolder.getActionBar().setNavigationMode(navigationMode);
        }

        View actionBarView = actionBar.getCustomView();
        if (actionBarView == null) {
            actionBarView = View.inflate(activity, R.layout.action_bar_layout, null);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(actionBarView);
        }

        hideStandardActionBar(actionBar, activity);

        // settings action bar option
        ImageView actionBarSettingsImageView = (ImageView) actionBarView.findViewById(R.id.settingsImageView);
        actionBarSettingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentSwitcherHolder.getFragmentSwitcher().showSettingsFragment();
            }
        });

        if (showSettingsMenuOption) {
            actionBarSettingsImageView.setVisibility(View.VISIBLE);
        } else {
            actionBarSettingsImageView.setVisibility(View.GONE);
        }
    }

    /**
     *
     * @param actionBar
     * @param activity
     */
    private static void hideStandardActionBar(ActionBar actionBar, Activity activity) {

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setLogo(null);

        View homeIcon = activity.findViewById(
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                        android.R.id.home : R.id.abs__home);
        if (homeIcon != null) {
            homeIcon.setVisibility(View.GONE);
            View homeIconParent = (View) homeIcon.getParent();
            if (homeIconParent != null && homeIconParent.getVisibility() != View.GONE) {
                homeIconParent.setVisibility(View.GONE);
            }
        }
    }

    /**
     *
     * @param subtitleResourceId
     * @param activity
     */
    public static void setActionBarSubtitle(int subtitleResourceId, Activity activity) {

        ActionBar actionBar = ActionBarHolder.getActionBar();
        View actionBarView = actionBar.getCustomView();

        // action bar subtitle
        TextView actionBarSubtitleTextView = (TextView) actionBarView.findViewById(R.id.subTitleTextView);
        actionBarSubtitleTextView.setText(activity.getString(subtitleResourceId));
    }
}
