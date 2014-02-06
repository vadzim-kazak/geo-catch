package com.jrew.geocatch.mobile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.actionbarsherlock.app.ActionBar;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.adapter.PostponedImageAdapter;
import com.jrew.geocatch.mobile.util.ActionBarHolder;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 06.02.14
 * Time: 16:16
 * To change this template use File | Settings | File Templates.
 */
public class PostponedPhotosFragment extends UserPhotosFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        init();

        // Action bar subtitle
        ActionBar actionBar = ActionBarHolder.getActionBar();
        actionBar.setSubtitle(getResources().getString(R.string.postponedPhotosFragmentLabel));

        View layout = inflater.inflate(R.layout.postponed_photos_fragment, container, false);

        ListView postponedPhotos = (ListView) layout.findViewById(R.id.postponedPhotosListView);
        postponedPhotos.setAdapter(new PostponedImageAdapter(getActivity()));

        return layout;
    }

    @Override
    protected TabTag getTabTag() {
        return TabTag.POSTPONED_PHOTOS_TAB;
    }
}
