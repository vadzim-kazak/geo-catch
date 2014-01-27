package com.jrew.geocatch.mobile.view;

import android.R;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Filterable;
import android.widget.ListAdapter;
import com.jrew.geocatch.mobile.adapter.DomainAutoCompleteAdapter;
import com.jrew.geocatch.web.model.DomainProperty;

import java.util.List;
import java.util.Locale;

/**
 * Domain Property AutoComplete view
 */
public class DomainPropertyView extends AutoCompleteTextView implements TextWatcher, View.OnFocusChangeListener {

    /** **/
    private static int INITIAL_TEXT_COLOR = Color.GRAY;

    /** **/
    private static int NORMAL_TEXT_COLOR = Color.BLACK;

    /** **/
    public static final long FISH_DOMAIN_PROPERTY_TYPE = 1l;

    /** **/
    public static final long FISHING_TOOL_DOMAIN_PROPERTY_TYPE = 2l;

    /** **/
    public static final long FISHING_BAIT_DOMAIN_PROPERTY_TYPE = 3l;

    /** **/
    private static final int THRESHOLD_VALUE = 0;

    /** **/
    private boolean isTextFilled;

    /** **/
    private String initialValue;

    /** **/
    private DomainAutoCompleteAdapter adapter;

    /**
     *
     * @param context
     */
    public DomainPropertyView(Context context) {
        super(context);
        adapter = new DomainAutoCompleteAdapter(context, R.layout.simple_dropdown_item_1line, null);
        setAdapter(adapter);
        setThreshold(THRESHOLD_VALUE);
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
    public DomainPropertyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        adapter = new DomainAutoCompleteAdapter(context, R.layout.simple_dropdown_item_1line, null);
        setAdapter(adapter);
        setThreshold(THRESHOLD_VALUE);
        setTextColor(INITIAL_TEXT_COLOR);
        addTextChangedListener(this);
        setOnFocusChangeListener(this);
        initialValue = getText().toString();
    }

    /**
     *
     * @param domainProperties
     */
    public void populateDomainProperties(List<DomainProperty> domainProperties) {
        adapter.setInitialDomainProperties(domainProperties);
    }

    /**
     *
     * @return
     * @param createIfNotExisted
     */
    public DomainProperty getSelectedDomainProperty(boolean createIfNotExisted) {

        // Currently typed value
        String typedValue = getText().toString();
        if (typedValue != null && typedValue.length() > 0 && !typedValue.equalsIgnoreCase(initialValue)) {

            DomainProperty selectedDomainProperty = adapter.getDomainPropertyByValue(typedValue);
            if (selectedDomainProperty == null && createIfNotExisted) {
                // New value is typed. Create domain property from scratch
                //Value
                selectedDomainProperty = new DomainProperty();
                selectedDomainProperty.setValue(typedValue);

                // Locale
                String locale = Locale.getDefault().getLanguage();
                selectedDomainProperty.setLocale(locale);
            }

            return selectedDomainProperty;
        }

        return null;
    }

    /**
     *
     * @param domainProperty
     */
    public void setSelectedDomainProperty(DomainProperty domainProperty) {
        setText(domainProperty.getValue());
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
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

    }

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


}
