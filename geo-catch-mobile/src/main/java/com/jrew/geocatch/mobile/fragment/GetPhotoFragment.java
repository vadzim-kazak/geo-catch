package com.jrew.geocatch.mobile.fragment;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.*;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.activity.MainActivity;
import com.jrew.geocatch.mobile.util.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 13.11.13
 * Time: 13:06
 * To change this template use File | Settings | File Templates.
 */
public class GetPhotoFragment extends SherlockFragment {

    /** **/
    private final static int PICTURE_SIDE_SIZE = 600;

    /** **/
    private SurfaceView preview;

    /** **/
    private SurfaceHolder previewHolder;

    /** **/
    private Camera camera;

    /** **/
    private boolean inPreview;

    /** **/
   // private ProgressDialog dialog;

    /** **/
    private FrameLayout parentLayout;

    /** **/
    private Camera.Size cameraPreviewSize, cameraSnapshotSize;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Action bar subtitle
        ActionBarUtil.initActionBar(ActionBar.NAVIGATION_MODE_STANDARD, getActivity());
        ActionBarUtil.setActionBarSubtitle(R.string.getPhotoFragmentLabel, getActivity());

        LayoutUtil.showFragmentContainer(getActivity());

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        parentLayout = (FrameLayout) inflater.inflate(R.layout.get_photo_fragment, container, false);

        preview = (SurfaceView) parentLayout.findViewById(R.id.surface);

        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        FragmentActivity activity =  getActivity();
        WindowManager windowManager = activity.getWindowManager();
        previewHolder.setFixedSize(windowManager.getDefaultDisplay().getWidth(), windowManager.getDefaultDisplay().getWidth());

        return parentLayout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_get_photo, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int pressedMenuItemId = item.getItemId();
        FragmentSwitcher fragmentSwitcher = FragmentSwitcherHolder.getFragmentSwitcher();

        switch (pressedMenuItemId) {
            case R.id.backMenuOption:
                getSherlockActivity().getSupportFragmentManager().popBackStack();
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

            if (camera != null) {
                Camera.Parameters parameters = camera.getParameters();
                if (calculateCameraSizes(width, height, parameters)) {

                    parameters.setPreviewSize(cameraPreviewSize.width, cameraPreviewSize.height);
                    parameters.setPictureSize(cameraSnapshotSize.width, cameraSnapshotSize.height);

                    resizeSurfaceView(width, height);

                    // Add cover layout over preview layout in order to take square preview
                    parentLayout.addView(createCameraCover(width), parentLayout.getWidth(), parentLayout.getHeight());

                    setCameraFocusMode(parameters);

                    camera.setParameters(parameters);
                    // Initially camera is rotated on 90 degrees. Need to align it
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
                        parameters.setRotation(90);
                    } else {
                        camera.setDisplayOrientation(90);
                    }

                    camera.startPreview();
                    inPreview = true;

                } else {
                    // Currently this camera isn't supported
                }
            }
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
    private boolean calculateCameraSizes(int width, int height, Camera.Parameters parameters){

        int resultArea = 0;
        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {

            double currentAspectRatio = getSizeAspectRatio(size);
            // Check size of preview mode
            if (size.width <= width && size.height <= height) {
                // Get max available preview mode by square
                int newArea = size.width * size.height;
                if (newArea > resultArea) {
                    // Looking for camera snapshot size with the same aspect ration
                    Camera.Size snapshotSize = findCameraSnapshotSize(parameters, currentAspectRatio);
                    if (snapshotSize != null) {
                        cameraPreviewSize = size;
                        cameraSnapshotSize = snapshotSize;
                    }
                }
            }
        }

        if (cameraPreviewSize != null && cameraSnapshotSize != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @param parameters
     * @param aspectRatio
     * @return
     */
    private Camera.Size findCameraSnapshotSize(Camera.Parameters parameters, double aspectRatio) {

        int resultArea = Integer.MAX_VALUE;
        Camera.Size smallerCameraSnapshotSize = null;

        for (Camera.Size size : parameters.getSupportedPictureSizes()) {

            double currentAspectRatio = getSizeAspectRatio(size);
            if (size.width >= PICTURE_SIDE_SIZE && size.height >= PICTURE_SIDE_SIZE &&
                    currentAspectRatio <= aspectRatio ) {

                int snapshotArea = size.width * size.height;
                if (snapshotArea < resultArea) {
                    resultArea = snapshotArea;
                    smallerCameraSnapshotSize = size;
                }
            }
        }

        return smallerCameraSnapshotSize;
    }

    /**
     *
     * @return
     */
    private double getSizeAspectRatio(Camera.Size size) {

        if (size.width >= size.height) {
            return ((double) size.width / size.height);
        } else {
            return ((double) size.height / size.width);
        }
    }

    /**
     *
     * @param maxWidth
     * @param maxHeight
     */
    private void resizeSurfaceView(int maxWidth, int maxHeight) {

        double scaleFactor = LayoutUtil.getViewScaleFactor(new Point(maxWidth, maxHeight), new Point(cameraPreviewSize.width,
                cameraPreviewSize.height), 0);
        FrameLayout.LayoutParams previewLayoutParams = new FrameLayout.LayoutParams((int)(cameraPreviewSize.height * scaleFactor),
                (int)(cameraPreviewSize.width * scaleFactor));
        previewLayoutParams.gravity = Gravity.CENTER;

        preview.setLayoutParams(previewLayoutParams);
    }

    /**
     *
     * @param parameters
     */
    private void setCameraFocusMode(Camera.Parameters parameters) {

        PackageManager packageManager = getSherlockActivity().getPackageManager();
        if(packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)) {

            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
        }
    }

    /**
     *
     */
    Camera.PictureCallback photoCallback = new Camera.PictureCallback() {

        public void onPictureTaken(final byte[] data, final Camera camera) {

           // dialog = DialogUtil.createProgressDialog(getActivity());

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

        Bitmap shapshot = BitmapFactory.decodeByteArray(data, 0, data.length);
        shapshot = cropImage(shapshot);

        Bundle bundle = new Bundle();
        bundle.putParcelable("bmp", shapshot);
        MainActivity activity = (MainActivity) getActivity();
        FragmentSwitcherHolder.getFragmentSwitcher().showPreviewPhotoFragment(bundle);

        //dialog.dismiss();
    }

    /**
     *
     * @param originalSnapshot
     * @return
     */
    private Bitmap cropImage(Bitmap originalSnapshot) {

        Bitmap snapshot = originalSnapshot.copy(Bitmap.Config.ARGB_8888, true);

        int snapshotWidth = snapshot.getWidth();
        int snapshotHeight = snapshot.getHeight();

        // 1) Crop snapshot to preview aspect ratio in case when it has some other aspect ratio
        double cameraPreviewAspectRatio = getSizeAspectRatio(cameraPreviewSize);
        if (cameraPreviewAspectRatio != getSizeAspectRatio(cameraSnapshotSize)) {
            if (snapshotWidth >= snapshotHeight) {
                int newSnapshotHeight = (int) (snapshotWidth / cameraPreviewAspectRatio);
                snapshot = Bitmap.createBitmap(snapshot, 0, (snapshotHeight - newSnapshotHeight) / 2, snapshotWidth, newSnapshotHeight);
            } else {
                int newSnapshotWidth = (int) (snapshotHeight / cameraPreviewAspectRatio);
                snapshot  = Bitmap.createBitmap(snapshot, (snapshotWidth - newSnapshotWidth) / 2, 0, newSnapshotWidth, snapshotHeight);
            }
        }

        //2) Scale image: smaller image size must be equals to PICTURE_SIDE_SIZE
        snapshotWidth = snapshot.getWidth();
        snapshotHeight = snapshot.getHeight();
        Bitmap scaledBitmap = null;
        if (snapshotWidth >= snapshotHeight) {
            double scaleFactor = ((double) snapshotWidth) / snapshotHeight;
            //scaledBitmap = Bitmap.createScaledBitmap (snapshot, (int)(PICTURE_SIDE_SIZE * scaleFactor), PICTURE_SIDE_SIZE, false);
            scaledBitmap = scaleBitmap(snapshot, (int) (PICTURE_SIDE_SIZE * scaleFactor), PICTURE_SIDE_SIZE);
        } else {
            double scaleFactor = ((double) snapshotHeight) / snapshotWidth;
            //scaledBitmap = Bitmap.createScaledBitmap(snapshot, PICTURE_SIDE_SIZE, (int)(PICTURE_SIDE_SIZE * scaleFactor), false);
            scaledBitmap = scaleBitmap(snapshot, PICTURE_SIDE_SIZE, (int) (PICTURE_SIDE_SIZE * scaleFactor));
        }

        //3) Crop scaled bitmap: leave middle image size with height equal to width
        Bitmap croppedBitmap = null;
        if (snapshotWidth >= snapshotHeight) {
            int cropMarginWidth = (scaledBitmap.getWidth() - PICTURE_SIDE_SIZE) / 2;
            croppedBitmap = Bitmap.createBitmap(scaledBitmap, cropMarginWidth, 0, PICTURE_SIDE_SIZE, PICTURE_SIDE_SIZE);
        } else {
            int cropMarginHeight = (scaledBitmap.getHeight() - PICTURE_SIDE_SIZE) / 2;
            croppedBitmap = Bitmap.createBitmap(scaledBitmap, 0, cropMarginHeight, PICTURE_SIDE_SIZE, PICTURE_SIDE_SIZE);
        }

        //4) Rotate image on 90 degree
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap result = Bitmap.createBitmap(croppedBitmap, 0, 0, croppedBitmap.getWidth(), croppedBitmap.getHeight(),
                matrix, true);

        return result;
    }

    /**
     *
     * @param source
     * @param newWidth
     * @param newHeight
     * @return
     */
    private Bitmap scaleBitmap(Bitmap source, int newWidth, int newHeight) {

        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float ratioX = newWidth / (float) source.getWidth();
        float ratioY = newHeight / (float) source.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(source, middleX - source.getWidth() / 2, middleY - source.getHeight() / 2,
                new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
    }

    /**
     *
     * @param cameraWidth
     * @return
     */
    public LinearLayout createCameraCover(int cameraWidth) {

        int layoutHeight = parentLayout.getHeight();
        int opaqueLayoutHeight = (layoutHeight - cameraWidth) / 2;

        LinearLayout coverLayout = (LinearLayout) getSherlockActivity().getLayoutInflater().inflate(R.layout.get_photo_fragment_camera_cover, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(parentLayout.getWidth(),
                parentLayout.getHeight());
        coverLayout.setLayoutParams(layoutParams);

        LinearLayout topLinearLayout = (LinearLayout) coverLayout.findViewById(R.id.topLayout);
        layoutParams = new LinearLayout.LayoutParams(parentLayout.getWidth(),
                opaqueLayoutHeight);
        topLinearLayout.setLayoutParams(layoutParams);

        LinearLayout middleLinearLayout = (LinearLayout) coverLayout.findViewById(R.id.middleLayout);
        layoutParams = new LinearLayout.LayoutParams(parentLayout.getWidth(), cameraWidth);
        middleLinearLayout.setLayoutParams(layoutParams);
        middleLinearLayout.setBackgroundColor(Color.TRANSPARENT);

        LinearLayout bottomLinearLayout = (LinearLayout) coverLayout.findViewById(R.id.bottomLayout);
        layoutParams = new LinearLayout.LayoutParams(parentLayout.getWidth(),
                opaqueLayoutHeight);
        bottomLinearLayout.setLayoutParams(layoutParams);

        return coverLayout;
    }
}
