package com.jrew.geocatch.mobile.fragment;

import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.dao.PostponedImageManager;
import com.jrew.geocatch.mobile.util.ActionBarHolder;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 2/4/14
 * Time: 1:25 PM
 */
public abstract class UserPhotosFragment extends SherlockFragment implements ActionBar.TabListener {

    /**
     *
     */
    protected enum TabTag {

        UPLOADED_PHOTOS_TAB,

        POSTPONED_PHOTOS_TAB
    }

    /**
     *
     */
    protected abstract TabTag getTabTag();

    /**
     *
     */
    protected void init() {

        setHasOptionsMenu(true);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Action bar subtitle
        ActionBar actionBar = ActionBarHolder.getActionBar();

        //Init fragment in tab mode if there are postponed images presented
        if (PostponedImageManager.isPostponedImagesPresented(getActivity())) {
            int currentNavigationMode = actionBar.getNavigationMode();
            if (currentNavigationMode != ActionBar.NAVIGATION_MODE_TABS) {
                actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            }

            int tabCount = actionBar.getTabCount();
            ActionBar.Tab uploadedPhotosTab, postponedPhotosTab;
            if (tabCount == 0) {
                // Init tabs
                uploadedPhotosTab = actionBar.newTab();
                uploadedPhotosTab.setIcon(android.R.drawable.ic_menu_gallery);
                uploadedPhotosTab.setTag(TabTag.UPLOADED_PHOTOS_TAB);
                uploadedPhotosTab.setTabListener(this);
                actionBar.addTab(uploadedPhotosTab, false);

                postponedPhotosTab = actionBar.newTab();
                postponedPhotosTab.setIcon(R.drawable.clock);
                postponedPhotosTab.setTag(TabTag.POSTPONED_PHOTOS_TAB);
                postponedPhotosTab.setTabListener(this);
                actionBar.addTab(postponedPhotosTab, false);
            } else {
                uploadedPhotosTab = getTabByTag(TabTag.UPLOADED_PHOTOS_TAB, actionBar);
                postponedPhotosTab = getTabByTag(TabTag.POSTPONED_PHOTOS_TAB, actionBar);
            }

            TabTag currentTabTag = getTabTag();
            ActionBar.Tab selectedTab = actionBar.getSelectedTab();
            if (currentTabTag == TabTag.UPLOADED_PHOTOS_TAB) {
                uploadedPhotosTab.select();
            } else if (currentTabTag == TabTag.POSTPONED_PHOTOS_TAB) {
                postponedPhotosTab.select();
            }
        }
    }

    /**
     *
     * @param tag
     * @param actionBar
     * @return
     */
    private ActionBar.Tab getTabByTag(TabTag tag, ActionBar actionBar) {
        for (int i = 0; i < actionBar.getTabCount(); i++) {
            ActionBar.Tab currentTab = actionBar.getTabAt(i);
            TabTag currentTabTag = (TabTag) currentTab.getTag();
            if (currentTabTag == tag) {
                return currentTab;
            }
        }
        return null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_user_photos, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int pressedMenuItemId = item.getItemId();

        FragmentSwitcher fragmentSwitcher = FragmentSwitcherHolder.getFragmentSwitcher();
        switch (pressedMenuItemId) {
            case R.id.proceedToMapMenuOption:
                fragmentSwitcher.showMapFragment();
                break;

            case R.id.takeImageMenuOption:
                fragmentSwitcher.showGetPhotoFragment();
                break;
        }

        return true;
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction transaction) {

    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction transaction) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction transaction) {}

    /**
     *
     * @param tab
     */
    protected void proceedTabSelection(ActionBar.Tab tab) {
        TabTag newTabTag = (TabTag) tab.getTag();
        FragmentSwitcher fragmentSwitcher = FragmentSwitcherHolder.getFragmentSwitcher();
        if (newTabTag == TabTag.UPLOADED_PHOTOS_TAB) {
            fragmentSwitcher.showUploadedPhotosFragment();
        } else if (newTabTag == TabTag.POSTPONED_PHOTOS_TAB) {
            fragmentSwitcher.showPostponedPhotosFragment();
        }
    }
}
