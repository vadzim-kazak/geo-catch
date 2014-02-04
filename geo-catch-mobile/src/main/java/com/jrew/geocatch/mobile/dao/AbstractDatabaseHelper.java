package com.jrew.geocatch.mobile.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 04.02.14
 * Time: 11:25
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractDatabaseHelper<T> extends OrmLiteSqliteOpenHelper {

    /** **/
    protected String LOG_NAME = getClass().getName();

    /** **/
    private Class<T> clazz;

    /** **/
    private Dao<T, Integer> dao;

    /**
     *
     * @param context
     */
    public AbstractDatabaseHelper(Context context, Class<T> clazz, String dbName, int dbVersion) {
        super(context, dbName, null, dbVersion);
        this.clazz = clazz;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, clazz);
        } catch (SQLException e) {
            Log.e(LOG_NAME, "Could not create new table for DomainProperty class.", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, clazz, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            Log.e(LOG_NAME, "Could not upgrade the table for Thing", e);
        }
    }

    abstract public Dao<T, Integer> getDao() throws SQLException;
}
