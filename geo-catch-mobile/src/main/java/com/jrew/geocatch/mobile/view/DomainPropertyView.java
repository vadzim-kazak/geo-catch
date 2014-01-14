package com.jrew.geocatch.mobile.view;

import android.R;
import android.content.Context;
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
public class DomainPropertyView extends AutoCompleteTextView {

    /** **/
    public static final long FISH_DOMAIN_PROPERTY_TYPE = 1l;

    /** **/
    public static final long FISHING_TOOL_DOMAIN_PROPERTY_TYPE = 2l;

    /** **/
    public static final long FISHING_BAIT_DOMAIN_PROPERTY_TYPE = 3l;

    /** **/
    private static final int THRESHOLD_VALUE = 0;

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
        if (typedValue != null && typedValue.length() > 0) {

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

}
