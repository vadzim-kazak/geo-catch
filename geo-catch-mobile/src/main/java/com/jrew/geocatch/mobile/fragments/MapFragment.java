package com.jrew.geocatch.mobile.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;
import com.jrew.geocatch.mobile.R;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 28.10.13
 * Time: 18:03
 * To change this template use File | Settings | File Templates.
 *
 */
public class MapFragment extends SherlockFragment {

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        // Create fragment view
//        View mapFragmentView = inflater.inflate(R.layout.map_fragment, container, false);
//
//
//        WebView webView = (WebView) mapFragmentView.findViewById(R.id.mapView);
//        // enable javascript
//        webView.getSettings().setJavaScriptEnabled(true);
//
//        final Activity activity = getActivity();
//        webView.setWebViewClient(new WebViewClient() {
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        // Set start up page
//        webView.loadUrl(getResources().getString(R.config.geoCatchRepositoryUrl));
//
//        return mapFragmentView;
//    }
}
