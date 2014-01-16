package com.jrew.geocatch.mobile.util;

import com.jrew.geocatch.mobile.menu.MenuHelper;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 16.01.14
 * Time: 18:55
 * To change this template use File | Settings | File Templates.
 */
public class MenuHelperHolder {

    /** **/
    private static MenuHelper menuHelper;

    /**
     *
     * @return
     */
    public static MenuHelper getMenuHelper() {
        return menuHelper;
    }

    /**
     *
     * @param menuHelper
     */
    public static void setMenuHelper(MenuHelper menuHelper) {
        MenuHelperHolder.menuHelper = menuHelper;
    }
}
