package com.jrew.geocatch.mobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 26.12.13
 * Time: 16:17
 * To change this template use File | Settings | File Templates.
 */
public class StrictDomainPropertyView extends DomainPropertyView implements View.OnFocusChangeListener {

    /**
     *
     * @param context
     */
    public StrictDomainPropertyView(Context context) {
        super(context);
        setOnFocusChangeListener(this);
    }

    /**
     *
     * @param context
     * @param attrs
     */
    public StrictDomainPropertyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (getText().length() > 0 && getSelectedDomainProperty(false) == null) {
            Toast.makeText(getContext(), "Populated value isn't presented", Toast.LENGTH_SHORT).show();
            setText("");
        }
    }
}
