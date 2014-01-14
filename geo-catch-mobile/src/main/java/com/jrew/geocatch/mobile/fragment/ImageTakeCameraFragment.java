package com.jrew.geocatch.mobile.fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.activity.MainActivity;

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
public class ImageTakeCameraFragment extends Fragment {

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

        View button = (Button) result.findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.takePicture(null, null, photoCallback);
                inPreview = false;
            }
        });

        return result;
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
     * @param width
     * @param height
     * @param parameters
     * @return
     */
    private Camera.Size getBestPreviewSize(int width, int height, Camera.Parameters parameters){

        Camera.Size result = null;
        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
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

        return(result);
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
            Camera.Size size = getBestPreviewSize(width, height, parameters);

            parameters.setPictureSize(640, 480);
            parameters.setPreviewSize(640, 480);

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

        MainActivity activity = (MainActivity) getActivity();
        activity.getFragmentSwitcher().showImageTakePreviewFragment(bundle);

        dialog.dismiss();
    }

    /**
     *
     */
    class SavePhotoTask extends AsyncTask<byte[], String, String> {

        @Override
        protected String doInBackground(byte[]... jpeg) {
            File photo=new File(Environment.getExternalStorageDirectory(), "photo.jpg");
            if (photo.exists()){
                photo.delete();
            }
            try {
                FileOutputStream fos=new FileOutputStream(photo.getPath());
                fos.write(jpeg[0]);
                fos.close();
            }
            catch (java.io.IOException e) {
                Log.e("PictureDemo", "Exception in photoCallback", e);
            }
            return(null);
        }
    }

    /**
     *
     * @param bmp
     */
    public void savePhoto(Bitmap bmp) {
        imageFileFolder = new File(Environment.getExternalStorageDirectory(),"Rotate");
        imageFileFolder.mkdir();
        FileOutputStream out = null;
        Calendar c = Calendar.getInstance();
        String date = fromInt(c.get(Calendar.MONTH))
                + fromInt(c.get(Calendar.DAY_OF_MONTH))
                + fromInt(c.get(Calendar.YEAR))
                + fromInt(c.get(Calendar.HOUR_OF_DAY))
                + fromInt(c.get(Calendar.MINUTE))
                + fromInt(c.get(Calendar.SECOND));

        imageFileName = new File(imageFileFolder, date.toString() + ".jpg");
        try {
            out = new FileOutputStream(imageFileName);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            scanPhoto(imageFileName.toString());
            out = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String fromInt(int val) {
        return String.valueOf(val);
    }

    /**
     *
     * @param imageFileName
     */
    public void scanPhoto(final String imageFileName)
    {
        msConn = new MediaScannerConnection(getActivity(), new MediaScannerConnection.MediaScannerConnectionClient()
        {
            public void onMediaScannerConnected()
            {
                msConn.scanFile(imageFileName, null);
                Log.i("msClient obj  in Photo Utility","connection established");
            }
            public void onScanCompleted(String path, Uri uri)
            {
                msConn.disconnect();
                Log.i("msClient obj in Photo Utility","scan completed");
            }
        });
        msConn.connect();
    }

    //@Override
    public boolean onKeyDown(int keyCode, KeyEvent event){

        if (keyCode == KeyEvent.KEYCODE_MENU&& event.getRepeatCount() == 0) {
            onBack();
        }

        //return super.onKeyDown(keyCode, event);
        return false;
    }

    /**
     *
     */
    public void onBack(){
        Log.e("onBack :","yes");
    }



}
