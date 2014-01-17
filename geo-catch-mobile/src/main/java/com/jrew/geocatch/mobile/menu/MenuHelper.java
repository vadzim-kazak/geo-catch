package com.jrew.geocatch.mobile.menu;

import android.app.Activity;
import android.os.Handler;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.fragment.FragmentSwitcher;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;
import com.jrew.geocatch.mobile.util.MenuHelperHolder;

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
     * @param activity
     */
    public MenuHelper(Activity activity) {
        this.activity = activity;
    }

    /**
     *
     */
    public void init() {
        isReady = true;
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
     * @param handler
     */
    public void makeViewSelected(final int itemId, final Handler handler) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                // Wait until menu will be initialized
                MenuHelper menuHelper = MenuHelperHolder.getMenuHelper();
                while (menuHelper == null || !menuHelper.isReady()){
                    menuHelper = MenuHelperHolder.getMenuHelper();
                }

                // Mark menu selection
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        activity.findViewById(itemId).setBackgroundResource(MenuHelper.SELECTED_ACTION_BAR_ITEM_COLOR);
                    }
                });
            }
        };
        new Thread(runnable).start();
    }

    /**
     *
     * @return
     */
    public Menu getMenu() {
        return menu;
    }

    /**
     *
     * @param menu
     */
    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
