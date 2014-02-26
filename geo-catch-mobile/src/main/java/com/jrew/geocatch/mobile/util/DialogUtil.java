package com.jrew.geocatch.mobile.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager;
import android.widget.TextView;
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
     * @param context
     * @return
     */
    public static ProgressDialog createProgressDialog(Context context, String message) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.show();

        dialog.setContentView(R.layout.progress_dialog);

        TextView textView = (TextView) dialog.findViewById(R.id.message);
        textView.setText(message);



        return dialog;
    }

    /**
     *
     * @param context
     * @return
     */
    public static ProgressDialog createProgressDialog(Context context, int resourceId) {

        String message = context.getString(resourceId);
        return createProgressDialog(context, message);
    }

}
