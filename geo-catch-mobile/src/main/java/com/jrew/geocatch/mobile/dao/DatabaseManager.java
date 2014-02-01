package com.jrew.geocatch.mobile.dao;

import android.content.Context;
import android.util.Log;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.jrew.geocatch.web.model.DomainProperty;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 2/1/14
 * Time: 3:03 PM
 */
public class DatabaseManager {

    /** **/
    private final static String LOG_NAME = DatabaseManager.class.getName();

    /**
     *
     * @param domainPropertyType
     * @return
     */
    public static List<DomainProperty> loadDomainProperties(long domainPropertyType, Context context) {

        DatabaseHelper helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);

        String locale = Locale.getDefault().getLanguage();
        List<DomainProperty> domainProperties = null;

        try {
            Map<String, Object> fieldValues = new HashMap<String, Object>();
            fieldValues.put("locale", locale);
            fieldValues.put("type", domainPropertyType);
            domainProperties = helper.getDomainPropertyDao().queryForFieldValues(fieldValues);

        } catch (SQLException exception) {
            Log.e(LOG_NAME, "Couldn't load domain properties.", exception);
        }

        OpenHelperManager.releaseHelper();
        return domainProperties;
    }

    /**
     *
     * @param context
     * @return
     */
    public static List<DomainProperty> loadDomainProperties(Context context) {

        DatabaseHelper helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);

        List<DomainProperty> localDomainProperties = null;

        try {
            localDomainProperties = helper.getDomainPropertyDao().queryForAll();
        }  catch (SQLException exception) {
           Log.e(LOG_NAME, "Couldn't load domain properties.", exception);
        }

        OpenHelperManager.releaseHelper();

        return localDomainProperties;
    }

    public static void persistDomainProperties(Context context, final List<DomainProperty> domainPropertiesToCreate,
                                            final List<DomainProperty> domainPropertiesToUpdate) {

        final DatabaseHelper helper = OpenHelperManager.getHelper(context, DatabaseHelper.class);

        try {

            // Persist domain properties
            helper.getDomainPropertyDao().callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {

                    for (DomainProperty domainPropertyToCreate : domainPropertiesToCreate) {
                        helper.getDomainPropertyDao().create(domainPropertyToCreate);
                    }

                    for (DomainProperty domainPropertyToUpdate : domainPropertiesToUpdate) {
                        helper.getDomainPropertyDao().update(domainPropertyToUpdate);
                    }

                    return null;
                }
            });

        } catch (Exception exception) {
            Log.e(LOG_NAME, "Couldn't persist domain properties.", exception);
        }

        OpenHelperManager.releaseHelper();
    }

}
