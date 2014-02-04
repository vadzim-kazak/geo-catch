package com.jrew.geocatch.mobile.dao;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseFieldConfig;
import com.j256.ormlite.table.DatabaseTableConfig;
import com.jrew.geocatch.mobile.model.PostponedImage;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 04.02.14
 * Time: 16:45
 * To change this template use File | Settings | File Templates.
 */
public class PostponedImageDatabaseHelper extends AbstractDatabaseHelper<PostponedImage> {

    /** **/
    protected static final String DATABASE_NAME = "PostponedImage.db";

    /** **/
    protected static final int DATABASE_VERSION = 1;

    /** **/
    private Dao<PostponedImage, Integer> postponedImageDao;

    /**
     *
     * @param context
     */
    public PostponedImageDatabaseHelper(Context context) {
        super(context, PostponedImage.class, DATABASE_NAME, DATABASE_VERSION);
    }

    @Override
    public Dao<PostponedImage, Integer> getDao() throws SQLException {

        if (postponedImageDao == null) {

            List<DatabaseFieldConfig> fieldConfigs =
                    new ArrayList<DatabaseFieldConfig>();

            DatabaseFieldConfig id = new DatabaseFieldConfig("id");
            id.setGeneratedId(true);
            id.setDataType(DataType.LONG);
            fieldConfigs.add(id);

            DatabaseFieldConfig imageData = new DatabaseFieldConfig("imageData");
            imageData.setDataType(DataType.BYTE_ARRAY);
            fieldConfigs.add(imageData);

            DatabaseFieldConfig bitmapData = new DatabaseFieldConfig("bitmapData");
            bitmapData.setDataType(DataType.BYTE_ARRAY);
            fieldConfigs.add(bitmapData);

            DatabaseTableConfig<PostponedImage> posponedImageTableConfig
                    = new DatabaseTableConfig<PostponedImage>(PostponedImage.class, fieldConfigs);

            postponedImageDao = DaoManager.createDao(connectionSource, posponedImageTableConfig);
        }

        return postponedImageDao;
    }
}
