package com.jrew.geocatch.mobile.util;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.actionbarsherlock.app.ActionBar;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.dao.PostponedImageManager;

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
     */
    private static enum MenuOption {

        /** **/
        SETTINGS_MENU_OPTION,

        /** **/
        LOCATION_LOADING_MENU_OPTION,
    }

    /**
     *
     */
    public static enum TabTag {

        UPLOADED_PHOTOS_TAB,

        POSTPONED_PHOTOS_TAB
    }

    /** **/
    private static MenuOption[] NO_MENU_OPTIONS = new MenuOption[0];

    /** **/
    private static MenuOption[] SETTING_MENU_OPTION = new MenuOption[] {MenuOption.SETTINGS_MENU_OPTION};

    /** **/
    private static MenuOption[] BOTH_MENU_OPTIONS = new MenuOption[] {MenuOption.SETTINGS_MENU_OPTION,
            MenuOption.LOCATION_LOADING_MENU_OPTION};

    /**
     *
     * @param navigationMode
     * @param activity
     */
    public static void initActionBar(int navigationMode, Activity activity) {
        initActionBar(navigationMode, activity, SETTING_MENU_OPTION);
    }

    /**
     *
     * @param navigationMode
     * @param activity
     */
    public static void initSettingsActionBar(int navigationMode, Activity activity) {
        initActionBar(navigationMode, activity, NO_MENU_OPTIONS);
    }

    /**
     *
     * @param navigationMode
     * @param activity
     */
    public static void initPopulatePhotoInfoActionBar(int navigationMode, Activity activity) {
        initActionBar(navigationMode, activity, BOTH_MENU_OPTIONS);
    }

    /**
     *
     * @param navigationMode
     * @param activity
     * @param menuOptions
     */
    public static void initActionBar(int navigationMode, Activity activity,  MenuOption... menuOptions) {

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

        if (isPresented(MenuOption.SETTINGS_MENU_OPTION, menuOptions)) {
            actionBarSettingsImageView.setVisibility(View.VISIBLE);
        } else {
            actionBarSettingsImageView.setVisibility(View.GONE);
        }


        // location loading action bar option
        ImageView actionBarLocationImageView = (ImageView) actionBarView.findViewById(R.id.locationLoading);
        if (isPresented(MenuOption.LOCATION_LOADING_MENU_OPTION, menuOptions)) {
            actionBarLocationImageView.setVisibility(View.VISIBLE);
        } else {
            actionBarLocationImageView.clearAnimation();
            actionBarLocationImageView.setVisibility(View.GONE);
        }
    }

    /**
     *
     * @param menuOption
     * @param menuOptions
     * @return
     */
    private static boolean isPresented(MenuOption menuOption, MenuOption... menuOptions) {

        for (MenuOption currentMenuOption : menuOptions) {
            if (currentMenuOption == menuOption) {
                return true;
            }
        }

        return false;
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

    /**
     *
     * @param activity
     */
    public static void initTabActionBar(Activity activity, TabTag tabToSelect, ActionBar.TabListener tabListener) {

        ActionBar actionBar = ActionBarHolder.getActionBar();

        if (PostponedImageManager.isPostponedImagesPresented(activity)) {
            ActionBarUtil.initActionBar(ActionBar.NAVIGATION_MODE_TABS, activity);

            int tabCount = actionBar.getTabCount();
            ActionBar.Tab uploadedPhotosTab, postponedPhotosTab;
            if (tabCount == 0) {

                // Init tabs
                uploadedPhotosTab = actionBar.newTab();
                uploadedPhotosTab.setIcon(android.R.drawable.ic_menu_gallery);
                uploadedPhotosTab.setTag(TabTag.UPLOADED_PHOTOS_TAB);
                uploadedPhotosTab.setTabListener(tabListener);
                actionBar.addTab(uploadedPhotosTab, false);
                if (TabTag.UPLOADED_PHOTOS_TAB == tabToSelect) {
                    uploadedPhotosTab.select();
                }

                postponedPhotosTab = actionBar.newTab();
                postponedPhotosTab.setIcon(android.R.drawable.ic_menu_recent_history);
                postponedPhotosTab.setTag(TabTag.POSTPONED_PHOTOS_TAB);
                postponedPhotosTab.setTabListener(tabListener);
                actionBar.addTab(postponedPhotosTab, false);
                if (TabTag.POSTPONED_PHOTOS_TAB == tabToSelect) {
                    postponedPhotosTab.select();
                }
            } else {
                if (TabTag.UPLOADED_PHOTOS_TAB == tabToSelect || TabTag.POSTPONED_PHOTOS_TAB == tabToSelect) {
                    getActionBarTabByTag(tabToSelect, actionBar).select();
                }
            }
        } else {
            ActionBarUtil.initActionBar(ActionBar.NAVIGATION_MODE_STANDARD, activity);
        }
    }

    /**
     *
     * @param tag
     * @param actionBar
     * @return
     */
    private static ActionBar.Tab getActionBarTabByTag(TabTag tag, ActionBar actionBar) {

        for (int i = 0; i < actionBar.getTabCount(); i++ ) {
            ActionBar.Tab tab = actionBar.getTabAt(i);
            if (tag == tab.getTag()) {
                return tab;
            }
        }

        return null;
    }

    /**
     *
     * @return
     */
    public static ImageView getLocationLoadingImageView() {

        ActionBar actionBar = ActionBarHolder.getActionBar();
        View actionBarView = actionBar.getCustomView();

        return (ImageView) actionBarView.findViewById(R.id.locationLoading);
    }
}
