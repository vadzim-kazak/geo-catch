package com.jrew.geocatch.mobile.view;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 26.12.13
 * Time: 16:17
 * To change this template use File | Settings | File Templates.
 */
public class StrictDomainPropertyView extends DomainPropertyView {

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
    public void onFocusChange(View view, boolean isFocused) {
        super.onFocusChange(view, isFocused);

        Editable text = getText();
        if (text != null && getText().length() > 0 &&
            !text.toString().equalsIgnoreCase(initialValue) &&
            getSelectedDomainProperty(false) == null) {

            Toast warning = Toast.makeText(getContext(), "Populated value isn't presented", Toast.LENGTH_SHORT);
            warning.setGravity(Gravity.CENTER, 0 ,0);
            warning.show();

            setTextColor(INITIAL_TEXT_COLOR);
            setText(initialValue);
            isTextFilled = false;
        }
    }
}
