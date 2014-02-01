package com.jrew.geocatch.mobile.reciever;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.jrew.geocatch.mobile.R;
import com.jrew.geocatch.mobile.adapter.DomainAutoCompleteAdapter;
import com.jrew.geocatch.mobile.dao.DatabaseHelper;
import com.jrew.geocatch.mobile.dao.DatabaseManager;
import com.jrew.geocatch.mobile.service.DomainInfoService;
import com.jrew.geocatch.mobile.util.DialogUtil;
import com.jrew.geocatch.mobile.util.SharedPreferencesHelper;
import com.jrew.geocatch.mobile.util.StringUtil;
import com.jrew.geocatch.mobile.view.DomainPropertyView;
import com.jrew.geocatch.web.model.DomainProperty;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 */
public class DomainInfoServiceResultReceiver extends ResultReceiver {

    /** **/
    private Activity activity;

    /** **/
    private ProgressDialog dialog;

    /**
     *
     * @param handler
     */
    public DomainInfoServiceResultReceiver(Handler handler, Activity activity) {
        super(handler);
        this.activity = activity;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {

        switch (resultCode) {

            case DomainInfoService.ResultStatus.LOADING:
                dialog = DialogUtil.createProgressDialog(activity);
                dialog.show();
                break;

            case DomainInfoService.ResultStatus.LOADING_FINISHED:

                List<DomainProperty> domainProperties =
                        (List<DomainProperty>) resultData.getSerializable(DomainInfoService.RESULT_KEY);
                if (domainProperties != null && domainProperties.size() > 0) {
                    synchronizeDomainProperties(domainProperties);
                    SharedPreferencesHelper.setLastSyncDate(activity);
                    dialog.hide();
                } else {
                    dialog.hide();
                    Toast.makeText(activity, activity.getResources().getString(R.string.syncError),
                            Toast.LENGTH_LONG).show();
                }

                break;

            case DomainInfoService.ResultStatus.ERROR:
                dialog.hide();
                Toast.makeText(activity, activity.getResources().getString(R.string.syncError),
                        Toast.LENGTH_LONG).show();
                break;
        }
    }

    /**
     *
     * @param serverDomainProperties
     */
    private void synchronizeDomainProperties(List<DomainProperty> serverDomainProperties) {
        // Get domain properties from current db
        List<DomainProperty> localDomainProperties = DatabaseManager.loadDomainProperties(activity);

        final List<DomainProperty> domainPropertiesToCreate = new ArrayList<DomainProperty>();
        final List<DomainProperty> domainPropertiesToUpdate = new ArrayList<DomainProperty>();

        for (DomainProperty serverDomainProperty : serverDomainProperties) {

            boolean isServerDomainPropertyPresented = false;
            for (DomainProperty localDomainProperty : localDomainProperties) {

                if (serverDomainProperty.getId() == localDomainProperty.getId()) {

                    isServerDomainPropertyPresented = true;
                    if (!areEquals(serverDomainProperty, localDomainProperty)) {
                        domainPropertiesToUpdate.add(serverDomainProperty);
                    }
                }
            }

            if(!isServerDomainPropertyPresented) {
                domainPropertiesToCreate.add(serverDomainProperty);
            }
        }

        DatabaseManager.persistDomainProperties(activity, domainPropertiesToCreate, domainPropertiesToUpdate);
    }

    /**
     *
     * @param first
     * @param second
     * @return
     */
    private boolean areEquals(DomainProperty first, DomainProperty second) {

        if (first != null && second != null &&
            first.getId() == second.getId() &&
            first.getType() == second.getType() &&
            first.getItem() == second.getItem()
            ) {

            String firstLocale = first.getLocale();
            String secondLocale = second.getLocale();
            String firstValue = first.getValue();
            String secondValue = second.getValue();

            if (StringUtil.equals(firstLocale, secondLocale) &&
                StringUtil.equals(firstValue, secondValue)) {

                return true;
            }
        }

        return false;
    }
}
