package com.jrew.geocatch.mobile.dao;

import android.content.Context;
import android.util.Log;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.PreparedQuery;
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
public class DomainDatabaseManager {

    /** **/
    private final static String LOG_NAME = DomainDatabaseManager.class.getName();

    /**
     *
     * @param domainPropertyType
     * @return
     */
    public static List<DomainProperty> loadDomainProperties(long domainPropertyType, Context context) {

        DomainDatabaseHelper helper = OpenHelperManager.getHelper(context, DomainDatabaseHelper.class);

        String locale = Locale.getDefault().getLanguage();
        List<DomainProperty> domainProperties = null;

        try {
            Map<String, Object> fieldValues = new HashMap<String, Object>();
            fieldValues.put("locale", locale);
            fieldValues.put("type", domainPropertyType);
            domainProperties = helper.getDao().queryForFieldValues(fieldValues);

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

        DomainDatabaseHelper helper = OpenHelperManager.getHelper(context, DomainDatabaseHelper.class);

        List<DomainProperty> localDomainProperties = null;

        try {
            localDomainProperties = helper.getDao().queryForAll();
        }  catch (SQLException exception) {
           Log.e(LOG_NAME, "Couldn't load domain properties.", exception);
        }

        OpenHelperManager.releaseHelper();

        return localDomainProperties;
    }

    /**
     *
     * @param context
     * @param domainPropertiesToCreate
     * @param domainPropertiesToUpdate
     */
    public static void persistDomainProperties(Context context, final List<DomainProperty> domainPropertiesToCreate,
                                            final List<DomainProperty> domainPropertiesToUpdate) {

        final DomainDatabaseHelper helper = OpenHelperManager.getHelper(context, DomainDatabaseHelper.class);

        try {

            // Persist domain properties
            helper.getDao().callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {

                    for (DomainProperty domainPropertyToCreate : domainPropertiesToCreate) {
                        helper.getDao().create(domainPropertyToCreate);
                    }

                    for (DomainProperty domainPropertyToUpdate : domainPropertiesToUpdate) {
                        helper.getDao().update(domainPropertyToUpdate);
                    }

                    return null;
                }
            });

        } catch (Exception exception) {
            Log.e(LOG_NAME, "Couldn't persist domain properties.", exception);
        }

        OpenHelperManager.releaseHelper();
    }

    /**
     *
     * @param domainProperty
     * @param context
     * @return
     */
    public static DomainProperty loadLocalizedDomainProperty(DomainProperty domainProperty, Context context) {

        DomainDatabaseHelper helper = OpenHelperManager.getHelper(context, DomainDatabaseHelper.class);

        String locale = Locale.getDefault().getLanguage();
        List<DomainProperty> domainProperties = null;

        try {
            Map<String, Object> fieldValues = new HashMap<String, Object>();
            fieldValues.put("locale", locale);
            fieldValues.put("item", domainProperty.getItem());
            domainProperties = helper.getDao().queryForFieldValues(fieldValues);
        } catch (SQLException exception) {
            Log.e(LOG_NAME, "Couldn't find localized domain property.", exception);
        }

        OpenHelperManager.releaseHelper();

        if (domainProperties != null && !domainProperties.isEmpty()) {
            return domainProperties.get(0);
        }

        // return original domain property if couldn't find localized one
        return domainProperty;
    }

}
