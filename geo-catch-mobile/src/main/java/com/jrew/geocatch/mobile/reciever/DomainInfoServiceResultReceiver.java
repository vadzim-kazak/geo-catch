package com.jrew.geocatch.mobile.reciever;

import android.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import com.jrew.geocatch.mobile.adapter.DomainAutoCompleteAdapter;
import com.jrew.geocatch.mobile.service.DomainInfoService;
import com.jrew.geocatch.web.model.DomainProperty;

import java.util.List;

/**
 *
 */
public class DomainInfoServiceResultReceiver extends ResultReceiver {

    final AutoCompleteTextView container;

    public DomainInfoServiceResultReceiver(Handler handler, AutoCompleteTextView container) {
        super(handler);
        this.container = container;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        switch (resultCode) {

            case DomainInfoService.ResultStatus.LOADING_FINISHED:

                List<DomainProperty> domainProperties = (List<DomainProperty>) resultData.getSerializable(DomainInfoService.RESULT_KEY);
                DomainAutoCompleteAdapter adapter = new DomainAutoCompleteAdapter(container.getContext(), R.layout.simple_dropdown_item_1line, domainProperties);
                container.setAdapter(adapter);
                container.setThreshold(1);
                break;

            case DomainInfoService.ResultStatus.ERROR:

                break;
        }
    }


}
