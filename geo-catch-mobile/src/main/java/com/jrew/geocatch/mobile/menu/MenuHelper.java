package com.jrew.geocatch.mobile.menu;

import android.content.res.Resources;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.activity.MainActivity;

import java.util.HashMap;
import java.util.Map;

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
    private MainActivity activity;

    private Map<Integer, Boolean> menuSelection;

    /**
     *
     * @param menu
     */
    public MenuHelper(Menu menu, MainActivity activity) {
        this.menu = menu;
        this.activity = activity;
        menuSelection = new HashMap<Integer, Boolean>();
    }

    /**
     *
     */
    public void init() {

        setDefaultBackground();
        markMenuOptionsUnselected();

        // As menu helper is responsible for action bar title setting itis decided
        // to init action bar title here
        activity.findViewById(R.id.viewMapMenuOption).setBackgroundResource(SELECTED_ACTION_BAR_ITEM_COLOR);
        final ActionBar actionBar = activity.getSupportActionBar();
        final Resources resources = activity.getResources();
        actionBar.setSubtitle(resources.getString(R.string.mapLabel));
        menuSelection.put(R.id.viewMapMenuOption, true);
    }

    /**
     *
     * @param item
     */
    public void onOptionsItemSelected(MenuItem item) {

        final ActionBar actionBar = activity.getSupportActionBar();
        final Resources resources = activity.getResources();

        if(isNewOptionMenuSelected(item)) {

            markMenuOptionsUnselected();
            menuSelection.put(item.getItemId(), true);

            processMenuItemBackgroundSelection(item);

            switch (item.getItemId()) {

                case R.id.viewMapMenuOption:
                    actionBar.setSubtitle(resources.getString(R.string.mapLabel));
                    activity.getFragmentSwitcher().showMapFragment();
                    break;

                case R.id.viewSettingsMenuOption:
                    actionBar.setSubtitle(resources.getString(R.string.viewSettingsLabel));
                    activity.getFragmentSwitcher().showMapSettingsFragment();
                    break;

                case R.id.viewImageMenuOption:
                    actionBar.setSubtitle(resources.getString(R.string.viewImageLabel));
                    activity.getFragmentSwitcher().showImageViewFragment(null);
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
    }

    /**
     *
     * @param selectedMenuItem
     * @return
     */
    private boolean isNewOptionMenuSelected(MenuItem selectedMenuItem) {

        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            int menuItemId = menuItem.getItemId();

            if (menuSelection.get(menuItemId) &&
                selectedMenuItem.getItemId() != menuItemId) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     */
    private void markMenuOptionsUnselected() {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            int menuItemId = menuItem.getItemId();
            menuSelection.put(menuItemId, false);
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
