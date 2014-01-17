package com.jrew.geocatch.mobile.fragment;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.activity.MainActivity;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;

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
public class ImageTakePreviewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

        View result = inflater.inflate(R.layout.image_take_preview_fragment, container, false);

        ImageView imageView = (ImageView) result.findViewById(R.id.imagePreview);

        final Bundle fragmentData = getArguments();
        if (fragmentData != null && !fragmentData.isEmpty()) {
            Bitmap image = (Bitmap) fragmentData.getParcelable("bmp");

            imageView.setImageBitmap(image);
        }

        Button proceedButton = (Button) result.findViewById(R.id.imagePreviewProceedButton);
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentSwitcherHolder.getFragmentSwitcher().showImageTakeInfoFragment(new Bundle(fragmentData));
            }
        });

        return result;
    }

    private void rotateImage() {

//        try {
//            File f = new File(imagePath);
//            ExifInterface exif = new ExifInterface(f.getPath());
//            int orientation = exif.getAttributeInt(
//                    ExifInterface.TAG_ORIENTATION,
//                    ExifInterface.ORIENTATION_NORMAL);
//
//            int angle = 0;
//
//            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
//                angle = 90;
//            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
//                angle = 180;
//            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
//                angle = 270;
//            }
//
//            Matrix mat = new Matrix();
//            mat.postRotate(angle);
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 2;
//
//            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f),
//                    null, options);
//            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
//                    bmp.getHeight(), mat, true);
//            ByteArrayOutputStream outstudentstreamOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100,
//                    outstudentstreamOutputStream);
//            imageView.setImageBitmap(bitmap);
//
//        } catch (IOException e) {
//            Log.w("TAG", "-- Error in setting image");
//        } catch (OutOfMemoryError oom) {
//            Log.w("TAG", "-- OOM Error in setting image");
//        }
    }
}
