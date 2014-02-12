package com.jrew.geocatch.mobile.util;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import com.jrew.geocatch.mobile.fragment.FragmentSwitcher;

/**
 * Fragment switcher holder.
 */
public class FragmentSwitcherHolder {

    /** **/
    private static FragmentSwitcher fragmentSwitcher;

    public static void initFragmentSwitcher(FragmentManager fragmentManager, Activity activity) {
        fragmentSwitcher = new FragmentSwitcher(fragmentManager, activity);
    }

    /**
     *
     * @return
     */
    public static FragmentSwitcher getFragmentSwitcher() {
        return fragmentSwitcher;
    }
}

