package com.jrew.geocatch.mobile.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.jrew.geocatch.mobile.R;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 26.12.13
 * Time: 16:17
 * To change this template use File | Settings | File Templates.
 */
public class StrictDomainPropertyView extends DomainPropertyView {

    /** **/
    private static int DISABLED_VIEW_COLOR = Color.argb(255, 200, 200, 200);

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

            Toast warning = Toast.makeText(getContext(), getResources().getString(R.string.domainPropertyNotPresented), Toast.LENGTH_SHORT);
            warning.setGravity(Gravity.CENTER, 0 ,0);
            warning.show();

            setTextColor(INITIAL_TEXT_COLOR);
            setText(initialValue);
            isTextFilled = false;
        }
    }

    @Override
    public void loadDomainProperties(long domainPropertyType) {
        super.loadDomainProperties(domainPropertyType);
        if (adapter.getCount() == 0) {
            setEnabled(false);
            LinearLayout parent = (LinearLayout) getParent();
            parent.setBackgroundColor(DISABLED_VIEW_COLOR);
        }
    }
}
