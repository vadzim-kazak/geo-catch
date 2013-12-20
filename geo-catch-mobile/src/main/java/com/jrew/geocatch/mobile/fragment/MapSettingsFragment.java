package com.jrew.geocatch.mobile.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.reciever.DomainInfoServiceResultReceiver;
import com.jrew.geocatch.mobile.service.DomainInfoService;
import com.jrew.geocatch.mobile.view.DomainPropertyView;
import com.jrew.geocatch.mobile.view.RangeSeekBar;
import com.jrew.geocatch.web.model.criteria.SearchCriteria;

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
        RangeSeekBar<Integer> timeFilter = new RangeSeekBar<Integer>(0, 24, getActivity());
        timeFilter.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                // handle changed range values
            }
        });
        timeFilterRow.addView(timeFilter);

        LinearLayout monthFilterRow = (LinearLayout) result.findViewById(R.id.monthFilterRow);
        RangeSeekBar<Integer> monthFilter = new RangeSeekBar<Integer>(1, 12, getActivity());
        timeFilter.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                // handle changed range values
            }
        });
        timeFilterRow.addView(timeFilter);

        return result;
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
    private SearchCriteria getSearchCriteria() {
        SearchCriteria criteria = new SearchCriteria();

        // Device Id
        String deviceId = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        criteria.setDeviceId(deviceId);



        return criteria;
    }

    @Override
    public void onPause() {
        // Save SharedPreferences here
        SharedPreferences prefs = getActivity().getSharedPreferences("com.example.app", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        super.onPause();
    }
}
