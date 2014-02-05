package com.jrew.geocatch.mobile.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager;
import com.jrew.geocatch.mobile.R;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 29.01.14
 * Time: 16:49
 * To change this template use File | Settings | File Templates.
 */
public class DialogUtil {

    /**
     *
     * @param mContext
     * @return
     */
    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
            dialog.hide();
        } catch (WindowManager.BadTokenException e) {

        }

        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_dialog);
        return dialog;
    }

}
