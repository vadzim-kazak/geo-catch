package com.jrew.geocatch.mobile.util;

import com.squareup.picasso.Picasso;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 28.02.14
 * Time: 18:33
 * To change this template use File | Settings | File Templates.
 */
public class PicassoHolder {

    /** **/
    private static Picasso picasso;

    /**
     *
     * @return
     */
    public static Picasso getPicasso() {
        return picasso;
    }

    /**
     *
     * @param picasso
     */
    public static void setPicasso(Picasso picasso) {
        PicassoHolder.picasso = picasso;
    }
}
