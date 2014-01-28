package com.jrew.geocatch.mobile.fragment;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.activity.MainActivity;
import com.jrew.geocatch.mobile.util.ActionBarHolder;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;
import com.jrew.geocatch.mobile.util.LayoutUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 13.11.13
 * Time: 13:06
 * To change this template use File | Settings | File Templates.
 */
public class ImageTakeCameraFragment extends SherlockFragment {

    /** **/
    private final static double CAMERA_SIDES_RATIO = 4d / 3;

    /** **/
    private final static int PREVIEW_AREA_MARGIN = 0;

    /** **/
    private final static int DEFAULT_PICTURE_WIDTH = 640;

    /** **/
    private final static int DEFAULT_PICTURE_HEIGHT = 480;

    /** **/
    private SurfaceView preview;

    /** **/
    private SurfaceHolder previewHolder;

    /** **/
    private Camera camera;

    /** **/
    private boolean inPreview;

    /** **/
    Bitmap bmp;

    /** **/
    static Bitmap mutableBitmap;

    /** **/
    File imageFileName;

    /** **/
    File imageFileFolder;

    /** **/
    private MediaScannerConnection msConn;

    /** **/
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Action bar subtitle
        ActionBar actionBar = ActionBarHolder.getActionBar();
        actionBar.setSubtitle(getResources().getString(R.string.takeImageLabel));

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        View result = inflater.inflate(R.layout.image_take_camera_fragment, container, false);

        //image=(ImageView)findViewById(R.id.image);
        preview = (SurfaceView) result.findViewById(R.id.surface);

        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        FragmentActivity activity =  getActivity();
        WindowManager windowManager = activity.getWindowManager();

        Rect displaySize = new Rect();
        windowManager.getDefaultDisplay().getRectSize(displaySize);

        previewHolder.setFixedSize(displaySize.width(), displaySize.width());

        return result;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_take_photo, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int pressedMenuItemId = item.getItemId();
        FragmentSwitcher fragmentSwitcher = FragmentSwitcherHolder.getFragmentSwitcher();

        switch (pressedMenuItemId) {
            case R.id.backMenuOption:
                fragmentSwitcher.showMapFragment();
                break;
            case R.id.takePhotoMenuOption:
                camera.takePicture(null, null, photoCallback);
                inPreview = false;
                break;
        }

        return true;
    }



    @Override
    public void onResume() {
        super.onResume();
        camera = Camera.open();
    }

    @Override
    public void onPause() {
        if (inPreview) {
            camera.stopPreview();
        }

        camera.release();
        camera = null;
        inPreview = false;
        super.onPause();
    }

    /**
     *
     */
    SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {

        /**
         *
         * @param holder
         */
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera.setPreviewDisplay(previewHolder);
            }
            catch (Throwable t) {
                Log.e("PreviewDemo-surfaceCallback", "Exception in setPreviewDisplay()", t);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG)
                     .show();
            }
        }

        /**
         *
         * @param holder
         * @param format
         * @param width
         * @param height
         */
        public void surfaceChanged(SurfaceHolder holder,
                                   int format, int width,
                                   int height) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size previewSize = getBestPreviewCameraSize(width, height, parameters);

            parameters.setPreviewSize(previewSize.width, previewSize.height);

            // Resize surface view
            double scaleFactor = LayoutUtil.getViewScaleFactor(new Point(width, height), new Point(previewSize.width, previewSize.height), PREVIEW_AREA_MARGIN);
            preview.setLayoutParams(new LinearLayout.LayoutParams((int)(previewSize.height * scaleFactor),(int)(previewSize.width * scaleFactor)));

            Camera.Size pictureSize = getPictureCameraSize(parameters);
            parameters.setPictureSize(pictureSize.width, pictureSize.height);

            PackageManager packageManager = getActivity().getPackageManager();
            if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)){
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }

            camera.setParameters(parameters);
            camera.setDisplayOrientation(90);
            camera.startPreview();

            inPreview = true;
        }

        /**
         *
         * @param holder
         */
        public void surfaceDestroyed(SurfaceHolder holder) {
            // no-op
        }
    };

    /**
     *
     * @param width
     * @param height
     * @param parameters
     * @return
     */
    private Camera.Size getBestPreviewCameraSize(int width, int height, Camera.Parameters parameters){

        Camera.Size result = null;
        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height && (((double)size.width/size.height ) == CAMERA_SIDES_RATIO)) {
                if (result == null) {
                    result = size;
                }
                else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;
                    if (newArea > resultArea) {
                        result = size;
                    }
                }
            }
        }

        return result;
    }

    /**
     *
     * @param parameters
     * @return
     */
    private Camera.Size getPictureCameraSize(Camera.Parameters parameters){

        boolean supportDefaultWidth = false;
        boolean supportDefaultHeight = false;
        for (Camera.Size size : parameters.getSupportedPictureSizes()) {

            // Need this for case when camera isn't support default photo size
            if (size.width == DEFAULT_PICTURE_WIDTH) {
                supportDefaultWidth = true;
            }

            // Need this for case when camera isn't support default photo size
            if (size.height == DEFAULT_PICTURE_HEIGHT) {
                supportDefaultHeight = true;
            }

            if (size.width == DEFAULT_PICTURE_WIDTH && size.height == DEFAULT_PICTURE_HEIGHT) {
                return size;
            }
        }

        // Default photo size isn't supported
        // Try to find some similar to default
        Camera.Size result = parameters.getSupportedPictureSizes().get(0);
        if (supportDefaultWidth || supportDefaultHeight) {

            int widthDelta = DEFAULT_PICTURE_WIDTH;
            int nearestWidth = 0;

            int heightDelta = DEFAULT_PICTURE_HEIGHT;
            int nearestHeight = 0;

            for (Camera.Size size : parameters.getSupportedPictureSizes()) {

                if (supportDefaultWidth) {
                    int currentHeightDelta = Math.abs(DEFAULT_PICTURE_HEIGHT - size.height);
                    if (currentHeightDelta < heightDelta) {
                        heightDelta = currentHeightDelta;
                        nearestHeight = size.height;
                    }
                }

                if (supportDefaultHeight) {
                    int currentWidthDelta = Math.abs(DEFAULT_PICTURE_WIDTH - size.width);
                    if (currentWidthDelta < widthDelta) {
                        widthDelta = currentWidthDelta;
                        nearestWidth = size.width;
                    }
                }
            }

            if (supportDefaultWidth) {
                result.width = DEFAULT_PICTURE_WIDTH;
                result.height = nearestHeight;
            }

            if (supportDefaultHeight) {
                result.width = nearestWidth;
                result.height = DEFAULT_PICTURE_HEIGHT;
            }

            return result;

        } else {

            // Get closest to default photo resolution by square
            int defaultPhotoSquare = DEFAULT_PICTURE_WIDTH * DEFAULT_PICTURE_HEIGHT;
            int squareDelta = defaultPhotoSquare;
            for (Camera.Size size : parameters.getSupportedPictureSizes()) {
                int currentSquareDelta = Math.abs(defaultPhotoSquare - size.width * size.height);
                if (currentSquareDelta < squareDelta) {
                    squareDelta = currentSquareDelta;
                    result = size;
                }
            }

            return result;
        }
    }

    /**
     *
     */
    Camera.PictureCallback photoCallback = new Camera.PictureCallback() {

        public void onPictureTaken(final byte[] data, final Camera camera) {

            dialog = ProgressDialog.show(getActivity(), "", "Saving Photo");
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(1000);
                    }
                    catch(Exception ex){}
                    onPictureTake(data, camera);
                }
            }.start();
        }
    };


    /**
     *
     * @param data
     * @param camera
     */
    public void onPictureTake(byte[] data, Camera camera){

        bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        mutableBitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);

        Bundle bundle = new Bundle();
        bundle.putParcelable("bmp", mutableBitmap);
        bundle.putBoolean("isRotated", true);

        MainActivity activity = (MainActivity) getActivity();
        FragmentSwitcherHolder.getFragmentSwitcher().showImageTakePreviewFragment(bundle);

        dialog.dismiss();
    }
}
