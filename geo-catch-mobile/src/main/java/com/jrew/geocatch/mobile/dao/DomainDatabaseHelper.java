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
public class DomainDatabaseHelper extends AbstractDatabaseHelper<DomainProperty> {

    /** **/
    protected static final String DATABASE_NAME = "DomainProperty.db";

    /** **/
    protected static final int DATABASE_VERSION = 1;

    /** **/
    private Dao<DomainProperty, Integer> domainPropertyDao;

    /**
     *
     * @param context
     */
    public DomainDatabaseHelper(Context context) {
        super(context, DomainProperty.class, DATABASE_NAME, DATABASE_VERSION);
    }

    /**
     *
     * @return
     */
    public Dao<DomainProperty, Integer> getDao() throws SQLException {

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
