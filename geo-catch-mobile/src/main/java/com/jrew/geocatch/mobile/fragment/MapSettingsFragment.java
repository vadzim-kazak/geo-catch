package com.jrew.geocatch.mobile.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.reciever.DomainInfoServiceResultReceiver;
import com.jrew.geocatch.mobile.service.DomainInfoService;
import com.jrew.geocatch.mobile.util.ActionBarHolder;
import com.jrew.geocatch.mobile.util.FragmentSwitcherHolder;
import com.jrew.geocatch.mobile.util.SearchCriteriaHolder;
import com.jrew.geocatch.mobile.view.DomainPropertyView;
import com.jrew.geocatch.mobile.view.RangeSeekBar;
import com.jrew.geocatch.mobile.view.StrictDomainPropertyView;
import com.jrew.geocatch.web.model.DomainProperty;
import com.jrew.geocatch.web.model.criteria.SearchCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 12.11.13
 * Time: 16:42
 * To change this template use File | Settings | File Templates.
 */
public class MapSettingsFragment extends SherlockFragment {

    /** **/
    private StrictDomainPropertyView fishTypeView, fishingToolView, fishingBaitView;

    /** **/
    private RadioGroup ownerRadioGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        // Action bar subtitle
        ActionBar actionBar = ActionBarHolder.getActionBar();
        actionBar.setSubtitle(getResources().getString(R.string.mapSettingsFragmentLabel));

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        View mapSettingsLayout = inflater.inflate(R.layout.map_settings_fragment, container, false);

        //Populate
        fishTypeView = (StrictDomainPropertyView) mapSettingsLayout.findViewById(R.id.fishTypeView);
        fishTypeView.loadDomainProperties(DomainInfoService.DomainInfoType.FISH);

        fishingToolView = (StrictDomainPropertyView) mapSettingsLayout.findViewById(R.id.fishingToolView);
        fishingToolView.loadDomainProperties(DomainInfoService.DomainInfoType.FISHING_TOOL);

        fishingBaitView = (StrictDomainPropertyView) mapSettingsLayout.findViewById(R.id.fishingBaitView);
        fishingBaitView.loadDomainProperties(DomainInfoService.DomainInfoType.BAIT);

        ownerRadioGroup = (RadioGroup) mapSettingsLayout.findViewById(R.id.ownerRadioGroup);

        initMapSettings();

        return mapSettingsLayout;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, com.actionbarsherlock.view.MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_map_settings, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int pressedMenuItemId = item.getItemId();

        FragmentSwitcher fragmentSwitcher = FragmentSwitcherHolder.getFragmentSwitcher();
        switch (pressedMenuItemId) {
            case R.id.proceedToMapMenuOption:
                fragmentSwitcher.showMapFragment();
                break;

            case R.id.resetMapSettingsMenuOption:
                break;
        }

        return true;
    }


    /**
     *
     */
    private void initMapSettings() {

        SearchCriteria searchCriteria = SearchCriteriaHolder.getSearchCriteria();

        // Init domain properties
        List<DomainProperty> domainProperties = searchCriteria.getDomainProperties();
        if (domainProperties != null && !domainProperties.isEmpty()) {
            for (DomainProperty domainProperty : domainProperties) {
                if (domainProperty.getType() == DomainPropertyView.FISH_DOMAIN_PROPERTY_TYPE) {
                    fishTypeView.setSelectedDomainProperty(domainProperty);
                } else if (domainProperty.getType() == DomainPropertyView.FISHING_TOOL_DOMAIN_PROPERTY_TYPE) {
                    fishingToolView.setSelectedDomainProperty(domainProperty);
                } else if (domainProperty.getType() == DomainPropertyView.FISHING_BAIT_DOMAIN_PROPERTY_TYPE) {
                    fishingBaitView.setSelectedDomainProperty(domainProperty);
                }
            }
        }

        // Init owner
        String owner = searchCriteria.getOwner();
        if (owner != null && owner.length() > 0) {
            if (SearchCriteria.OWNER_ANY_VALUE.equalsIgnoreCase(owner)) {
                ownerRadioGroup.findViewById(R.id.anyone).setSelected(true);
            } else if (SearchCriteria.OWNER_SELF_VALUE.equalsIgnoreCase((owner))) {
                ownerRadioGroup.findViewById(R.id.self).setSelected(true);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        saveSearchCriteria();
        //clearSettingViews();
    }

    /**
     *
     * @return
     */
    private SearchCriteria saveSearchCriteria() {

        SearchCriteria searchCriteria = SearchCriteriaHolder.getSearchCriteria();

        // Device Id
        String deviceId = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        searchCriteria.setDeviceId(deviceId);

        // Domain properties
        List<DomainProperty> domainProperties = new ArrayList<DomainProperty>();
        DomainProperty fishType = fishTypeView.getSelectedDomainProperty(false);
        if (fishType != null) {
            domainProperties.add(fishType);
        }

        DomainProperty fishingTool = fishingToolView.getSelectedDomainProperty(false);
        if (fishingTool != null) {
            domainProperties.add(fishingTool);
        }

        DomainProperty fishingBait = fishingBaitView.getSelectedDomainProperty(false);
        if (fishingBait != null) {
            domainProperties.add(fishingBait);
        }
        searchCriteria.setDomainProperties(domainProperties);

        // Owner
        int checkedRadioButtonId = ownerRadioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) ownerRadioGroup.findViewById(checkedRadioButtonId);
        if (radioButton.getId() == R.id.anyone) {
            searchCriteria.setOwner(SearchCriteria.OWNER_ANY_VALUE);
        } else {
            searchCriteria.setOwner(SearchCriteria.OWNER_SELF_VALUE);
        }

        return searchCriteria;
    }
}
