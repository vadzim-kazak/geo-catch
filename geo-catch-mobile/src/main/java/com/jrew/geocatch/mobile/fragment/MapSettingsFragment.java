package com.jrew.geocatch.mobile.fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.dao.DomainDatabaseManager;
import com.jrew.geocatch.mobile.service.DomainInfoService;
import com.jrew.geocatch.mobile.util.*;
import com.jrew.geocatch.mobile.view.DomainPropertyView;
import com.jrew.geocatch.mobile.view.StrictDomainPropertyView;
import com.jrew.geocatch.web.model.DomainProperty;
import com.jrew.geocatch.web.model.criteria.SearchCriteria;

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

    /**
     *
     */
    private interface OwnerValuePositions {

        /** **/
        public static final int OWNER_ANY_VALUE_POSITION = 0;

        /** **/
        public static final int OWNER_SELF_VALUE_POSITION = 1;
    }

    /** **/
    private StrictDomainPropertyView fishTypeView, fishingToolView, fishingBaitView;

    /** **/
    private Spinner ownerSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        ActionBarUtil.initActionBar(ActionBar.NAVIGATION_MODE_STANDARD, getActivity());
        ActionBarUtil.setActionBarSubtitle(R.string.mapSettingsFragmentLabel, getActivity());

        LayoutUtil.showFragmentContainer(getActivity());
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        View mapSettingsLayout = inflater.inflate(R.layout.map_settings_fragment, container, false);

        //Populate
        fishTypeView = (StrictDomainPropertyView) mapSettingsLayout.findViewById(R.id.fishTypeView);
        fishTypeView.loadDomainProperties(DomainInfoService.DomainInfoType.FISH);

        fishingToolView = (StrictDomainPropertyView) mapSettingsLayout.findViewById(R.id.fishingToolView);
        fishingToolView.loadDomainProperties(DomainInfoService.DomainInfoType.FISHING_TOOL);

        fishingBaitView = (StrictDomainPropertyView) mapSettingsLayout.findViewById(R.id.fishingBaitView);
        fishingBaitView.loadDomainProperties(DomainInfoService.DomainInfoType.BAIT);

        ownerSpinner = (Spinner) mapSettingsLayout.findViewById(R.id.ownerSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spinner_item, getResources().getStringArray(R.array.photoOwnerArray));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ownerSpinner.setAdapter(adapter);

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
                fragmentSwitcher.showMapFragment(true);
                break;

            case R.id.resetMapSettingsMenuOption:
                fishTypeView.reset();
                fishingToolView.reset();
                fishingBaitView.reset();
                ownerSpinner.setSelection(0);
                resetSearchCriteria();
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

            String locale = Locale.getDefault().getLanguage();
            for (DomainProperty domainProperty : domainProperties) {

                if (!locale.equalsIgnoreCase(domainProperty.getLocale())) {
                    domainProperty =
                            DomainDatabaseManager.loadLocalizedDomainProperty(domainProperty, getActivity());
                }

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
                ownerSpinner.setSelection(OwnerValuePositions.OWNER_ANY_VALUE_POSITION);
            } else if (SearchCriteria.OWNER_SELF_VALUE.equalsIgnoreCase((owner))) {
                ownerSpinner.setSelection(OwnerValuePositions.OWNER_SELF_VALUE_POSITION);
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
        searchCriteria.setDeviceId(CommonUtil.getDeviceId(getActivity()));

        // Domain properties
        List<DomainProperty> domainProperties = searchCriteria.getDomainProperties();
        domainProperties.clear();

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

        if (ownerSpinner.getSelectedItemPosition() == OwnerValuePositions.OWNER_ANY_VALUE_POSITION) {
            searchCriteria.setOwner(SearchCriteria.OWNER_ANY_VALUE);
        } else {
            searchCriteria.setOwner(SearchCriteria.OWNER_SELF_VALUE);
        }

        return searchCriteria;
    }

    /**
     *
     */
    private void resetSearchCriteria() {
        SearchCriteria searchCriteria = SearchCriteriaHolder.getSearchCriteria();
        searchCriteria.getDomainProperties().clear();
        searchCriteria.setOwner(SearchCriteria.OWNER_ANY_VALUE);
    }
}
