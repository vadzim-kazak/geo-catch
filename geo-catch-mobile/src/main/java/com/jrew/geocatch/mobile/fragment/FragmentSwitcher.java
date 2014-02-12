package com.jrew.geocatch.mobile.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.actionbarsherlock.app.ActionBar;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.util.ActionBarUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 12.11.13
 * Time: 16:56
 * To change this template use File | Settings | File Templates.
 */
public class FragmentSwitcher implements ActionBar.TabListener {

    public interface TAG {

        /** **/
        String MAP_FRAGMENT_TAG = "mapFragmentTag";

        /** **/
        String MAP_SETTINGS_FRAGMENT_TAG = "mapSettingsFragmentTag";

        /** **/
        String PHOTO_BROWSING_FRAGMENT_TAG = "photoBrowsingFragmentTag";

        /** **/
        String GET_PHOTO_FRAGMENT_TAG = "getPhotoFragmentTag";

        /** **/
        String PREVIEW_PHOTO_FRAGMENT_TAG = "previewPhotoFragmentTag";

        /** **/
        String POPULATE_PHOTO_INFO_FRAGMENT_TAG = "populatePhotoInfoFragmentTag";

        /** **/
        String POSTPONED_PHOTOS_FRAGMENT_TAG = "postponedPhotosFragmentTag";

        /** **/
        String UPLOADED_PHOTOS_FRAGMENT_TAG = "uploadedPhotosFragmentTag";

        /** **/
        String SETTINGS_FRAGMENT_TAG = "settingsFragmentTag";

        /** **/
        String PREPARE_USER_ACTION_BAR_FRAGMENT_TAG = "prepareUserActionBarFragmentTag";
    }

    /** **/
    private Activity activity;

    /** **/
    private FragmentManager fragmentManager;

    /**
     *
     * @param fragmentManager
     */
    public FragmentSwitcher(FragmentManager fragmentManager, Activity activity) {
        this.fragmentManager = fragmentManager;
        this.activity = activity;
    }

    /**
     *
     * @param fragmentTag
     */
    private void showFragment(String fragmentTag) {
        showFragment(fragmentTag, null);
    }

    /**
     *
     * @param fragmentTag
     * @param fragmentData
     */
    private void showFragment(String fragmentTag, Bundle fragmentData) {
        showFragment(fragmentTag, fragmentData, true);
    }

    /**
     *
     * @param fragmentTag
     * @param bundle
     * @param addToBackStack
     */
    private void showFragment(String fragmentTag, Bundle bundle, boolean addToBackStack) {

        Fragment fragment = getFragment(fragmentTag);

        if (bundle != null) {
            Bundle fragmentBundle = fragment.getArguments();
            if (fragmentBundle != null ) {
                fragmentBundle.clear();
                fragmentBundle.putAll(bundle);
            } else {
                fragment.setArguments(bundle);
            }
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment, fragmentTag);
        if (addToBackStack) {
            transaction.addToBackStack(fragmentTag);
        }
        transaction.commit();
    }

    /**
     *
     * @param fragmentTag
     * @return
     */
    private Fragment getFragment(String fragmentTag) {
        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
        if (fragment == null) {
            fragment = createFragment(fragmentTag);
        }

        return fragment;
    }

    /**
     *
     */
    public void popBackStack() {
        if (fragmentManager.getBackStackEntryCount() > 1) {

            String newTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 2).getName();
            if (TAG.UPLOADED_PHOTOS_FRAGMENT_TAG.equalsIgnoreCase(newTag)) {
                showUploadedPhotosFragment();
            } else if (TAG.POSTPONED_PHOTOS_FRAGMENT_TAG.equalsIgnoreCase(newTag)) {
                showPostponedPhotosFragment();
            } else {
                fragmentManager.popBackStack();
            }
        }
    }

    /**
     *
     * @param fragmentTag
     * @return
     */
    private Fragment createFragment(String fragmentTag) {

        if (TAG.MAP_FRAGMENT_TAG.equals(fragmentTag)){
            return new MapFragment();
        } else if (TAG.MAP_SETTINGS_FRAGMENT_TAG.equals(fragmentTag)) {
            return new MapSettingsFragment();
        } else if (TAG.PHOTO_BROWSING_FRAGMENT_TAG.equals(fragmentTag)) {
            return new PhotoBrowsingFragment();
        } else if (TAG.GET_PHOTO_FRAGMENT_TAG.equals(fragmentTag)) {
            return new GetPhotoFragment();
        } else if (TAG.PREVIEW_PHOTO_FRAGMENT_TAG.equals(fragmentTag)) {
            return new PreviewPhotoFragment();
        } else if (TAG.POPULATE_PHOTO_INFO_FRAGMENT_TAG.equals(fragmentTag)) {
            return new PopulatePhotoInfoFragment();
        } else if (TAG.POSTPONED_PHOTOS_FRAGMENT_TAG.equals(fragmentTag)) {
            return new PostponedPhotosFragment();
        } else if (TAG.UPLOADED_PHOTOS_FRAGMENT_TAG.equals(fragmentTag)) {
            return new UploadedPhotosFragment();
        } else if (TAG.SETTINGS_FRAGMENT_TAG.equals(fragmentTag)) {
            return new SettingsFragment();
        } else if (TAG.PREPARE_USER_ACTION_BAR_FRAGMENT_TAG.equalsIgnoreCase(fragmentTag)) {
            return new PrepareUserActionBarFragment();
        }

        return null;
    }

    /**
     *
     */
    public void showMapFragment() {
        showFragment(TAG.MAP_FRAGMENT_TAG);
    }

    /**
     *
     */
    public void showMapSettingsFragment() {
        showFragment(TAG.MAP_SETTINGS_FRAGMENT_TAG);
    }

    /**
     *
     * @param bundle
     */
    public void showPhotoBrowsingFragment(Bundle bundle) {
        showFragment(TAG.PHOTO_BROWSING_FRAGMENT_TAG, bundle);
    }

    /**
     *
     */
    public void showGetPhotoFragment() {
        showFragment(TAG.GET_PHOTO_FRAGMENT_TAG);
    }

    /**
     *
     * @param bundle
     */
    public void showPreviewPhotoFragment(Bundle bundle) {
        showFragment(TAG.PREVIEW_PHOTO_FRAGMENT_TAG, bundle);
    }

    /**
     *
     * @param bundle
     */
    public void showPopulatePhotoInfoFragment(Bundle bundle) {
        showFragment(TAG.POPULATE_PHOTO_INFO_FRAGMENT_TAG, bundle);
    }

    /**
     *
     */
    public void showPostponedPhotosFragment() {
       Bundle bundle = new Bundle();
       bundle.putString(PrepareUserActionBarFragment.SELECTED_TAB_KEY, ActionBarUtil.TabTag.POSTPONED_PHOTOS_TAB.toString());
       showFragment(TAG.PREPARE_USER_ACTION_BAR_FRAGMENT_TAG, bundle, false);
    }

    /**
     *
     */
    public void showUploadedPhotosFragment() {
       // ActionBarUtil.initTabActionBar(activity, ActionBarUtil.TabTag.UPLOADED_PHOTOS_TAB, this);
        Bundle bundle = new Bundle();
        bundle.putString(PrepareUserActionBarFragment.SELECTED_TAB_KEY, ActionBarUtil.TabTag.UPLOADED_PHOTOS_TAB.toString());
        showFragment(TAG.PREPARE_USER_ACTION_BAR_FRAGMENT_TAG, bundle, false);
    }

    /**
     *
     */
    public void showSettingsFragment() {
        showFragment(TAG.SETTINGS_FRAGMENT_TAG);
    }

    /**
     *
     */
    public void refreshCurrentFragment() {
        String currentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        showFragment(currentTag, null, false);
    }

    /**
     *
     */
    public void handleActivityCreation() {
        if (fragmentManager.getBackStackEntryCount() == 0) {
            // Display start fragment here
            showMapFragment();
        }
    }

    /**
     *
     * @param fragmentManager
     */
    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        ActionBarUtil.TabTag selectedTab = (ActionBarUtil.TabTag) tab.getTag();
        if (selectedTab == ActionBarUtil.TabTag.POSTPONED_PHOTOS_TAB) {
            showFragment(TAG.POSTPONED_PHOTOS_FRAGMENT_TAG);
        } else if (selectedTab == ActionBarUtil.TabTag.UPLOADED_PHOTOS_TAB) {
            showFragment(TAG.UPLOADED_PHOTOS_FRAGMENT_TAG);
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {}

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {}
}
