package com.jrew.geocatch.mobile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.reciever.DomainInfoServiceResultReceiver;
import com.jrew.geocatch.mobile.service.DomainInfoService;
import com.jrew.geocatch.mobile.util.MessageBuilder;
import com.jrew.geocatch.mobile.util.SearchCriteriaHolder;
import com.jrew.geocatch.mobile.view.DomainPropertyView;
import com.jrew.geocatch.mobile.view.RangeSeekBar;
import com.jrew.geocatch.web.model.DomainProperty;
import com.jrew.geocatch.web.model.criteria.DayPeriodSearchCriterion;
import com.jrew.geocatch.web.model.criteria.MonthPeriodSearchCriterion;
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
public class MapSettingsFragment extends Fragment {

    /** **/
    private DomainPropertyView fishTypeView, fishingToolView, fishingBaitView;

    /** **/
    private RadioGroup ownerRadioGroup;

    /** **/
    private CheckBox timeFilterCheckbox, monthFilterCheckbox;

    /** **/
    private RangeSeekBar<Integer> timeFilter, monthFilter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View result = inflater.inflate(R.layout.map_settings_fragment, container, false);

        //Populate
        fishTypeView = (DomainPropertyView) result.findViewById(R.id.fishTypeView);
        loadDomainInfo(fishTypeView, DomainInfoService.DomainInfoType.FISH);

        fishingToolView = (DomainPropertyView) result.findViewById(R.id.fishingToolView);
        loadDomainInfo(fishingToolView, DomainInfoService.DomainInfoType.FISHING_TOOL);

        fishingBaitView = (DomainPropertyView) result.findViewById(R.id.fishingBaitView);
        loadDomainInfo(fishingBaitView, DomainInfoService.DomainInfoType.BAIT);

        LinearLayout timeFilterRow = (LinearLayout) result.findViewById(R.id.timeFilterRow);
        timeFilter = new RangeSeekBar<Integer>(0, 24, getActivity());
        timeFilter.setEnabled(false);
        timeFilter.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                if (timeFilterCheckbox.isChecked()) {
                    timeFilterCheckbox.setText(MessageBuilder.getDayFilterMessage(timeFilter.getSelectedMinValue(),
                            timeFilter.getSelectedMaxValue(), getResources()));
                }
            }
        });

        timeFilterCheckbox = (CheckBox) result.findViewById(R.id.timeFilterCheckbox);
        timeFilterCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isTimeFilterEnabled = timeFilter.isEnabled();
                // Inverse time filter state
                isTimeFilterEnabled = !isTimeFilterEnabled;
                timeFilter.setEnabled(isTimeFilterEnabled);
            }
        });
        timeFilterRow.addView(timeFilter);

        LinearLayout monthFilterRow = (LinearLayout) result.findViewById(R.id.monthFilterRow);
        monthFilter = new RangeSeekBar<Integer>(1, 12, getActivity());
        monthFilter.setEnabled(false);
        monthFilter.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                if (monthFilterCheckbox.isChecked()) {
                    monthFilterCheckbox.setText(MessageBuilder.getMonthFilterMessage(monthFilter.getSelectedMinValue(),
                            monthFilter.getSelectedMaxValue(), getResources()));
                }
            }
        });

        monthFilterCheckbox = (CheckBox) result.findViewById(R.id.monthFilterCheckbox);
        monthFilterCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isMonthFilterEnabled = monthFilter.isEnabled();
                // Inverse time filter state
                isMonthFilterEnabled = !isMonthFilterEnabled;
                monthFilter.setEnabled(isMonthFilterEnabled);
            }
        });
        monthFilterRow.addView(monthFilter);

        ownerRadioGroup = (RadioGroup) result.findViewById(R.id.ownerRadioGroup);

        initMapSettings();

        return result;
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

        // Init time filter
        DayPeriodSearchCriterion dayPeriodCriterion = searchCriteria.getDayPeriod();
        if (dayPeriodCriterion != null) {
            // Enable checkbox
            timeFilterCheckbox.setChecked(true);
            timeFilterCheckbox.setText(MessageBuilder.getDayFilterMessage(dayPeriodCriterion.getFromHour(),
                    dayPeriodCriterion.getToHour(), getResources()));
            timeFilter.setEnabled(true);
            timeFilter.setSelectedMinValue(dayPeriodCriterion.getFromHour());
            timeFilter.setSelectedMaxValue(dayPeriodCriterion.getToHour());
        } else {
            timeFilterCheckbox.setText(getResources().getString(R.string.timeFilterLabel));
            timeFilterCheckbox.setChecked(false);
            timeFilter.setEnabled(false);
        }

        // Init month filter
        MonthPeriodSearchCriterion monthPeriodCriterion = searchCriteria.getMonthPeriod();
        if (monthPeriodCriterion != null) {
            monthFilterCheckbox.setChecked(true);
            monthFilterCheckbox.setText(MessageBuilder.getMonthFilterMessage(monthPeriodCriterion.getFromMonth(),
                    monthPeriodCriterion.getToMonth(), getResources()));
            monthFilter.setEnabled(true);
            monthFilter.setSelectedMinValue(monthPeriodCriterion.getFromMonth());
            monthFilter.setSelectedMaxValue(monthPeriodCriterion.getToMonth());
        } else {
            monthFilterCheckbox.setText(getResources().getString(R.string.monthFilterLabel));
            monthFilterCheckbox.setChecked(false);
            monthFilter.setEnabled(false);
        }
    }

    /**
     *
     */
    public void loadDomainInfo(DomainPropertyView domainPropertyView, int domainInfoType) {

        Bundle bundle = new Bundle();

        DomainInfoServiceResultReceiver receiver = new DomainInfoServiceResultReceiver(new Handler(), domainPropertyView);

        String locale = Locale.getDefault().getLanguage();
        bundle.putString(DomainInfoService.LOCALE_KEY, locale);
        bundle.putInt(DomainInfoService.DOMAIN_INFO_TYPE_KEY, domainInfoType);

        final Intent intent = new Intent(Intent.ACTION_SYNC, null, getActivity(), DomainInfoService.class);
        intent.putExtra(DomainInfoService.REQUEST_KEY, bundle);
        intent.putExtra(DomainInfoService.RECEIVER_KEY, receiver);
        getActivity().startService(intent);
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
        DomainProperty fishType = fishTypeView.getSelectedDomainProperty();
        if (fishType != null) {
            domainProperties.add(fishType);
        }

        DomainProperty fishingTool = fishingToolView.getSelectedDomainProperty();
        if (fishingTool != null) {
            domainProperties.add(fishingTool);
        }

        DomainProperty fishingBait = fishingBaitView.getSelectedDomainProperty();
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

        // Time filter
        if (timeFilterCheckbox.isChecked()) {
            DayPeriodSearchCriterion criterion = new DayPeriodSearchCriterion();
            criterion.setFromHour(timeFilter.getSelectedMinValue());
            criterion.setToHour(timeFilter.getSelectedMaxValue());
            searchCriteria.setDayPeriod(criterion);
        } else {
            searchCriteria.setDayPeriod(null);
        }

        // Month filter
        if (monthFilterCheckbox.isChecked()) {
            MonthPeriodSearchCriterion criterion = new MonthPeriodSearchCriterion();
            criterion.setFromMonth(monthFilter.getSelectedMinValue());
            criterion.setToMonth(monthFilter.getSelectedMaxValue());
            searchCriteria.setMonthPeriod(criterion);
        } else {
            searchCriteria.setMonthPeriod(null);
        }

        return searchCriteria;
    }

    @Override
    public void onPause() {
        super.onPause();
        saveSearchCriteria();
    }
}
