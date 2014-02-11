package com.jrew.geocatch.mobile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.util.LayoutUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 12.11.13
 * Time: 16:56
 * To change this template use File | Settings | File Templates.
 */
public class FragmentSwitcher {

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
    }

    /** **/
    private FragmentManager fragmentManager;

    /** **/
    private String displayingFragmentTag;

    /**
     *
     * @param fragmentManager
     */
    public FragmentSwitcher(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
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
     * @param fragmentData
     * @param addToBackStack
     */
    private void showFragment(String fragmentTag, Bundle fragmentData, boolean addToBackStack) {

        displayingFragmentTag = fragmentTag;
        Fragment fragment = getFragment(fragmentTag);

        if (fragmentData != null) {
            Bundle bundle = fragment.getArguments();
            if (bundle != null ) {
                bundle.clear();
                bundle.putAll(fragmentData);
            } else {
                fragment.setArguments(fragmentData);
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
        showPostponedPhotosFragment(true);
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
        Bundle bundle = new Bundle();
        bundle.putBoolean(LayoutUtil.REFRESH_FRAGMENT_FLAG, true);
        showFragment(displayingFragmentTag);
    }

    /**
     *
     * @param selectTab
     */
    public void showPostponedPhotosFragment(boolean selectTab) {

        Bundle bundle = null;
        if (selectTab) {
            bundle = new Bundle();
            bundle.putString(UserPhotosFragment.SELECTED_TAB_KEY,
                    UserPhotosFragment.TabTag.POSTPONED_PHOTOS_TAB.toString());
        }
        showFragment(TAG.POSTPONED_PHOTOS_FRAGMENT_TAG, bundle);

    }

    /**
     *
     */
    public void showUploadedPhotosFragment() {
        showUploadedPhotosFragment(true);
    }

    /**
     *
     * @param selectTab
     */
    public void showUploadedPhotosFragment(boolean selectTab) {

        Bundle bundle = null;
        if (selectTab) {
            bundle = new Bundle();
            bundle.putString(UserPhotosFragment.SELECTED_TAB_KEY,
                    UserPhotosFragment.TabTag.UPLOADED_PHOTOS_TAB.toString());
        }
        showFragment(TAG.UPLOADED_PHOTOS_FRAGMENT_TAG, bundle);
    }

    /**
     *
     */
    public void handleActivityCreation() {
        if (displayingFragmentTag == null) {
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
}
