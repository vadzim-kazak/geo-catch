package com.jrew.geocatch.mobile.fragment;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.activity.MainActivity;
import com.jrew.geocatch.mobile.util.ActionBarHolder;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;
import com.jrew.geocatch.mobile.util.LayoutUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 13.11.13
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
public class ImageTakePreviewFragment extends SherlockFragment {

    /** **/
    private static final int IMAGE_VIEW_MARGIN = 0;

    /** **/
    private Bitmap image;

    /** **/
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Action bar subtitle
        ActionBar actionBar = ActionBarHolder.getActionBar();
        actionBar.setSubtitle(getResources().getString(R.string.photoPreviewLabel));

        View result = inflater.inflate(R.layout.image_take_preview_fragment, container, false);
        imageView = (ImageView) result.findViewById(R.id.imagePreview);

        final Bundle fragmentData = getArguments();
        if (fragmentData != null && !fragmentData.isEmpty()) {
            image = (Bitmap) fragmentData.getParcelable("bmp");

            if(fragmentData.getBoolean("isRotated", false)) {
                // Initially image rotated to 90 degrees CCW
                rotateImage(90);
                fragmentData.putBoolean("isRotated", false);
            }

            displayImage();
        }

        return result;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_preview_photo, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int pressedMenuItemId = item.getItemId();
        FragmentSwitcher fragmentSwitcher = FragmentSwitcherHolder.getFragmentSwitcher();
        switch (pressedMenuItemId) {
            case R.id.backMenuOption:
                getSherlockActivity().getSupportFragmentManager().popBackStack();
                break;

            case R.id.revertCCWMenuOption:
                if (image != null) {
                    rotateImage(-90);
                    displayImage();
                }
                break;

            case R.id.revertCWMenuOption:
                if (image != null) {
                    rotateImage(90);
                    displayImage();
                }
                break;

            case R.id.forwardMenuOption:
                final Bundle fragmentData = getArguments();
                if (fragmentData != null) {
                    fragmentData.putParcelable("bmp", image);
                }
                FragmentSwitcherHolder.getFragmentSwitcher().showImageTakeInfoFragment(new Bundle(fragmentData));
                break;
        }

        return true;
    }

    /**
     *
     * @param degree
     */
    private void rotateImage(int degree) {

        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(),
                matrix, true);
    }

    private void displayImage() {
        imageView.setImageBitmap(image);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);

        double scaleFactor = LayoutUtil.getViewWidthScaleFactor(displaySize.x, image.getWidth(), IMAGE_VIEW_MARGIN);
        imageView.setLayoutParams(new LinearLayout.LayoutParams((int) (image.getWidth() * scaleFactor), (int) (image.getHeight() * scaleFactor)));
    }
}
