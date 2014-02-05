package com.jrew.geocatch.mobile.util;

import com.jrew.geocatch.mobile.model.PostponedImage;

import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 05.02.14
 * Time: 10:02
 * To change this template use File | Settings | File Templates.
 */
public class PostponedImageDescComparator implements Comparator<PostponedImage> {

    /** **/
    private static final int GREATER = 1;

    /** **/
    private static final int LOWER = 1;

    /** **/
    private static final int EQUAL = 1;

    @Override
    public int compare(PostponedImage first, PostponedImage second) {

        if (first != null && second != null) {
            long firstId = first.getId();
            long secondId = second.getId();
            if (firstId > secondId) {
                return LOWER;
            } else if (firstId < secondId) {
                return GREATER;
            } else {
                return EQUAL;
            }
        } else if(first == null && second != null) {
            return GREATER;
        } else if(first != null && second == null) {
            return LOWER;
        } else {
            return EQUAL;
        }
    }
}
