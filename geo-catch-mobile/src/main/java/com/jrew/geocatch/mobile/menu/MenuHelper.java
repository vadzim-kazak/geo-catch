package com.jrew.geocatch.mobile.menu;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v4.view.MenuItemCompat;
import android.view.View;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.internal.view.menu.MenuWrapper;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jrew.geocatch.mobile.R;

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

    /** **/
    private SherlockFragmentActivity activity;

    /**
     *
     * @param menu
     */
    public MenuHelper(Menu menu, SherlockFragmentActivity activity) {
        this.menu = menu;
        this.activity = activity;
    }

    /**
     *
     */
    public void init() {

        setDefaultBackground();

        // As menu helper is responsible for action bar title setting itis decided
        // to init action bar title here
        activity.findViewById(R.id.viewMapMenuOption).setBackgroundResource(SELECTED_ACTION_BAR_ITEM_COLOR);
        final ActionBar actionBar = activity.getSupportActionBar();
        final Resources resources = activity.getResources();
        actionBar.setSubtitle(resources.getString(R.string.mapLabel));
    }

    /**
     *
     * @param item
     */
    public void onOptionsItemSelected(MenuItem item) {

        final ActionBar actionBar = activity.getSupportActionBar();
        final Resources resources = activity.getResources();

        processMenuItemBackgroundSelection(item);

        switch (item.getItemId()) {

            case R.id.viewMapMenuOption:
                actionBar.setSubtitle(resources.getString(R.string.mapLabel));
                break;

            case R.id.viewSettingsMenuOption:
                actionBar.setSubtitle(resources.getString(R.string.viewSettingsLabel));
                break;

            case R.id.viewImageMenuOption:
                actionBar.setSubtitle(resources.getString(R.string.viewImageLabel));
                break;

            case R.id.takeImageMenuOption:
                actionBar.setSubtitle(resources.getString(R.string.takeImageLabel));
                break;

            case R.id.ownImagesMenuOption:
                actionBar.setSubtitle(resources.getString(R.string.ownImagesLabel));
                break;

            default:
                break;
        }
    }


    /**
     *
     */
    private void processMenuItemBackgroundSelection(MenuItem selectedMenuItem) {

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

    /**
     *
     */
    public void setDefaultBackground() {
        for (int i = 0; i < menu.size(); i++) {
            int itemId = menu.getItem(i).getItemId();
            activity.findViewById(itemId).setBackgroundResource(USUAL_ACTION_BAR_ITEM_COLOR);
        }
    }
}
