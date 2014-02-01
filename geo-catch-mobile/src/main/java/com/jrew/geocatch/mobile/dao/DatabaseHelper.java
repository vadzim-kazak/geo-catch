package com.jrew.geocatch.mobile.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseFieldConfig;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.j256.ormlite.table.TableUtils;
import com.jrew.geocatch.web.model.DomainProperty;

import java.sql.SQLException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 2/1/14
 * Time: 11:01 AM
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    /** **/
    private static final String DATABASE_NAME = "DomainProperty.db";

    /** **/
    private static final int DATABASE_VERSION = 1;

    /** **/
    private final String LOG_NAME = getClass().getName();

    /** **/
    private Dao<DomainProperty, Integer> domainPropertyDao;

    /**
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, DomainProperty.class);
        } catch (SQLException e) {
            Log.e(LOG_NAME, "Could not create new table for DomainProperty class.", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, DomainProperty.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            Log.e(LOG_NAME, "Could not upgrade the table for Thing", e);
        }
    }

    /**
     *
     * @return
     */
    public Dao<DomainProperty, Integer> getDomainPropertyDao() throws SQLException {

        if (domainPropertyDao == null) {

            List<DatabaseFieldConfig> fieldConfigs =
                    new ArrayList<DatabaseFieldConfig>();

            DatabaseFieldConfig id = new DatabaseFieldConfig("id");
            id.setId(true);
            id.setDataType(DataType.LONG);
            fieldConfigs.add(id);

            DatabaseFieldConfig type = new DatabaseFieldConfig("type");
            type.setDataType(DataType.LONG);
            fieldConfigs.add(type);

            DatabaseFieldConfig item = new DatabaseFieldConfig("item");
            item.setDataType(DataType.LONG);
            fieldConfigs.add(item);

            DatabaseFieldConfig locale = new DatabaseFieldConfig("locale");
            locale.setDataType(DataType.STRING);
            fieldConfigs.add(locale);

            DatabaseFieldConfig value = new DatabaseFieldConfig("value");
            value.setDataType(DataType.STRING);
            fieldConfigs.add(value);

            DatabaseTableConfig<DomainProperty> domainPropertyTableConfig
                    = new DatabaseTableConfig<DomainProperty>(DomainProperty.class, fieldConfigs);

            domainPropertyDao = DaoManager.createDao(connectionSource, domainPropertyTableConfig);
        }

        return domainPropertyDao;
    }

}
