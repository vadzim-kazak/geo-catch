package com.jrew.geocatch.mobile.util;

import android.graphics.Point;
import android.hardware.Camera;

/**
 *
 */
public class LayoutUtil {

    /**
     *
     * @param maxLayoutSize
     * @param actualLayoutSize
     * @param margin
     * @return
     */
    public static double getViewScaleFactor(Point maxLayoutSize,
                                      Point actualLayoutSize,
                                      int margin) {

        int smallerLayoutSize = maxLayoutSize.x;
        if (maxLayoutSize.y < maxLayoutSize.x) {
            smallerLayoutSize = maxLayoutSize.y;
        }

        int smallerActualViewSize = actualLayoutSize.x;
        if (actualLayoutSize.y < actualLayoutSize.x) {
            smallerActualViewSize = actualLayoutSize.y;
        }

        return  ((double) smallerLayoutSize - 2 * margin) / smallerActualViewSize;
    }

    /**
     *
     * @param layoutWidth
     * @param viewWidth
     * @param margin
     * @return
     */
    public static double getViewWidthScaleFactor(int layoutWidth,
                                            int viewWidth,
                                            int margin) {

        return  ((double) layoutWidth - 2 * margin) / viewWidth;
    }

}
