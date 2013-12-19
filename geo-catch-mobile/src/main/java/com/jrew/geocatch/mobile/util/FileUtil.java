package com.jrew.geocatch.mobile.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Files manipulation util class
 */
public class FileUtil {

    /** **/
    public static String BITMAP_NAME = "geoCatchUpload.jpg";

    /**
     *
     * @param bitmap
     * @param folderToSave
     * @param resources
     * @return
     */
    public static String writeBitmapToFileSystem(Bitmap bitmap, File folderToSave, Resources resources) {
        File bitmapFile = new File(folderToSave, BITMAP_NAME);
        if (bitmapFile.exists()) {
            bitmapFile.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(bitmapFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            out.flush();
            out.close();
            return bitmapFile.getAbsolutePath();
        } catch (Exception exception) {
            Log.e(CommonUtils.getDebugTag(resources), exception.getMessage());
        }

        return null;
    }

}
