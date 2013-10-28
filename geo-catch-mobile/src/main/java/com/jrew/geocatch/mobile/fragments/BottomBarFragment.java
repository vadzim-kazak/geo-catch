package com.jrew.geocatch.mobile.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.jrew.geocatch.mobile.R;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 28.10.13
 * Time: 17:50
 * To change this template use File | Settings | File Templates.
 */
public class BottomBarFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Create fragment view
        return inflater.inflate(R.layout.bottom_bar_fragment, container, false);
    }
}
