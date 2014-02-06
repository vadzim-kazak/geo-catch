package com.jrew.geocatch.mobile.listener;

import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.ActionBar;
import com.jrew.geocatch.mobile.fragment.FragmentSwitcher;
import com.jrew.geocatch.mobile.fragment.UserPhotosFragment;
import com.jrew.geocatch.mobile.util.ActionBarHolder;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 2/6/14
 * Time: 2:23 PM
 */
public class UserPhotoTabListener implements ActionBar.TabListener {

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        proceedTabSelection(tab);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {}

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {}

    /**
     *
     */
    protected void proceedTabSelection(ActionBar.Tab tab) {

        UserPhotosFragment.TabTag tabTag = (UserPhotosFragment.TabTag) tab.getTag();
        FragmentSwitcher fragmentSwitcher = FragmentSwitcherHolder.getFragmentSwitcher();
        if (tabTag == UserPhotosFragment.TabTag.UPLOADED_PHOTOS_TAB) {
            fragmentSwitcher.showUploadedPhotosFragment(false);
        } else if (tabTag == UserPhotosFragment.TabTag.POSTPONED_PHOTOS_TAB) {
            fragmentSwitcher.showPostponedPhotosFragment(false);
        }
    }
}
