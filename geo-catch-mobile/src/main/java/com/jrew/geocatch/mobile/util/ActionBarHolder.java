package com.jrew.geocatch.mobile.util;

import com.actionbarsherlock.app.ActionBar;

/**
 * Holder for Action bar instance
 */
public class ActionBarHolder {

    /** **/
    private static ActionBar actionBar;

    /**
     *
     * @return
     */
    public static ActionBar getActionBar() {
        return actionBar;
    }

    /**
     *
     * @param actionBar
     */
    public static void setActionBar(ActionBar actionBar) {
        ActionBarHolder.actionBar = actionBar;
    }
}
