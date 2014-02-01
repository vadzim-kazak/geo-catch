package com.jrew.geocatch.mobile.util;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 2/1/14
 * Time: 1:58 PM
 */
public class StringUtil {

    public static boolean equals(String first, String second) {

        if (first != null && second != null) {
            return first.equalsIgnoreCase(second);
        } else if (first == null && second == null) {
            return true;
        } else {
            return false;
        }
    }

}
