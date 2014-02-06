package com.jrew.geocatch.mobile.fragment;

import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.dao.PostponedImageManager;
import com.jrew.geocatch.mobile.listener.UserPhotoTabListener;
import com.jrew.geocatch.mobile.util.ActionBarHolder;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 2/4/14
 * Time: 1:25 PM
 */
public class UserPhotosFragment extends SherlockFragment {

    /**
     *
     */
    public enum TabTag {

        UPLOADED_PHOTOS_TAB,

        POSTPONED_PHOTOS_TAB
    }

    /**
     *
     */
    protected void init() {

        setHasOptionsMenu(true);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

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

                UserPhotoTabListener tabListener = new UserPhotoTabListener();

                // Init tabs
                uploadedPhotosTab = actionBar.newTab();
                uploadedPhotosTab.setIcon(android.R.drawable.ic_menu_gallery);
                uploadedPhotosTab.setTag(TabTag.UPLOADED_PHOTOS_TAB);
                uploadedPhotosTab.setTabListener(tabListener);
                actionBar.addTab(uploadedPhotosTab, false);

                postponedPhotosTab = actionBar.newTab();
                postponedPhotosTab.setIcon(R.drawable.clock);
                postponedPhotosTab.setTag(TabTag.POSTPONED_PHOTOS_TAB);
                postponedPhotosTab.setTabListener(tabListener);
                actionBar.addTab(postponedPhotosTab, false);
            }
        }
    }

    /**
     *
     * @param tag
     * @param actionBar
     * @return
     */
    protected ActionBar.Tab getTabByTag(TabTag tag, ActionBar actionBar) {
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

    /**
     *
     * @param tabTag
     */
    public void selectTabByTag(TabTag tabTag) {
        ActionBar actionBar = ActionBarHolder.getActionBar();
        ActionBar.Tab tab = getTabByTag(tabTag, actionBar);
        if (tab != null) {
            tab.select();
        }
    }
}
