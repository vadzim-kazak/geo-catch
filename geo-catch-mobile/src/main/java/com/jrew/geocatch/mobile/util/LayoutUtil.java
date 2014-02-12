package com.jrew.geocatch.mobile.util;

import android.app.Activity;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.jrew.geocatch.mobile.R;

/**
 *
 */
public class LayoutUtil {

    /** **/
    public static final String REFRESH_FRAGMENT_FLAG = "refreshFragmentFlag";

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

    /**
     *
     * @param activity
     */
    public static void showNoConnectionLayout(Activity activity, Fragment fragment) {

        FrameLayout fragmentContainer = (FrameLayout) activity.findViewById(R.id.fragmentContainer);
        if (fragmentContainer != null && fragmentContainer.getVisibility() != View.GONE) {
            fragmentContainer.setVisibility(View.GONE);
        }

        LinearLayout noNetworkConnectionLayout = (LinearLayout) activity.findViewById(R.id.noNetworkConnectionLayout);
        if (noNetworkConnectionLayout != null) {
            if (noNetworkConnectionLayout.getVisibility() != View.VISIBLE) {
                noNetworkConnectionLayout.setVisibility(View.VISIBLE);
            }

            ImageView refreshImageView = (ImageView) noNetworkConnectionLayout.findViewById(R.id.refreshNetworkImageView);
            refreshImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentSwitcherHolder.getFragmentSwitcher().refreshCurrentFragment();
                }
            });

        }

        Toast.makeText(activity, activity.getResources().getString(R.string.noNetworkConnectionError),
            Toast.LENGTH_SHORT).show();
    }

    /**       l
     *
     * @param activity
     */
    public static void showFragmentContainer(Activity activity) {

        LinearLayout noNetworkConnectionLayout = (LinearLayout) activity.findViewById(R.id.noNetworkConnectionLayout);
        if (noNetworkConnectionLayout != null && noNetworkConnectionLayout.getVisibility() != View.GONE) {
            noNetworkConnectionLayout.setVisibility(View.GONE);
        }

        FrameLayout fragmentContainer = (FrameLayout) activity.findViewById(R.id.fragmentContainer);
        if (fragmentContainer != null && fragmentContainer.getVisibility() != View.VISIBLE) {
            fragmentContainer.setVisibility(View.VISIBLE);
        }
    }
}
