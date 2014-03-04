package com.jrew.geocatch.mobile.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import com.jrew.geocatch.mobile.R;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 2/25/14
 * Time: 3:02 PM
 */
public class BitmapUtil {

    /** **/
    private static Paint iconPaint;

    /** **/
    private static Paint thumbnailPaint;

//    /** **/
//    private static Paint debugPaint;

    static {
        iconPaint = new Paint();
        iconPaint.setColor(Color.WHITE);
        iconPaint.setStyle(Paint.Style.FILL);

        thumbnailPaint = new Paint(Paint.FILTER_BITMAP_FLAG);

//        debugPaint = new Paint();
//        debugPaint.setColor(Color.RED);
//        debugPaint.setStyle(Paint.Style.FILL);
    }

    /**
     *
     * @param thumbnail
     * @return
     */
    public static Bitmap createIconWithBorder(Bitmap thumbnail, Activity activity) {

        double scaleFactor = Double.parseDouble(activity.getResources().getString(R.config.thumbnailSizeScaleFactor));
        double borderSizeFactor = Double.parseDouble(activity.getResources().getString(R.config.thumbnailBorderSizeScaleFactor));

        // get larger display size
        int largerSide = CommonUtil.getDisplayLargerSideSize(activity);

        int thumbnailSize = (int) (largerSide * scaleFactor);
        int borderSize = (int)(thumbnailSize * borderSizeFactor);

        int iconSize = thumbnailSize + borderSize * 2;

        Bitmap icon = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(icon);
        canvas.drawRect(0, 0, iconSize, iconSize, iconPaint);

        Bitmap scaledThumbnail = Bitmap.createScaledBitmap(thumbnail, thumbnailSize, thumbnailSize, false);

        // Draw scaled thumbnail on bitmap
        canvas.drawBitmap(scaledThumbnail, borderSize, borderSize, thumbnailPaint);

       // canvas.drawCircle(borderSize + scaledThumbnail.getWidth() / 2, 2 * borderSize + scaledThumbnail.getWidth(), 5, debugPaint);

        return icon;
    }

}
