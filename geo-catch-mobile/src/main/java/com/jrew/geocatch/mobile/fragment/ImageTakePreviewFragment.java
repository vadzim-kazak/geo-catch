package com.jrew.geocatch.mobile.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.activity.MainActivity;

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
                MainActivity activity = (MainActivity) getActivity();
                activity.getFragmentSwitcher().showImageTakeInfoFragment(new Bundle(fragmentData));
            }
        });

        return result;
    }
}
