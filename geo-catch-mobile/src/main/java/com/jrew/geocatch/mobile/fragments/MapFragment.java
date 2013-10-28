package com.jrew.geocatch.mobile.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import com.jrew.geocatch.mobile.R;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 28.10.13
 * Time: 18:03
 * To change this template use File | Settings | File Templates.
 *
 */
public class MapFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create fragment view
        View mapFragmentView = inflater.inflate(R.layout.map_fragment, container, false);

        // Set start up page to web view
        WebView webView = (WebView) mapFragmentView.findViewById(R.id.mapView);
        webView.loadUrl(getResources().getString(R.string.geoCatchRepositoryUrl));

        return mapFragmentView;
    }
}
