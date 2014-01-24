package com.jrew.geocatch.mobile.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 24.01.14
 * Time: 15:59
 * To change this template use File | Settings | File Templates.
 */
public class PrePopulatedEditText extends EditText implements TextWatcher, View.OnFocusChangeListener {

    /** **/
    private static int INITIAL_TEXT_COLOR = Color.GRAY;

    /** **/
    private static int NORMAL_TEXT_COLOR = Color.BLACK;

    /** **/
    private boolean isTextFilled;

    /** **/
    private String initialValue;

    /**
     *
     * @param context
     */
    public PrePopulatedEditText(Context context) {
        super(context);
        setTextColor(INITIAL_TEXT_COLOR);
        addTextChangedListener(this);
        setOnFocusChangeListener(this);
        initialValue = getText().toString();
    }

    /**
     *
     * @param context
     * @param attrs
     */
    public PrePopulatedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTextColor(INITIAL_TEXT_COLOR);
        addTextChangedListener(this);
        setOnFocusChangeListener(this);
        initialValue = getText().toString();
    }

    /**
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public PrePopulatedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTextColor(INITIAL_TEXT_COLOR);
        addTextChangedListener(this);
        setOnFocusChangeListener(this);
        initialValue = getText().toString();
    }

    @Override
    public void onFocusChange(View view, boolean isFocused) {

        if (!isTextFilled) {
            if (isFocused) {
                setTextColor(NORMAL_TEXT_COLOR);
                this.setText("");
            } else {
                setTextColor(INITIAL_TEXT_COLOR);
                this.setText(initialValue);
            }
        }
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {}

    @Override
    public void afterTextChanged(Editable editable) {

        String populatedValue = editable.toString();
        if (populatedValue != null && ( populatedValue.length() == 0) ||
                populatedValue.equalsIgnoreCase(initialValue)) {
            isTextFilled = false;
        } else {
            isTextFilled = true;
        }
    }

    /**
     *
     * @return
     */
    public String getValue() {

        if(!isTextFilled) {
            return "";
        }

        return getText().toString();
    }
}