package com.jrew.geocatch.mobile.menu;

import android.app.Activity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.fragment.FragmentSwitcher;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 01.11.13
 * Time: 14:37
 * To change this template use File | Settings | File Templates.
 */
public class MenuHelper {

    /** **/
    public static int SELECTED_ACTION_BAR_ITEM_COLOR = android.R.color.darker_gray;

    /** **/
    public static int USUAL_ACTION_BAR_ITEM_COLOR = android.R.color.transparent;

    /** **/
    private Menu menu;

    /** **/
    private Activity activity;

    /** **/
    private boolean isReady;

    /** **/
    private int selectedItemId;

    /**
     *
     * @param menu
     */
    public MenuHelper(Menu menu, Activity activity) {
        this.menu = menu;
        this.activity = activity;
    }

    /**
     *
     */
    public void init() {
        isReady = true;
        makeViewSelected(R.id.viewMapMenuOption);
    }

    /**
     *
     * @param item
     */
    public void onOptionsItemSelected(MenuItem item) {

        int pressedMenuItemId = item.getItemId();
        if (pressedMenuItemId != selectedItemId) {

            selectedItemId = pressedMenuItemId;

            setUnselectedBackgrounds();

            FragmentSwitcher fragmentSwitcher = FragmentSwitcherHolder.getFragmentSwitcher();

            switch (selectedItemId) {

                case R.id.viewMapMenuOption:
                    fragmentSwitcher.showMapFragment();
                    break;

                case R.id.viewSettingsMenuOption:
                    fragmentSwitcher.showMapSettingsFragment();
                    break;

                /**
                 case R.id.viewImageMenuOption:
                 actionBar.setSubtitle(resources.getString(R.string.viewImageLabel));
                 activity.initFragmentSwitcher().showImageViewFragment(null);
                 break;
                 **/

                case R.id.takeImageMenuOption:
                    fragmentSwitcher.showImageTakeCameraFragment();
                    break;

                case R.id.ownImagesMenuOption:
                    break;

                default:
                    break;
            }
        }
    }

    /**
     *
     */
    public void setUnselectedBackgrounds() {
        for (int i = 0; i < menu.size(); i++) {
            int itemId = menu.getItem(i).getItemId();
            activity.findViewById(itemId).setBackgroundResource(USUAL_ACTION_BAR_ITEM_COLOR);
        }
    }

    /**
     *
     * @return
     */
    public boolean isReady() {
        return isReady;
    }

    /**
     *
     * @param itemId
     */
    public void makeViewSelected(int itemId) {
        if (isReady) {
            activity.findViewById(itemId).setBackgroundResource(MenuHelper.SELECTED_ACTION_BAR_ITEM_COLOR);
        }
    }
}
