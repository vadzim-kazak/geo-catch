package com.jrew.geocatch.mobile.menu;

import android.app.Activity;
import com.actionbarsherlock.internal.view.menu.MenuWrapper;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 01.11.13
 * Time: 14:37
 * To change this template use File | Settings | File Templates.
 */
public class MenuHelper {

    /** **/
    private static int SELECTED_ACTION_BAR_ITEM_COLOR = android.R.color.darker_gray;

    /** **/
    private static int USUAL_ACTION_BAR_ITEM_COLOR = android.R.color.transparent;

    /** **/
    private Menu menu;

    /**
     *
     * @param menu
     */
    public MenuHelper(Menu menu) {
        this.menu = menu;
    }

    /**
     *
     * @param selectedMenuItem
     */
    public void processMenuItemBackgroundSelection(MenuItem selectedMenuItem, Activity activity) {

        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            int menuItemId = menuItem.getItemId();
            if (menuItemId == selectedMenuItem.getItemId()) {
                // Set selection
                activity.findViewById(menuItemId).setBackgroundResource(SELECTED_ACTION_BAR_ITEM_COLOR);
            } else {
                // Clear selection
                activity.findViewById(menuItemId).setBackgroundResource(USUAL_ACTION_BAR_ITEM_COLOR);
            }
        }
    }

}
