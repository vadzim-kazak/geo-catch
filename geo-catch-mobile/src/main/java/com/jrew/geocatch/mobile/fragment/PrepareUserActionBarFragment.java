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
import com.jrew.geocatch.mobile.util.ActionBarUtil;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;
import com.jrew.geocatch.mobile.util.LayoutUtil;
import com.jrew.geocatch.mobile.util.WebUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 12.02.14
 * Time: 15:07
 * To change this template use File | Settings | File Templates.
 */
public class PrepareUserActionBarFragment extends SherlockFragment {

    /** **/
    public static final String SELECTED_TAB_KEY = "selectedTabKey";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(SELECTED_TAB_KEY)) {
            ActionBarUtil.initTabActionBar(getActivity(), ActionBarUtil.TabTag.valueOf(bundle.getString(SELECTED_TAB_KEY)),
                    FragmentSwitcherHolder.getFragmentSwitcher());
        }

        return inflater.inflate(R.layout.empty_layout, container, false);
    }
}
