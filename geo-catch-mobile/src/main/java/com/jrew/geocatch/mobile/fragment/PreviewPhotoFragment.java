package com.jrew.geocatch.mobile.fragment;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.util.ActionBarHolder;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;
import com.jrew.geocatch.mobile.util.LayoutUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 13.11.13
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
public class PreviewPhotoFragment extends SherlockFragment {

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
        actionBar.setSubtitle(getResources().getString(R.string.previewPhotoFragmentLabel));

        View result = inflater.inflate(R.layout.preview_photo_fragment, container, false);
        imageView = (ImageView) result.findViewById(R.id.imagePreview);

        final Bundle fragmentData = getArguments();
        if (fragmentData != null && !fragmentData.isEmpty()) {
            image = (Bitmap) fragmentData.getParcelable("bmp");
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

    /**
     *
     */
    private void displayImage() {
        imageView.setImageBitmap(image);

        Display display = getActivity().getWindowManager().getDefaultDisplay();

        double scaleFactor = LayoutUtil.getViewWidthScaleFactor(display.getWidth(), image.getWidth(), 0);
        imageView.setLayoutParams(new LinearLayout.LayoutParams((int) (image.getWidth() * scaleFactor), (int) (image.getHeight() * scaleFactor)));
    }
}
