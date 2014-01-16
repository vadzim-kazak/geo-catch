package com.jrew.geocatch.mobile.util;

import android.support.v4.app.FragmentManager;
import com.jrew.geocatch.mobile.fragment.FragmentSwitcher;

/**
 * Fragment switcher holder.
 */
public class FragmentSwitcherHolder {

    /** **/
    private static FragmentSwitcher fragmentSwitcher;

    public static void initFragmentSwitcher(FragmentManager fragmentManager) {
        if (fragmentSwitcher == null) {
            fragmentSwitcher = new FragmentSwitcher(fragmentManager);
        } else {
            fragmentSwitcher.setFragmentManager(fragmentManager);
        }
    }

    /**
     *
     * @return
     */
    public static FragmentSwitcher getFragmentSwitcher() {
        return fragmentSwitcher;
    }
}

