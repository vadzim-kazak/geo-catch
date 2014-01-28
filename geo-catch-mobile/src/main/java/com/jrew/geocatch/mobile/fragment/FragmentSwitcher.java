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
        String IMAGE_VIEW_FRAGMENT_TAG = "viewImageFragmentTag";

        /** **/
        String IMAGE_TAKE_CAMERA_FRAGMENT_TAG = "takeImageCameraFragmentTag";

        /** **/
        String IMAGE_TAKE_PREVIEW_FRAGMENT_TAG = "takeImagePreviewFragmentTag";

        /** **/
        String IMAGE_TAKE_INFO_FRAGMENT_TAG = "takeImageInfoFragmentTag";

        /** **/
        String OWN_IMAGES_FRAGMENT_TAG = "ownImagesFragmentTag";
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

        Fragment fragment = fragmentManager.findFragmentByTag(fragmentTag);
        if (fragment == null) {
            fragment = createFragment(fragmentTag);
        }

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
    private Fragment createFragment(String fragmentTag) {

        if (TAG.MAP_FRAGMENT_TAG.equals(fragmentTag)){
            return new MapFragment();
        } else if (TAG.MAP_SETTINGS_FRAGMENT_TAG.equals(fragmentTag)) {
            return new MapSettingsFragment();
        } else if (TAG.IMAGE_VIEW_FRAGMENT_TAG.equals(fragmentTag)) {
            return new ImageViewFragment();
        } else if (TAG.IMAGE_TAKE_CAMERA_FRAGMENT_TAG.equals(fragmentTag)) {
            return new ImageTakeCameraFragment();
        } else if (TAG.IMAGE_TAKE_PREVIEW_FRAGMENT_TAG.equals(fragmentTag)) {
            return new ImageTakePreviewFragment();
        } else if (TAG.IMAGE_TAKE_INFO_FRAGMENT_TAG.equals(fragmentTag)) {
            return new ImageTakeInfoFragment();
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
    public void showImageViewFragment(Bundle bundle) {
        showFragment(TAG.IMAGE_VIEW_FRAGMENT_TAG, bundle);
    }

    /**
     *
     */
    public void showImageTakeCameraFragment() {
        showFragment(TAG.IMAGE_TAKE_CAMERA_FRAGMENT_TAG);
    }

    /**
     *
     * @param bundle
     */
    public void showImageTakePreviewFragment(Bundle bundle) {
        showFragment(TAG.IMAGE_TAKE_PREVIEW_FRAGMENT_TAG, bundle);
    }

    /**
     *
     * @param bundle
     */
    public void showImageTakeInfoFragment(Bundle bundle) {
        showFragment(TAG.IMAGE_TAKE_INFO_FRAGMENT_TAG, bundle);
    }

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
