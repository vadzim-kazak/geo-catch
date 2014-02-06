package com.jrew.geocatch.mobile.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.actionbarsherlock.app.ActionBar;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.adapter.UploadedPhotosGridViewAdapter;
import com.jrew.geocatch.mobile.util.ActionBarHolder;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 06.02.14
 * Time: 16:15
 * To change this template use File | Settings | File Templates.
 */
public class UploadedPhotosFragment extends UserPhotosFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        init();

        // Action bar subtitle
        ActionBar actionBar = ActionBarHolder.getActionBar();
        actionBar.setSubtitle(getResources().getString(R.string.uploadedPhotosFragmentLabel));

        View layout = inflater.inflate(R.layout.uploaded_photos_fragment, container, false);

        GridView photosGridView = (GridView) layout.findViewById(R.id.photosGridView);
        photosGridView.setAdapter(new UploadedPhotosGridViewAdapter(getActivity()));

        return layout;
    }

}
