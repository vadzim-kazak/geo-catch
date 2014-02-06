package com.jrew.geocatch.mobile.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.jrew.geocatch.mobile.R;

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
        displayingFragmentTag = fragmentTag;
        showFragment(fragmentTag, null);
    }

    /**
     *
     * @param fragmentTag
     * @param fragmentData
     */
    private void showFragment(String fragmentTag, Bundle fragmentData) {

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
        transaction.replace(R.id.fragment_container, fragment, fragmentTag);
        transaction.addToBackStack(null);
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
     * @param selectTab
     */
    public void showPostponedPhotosFragment(boolean selectTab) {
        showFragment(TAG.POSTPONED_PHOTOS_FRAGMENT_TAG);
        if (selectTab) {
            UserPhotosFragment fragment = (UserPhotosFragment) getFragment(TAG.POSTPONED_PHOTOS_FRAGMENT_TAG);
            fragment.selectTabByTag(UserPhotosFragment.TabTag.POSTPONED_PHOTOS_TAB);
        }
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
        showFragment(TAG.UPLOADED_PHOTOS_FRAGMENT_TAG);
        if (selectTab) {
            UserPhotosFragment fragment = (UserPhotosFragment) getFragment(TAG.UPLOADED_PHOTOS_FRAGMENT_TAG);
            fragment.selectTabByTag(UserPhotosFragment.TabTag.UPLOADED_PHOTOS_TAB);
        }
    }

    /**
     *
     */
    public void handleActivityCreation() {
        if (displayingFragmentTag != null) {
            showFragment(displayingFragmentTag);
        } else {
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
