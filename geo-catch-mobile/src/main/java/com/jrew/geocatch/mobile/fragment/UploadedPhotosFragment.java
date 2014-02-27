package com.jrew.geocatch.mobile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.adapter.UploadedPhotosAdapter;
import com.jrew.geocatch.mobile.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 06.02.14
 * Time: 16:15
 * To change this template use File | Settings | File Templates.
 */
public class UploadedPhotosFragment extends SherlockFragment {

    /** **/
    private UploadedPhotosAdapter uploadedPhotosAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Action bar subtitle
        ActionBarUtil.setActionBarSubtitle(R.string.uploadedPhotosFragmentLabel, getActivity());

        View layout = null;
        if (WebUtil.isNetworkAvailable(getActivity())) {

            LayoutUtil.showFragmentContainer(getActivity());

            layout = inflater.inflate(R.layout.uploaded_photos_fragment, container, false);
            GridView photosGridView = (GridView) layout.findViewById(R.id.photosGridView);
            if (uploadedPhotosAdapter == null) {
                uploadedPhotosAdapter = new UploadedPhotosAdapter(getActivity());
            }
            photosGridView.setAdapter(uploadedPhotosAdapter);

        } else {

            LayoutUtil.showRefreshLayout(getActivity(), R.string.noNetworkConnectionError);
        }

        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_user_photos, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int pressedMenuItemId = item.getItemId();

        FragmentSwitcher fragmentSwitcher = FragmentSwitcherHolder.getFragmentSwitcher();
        switch (pressedMenuItemId) {
            case R.id.proceedToMapMenuOption:
                fragmentSwitcher.showMapFragment();
                break;

            case R.id.takeImageMenuOption:
                fragmentSwitcher.showGetPhotoFragment();
                break;
        }

        return true;
    }
}
