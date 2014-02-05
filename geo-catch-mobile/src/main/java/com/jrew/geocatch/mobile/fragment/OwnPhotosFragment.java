package com.jrew.geocatch.mobile.fragment;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.adapter.PhotosGridViewAdapter;
import com.jrew.geocatch.mobile.adapter.PostponedImageAdapter;
import com.jrew.geocatch.mobile.util.ActionBarHolder;
import com.jrew.geocatch.mobile.util.DialogUtil;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 2/4/14
 * Time: 1:25 PM
 */
public class OwnPhotosFragment extends SherlockFragment {

    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Action bar subtitle
        ActionBar actionBar = ActionBarHolder.getActionBar();
        actionBar.setSubtitle(getResources().getString(R.string.ownPhotosFragmentLabel));

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        View layout = inflater.inflate(R.layout.own_photos_fragment, container, false);

        ListView postponedPhotos = (ListView) layout.findViewById(R.id.postponedPhotosListView);
        postponedPhotos.setAdapter(new PostponedImageAdapter(getActivity()));

        GridView photosGridView = (GridView) layout.findViewById(R.id.photosGridView);
        photosGridView.setAdapter(new PhotosGridViewAdapter(getActivity()));

        progressDialog = DialogUtil.createProgressDialog(getActivity());

        return layout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_own_photos, menu);
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
                fragmentSwitcher.showImageTakeCameraFragment();
                break;
        }

        return true;
    }
}
