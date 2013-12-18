package com.jrew.geocatch.mobile.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import com.jrew.geocatch.web.model.DomainProperty;

/**
 * Created with IntelliJ IDEA.
 * User: Vadim
 * Date: 12/18/13
 * Time: 2:58 PM
 */
public class DomainPropertyView extends AutoCompleteTextView implements AdapterView.OnItemClickListener {

    /** **/
    private DomainProperty selectedDomainProperty;

    public DomainPropertyView(Context context) {
        super(context);
    }

    public DomainPropertyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView != null) {
            selectedDomainProperty = (DomainProperty) adapterView.getAdapter().getItem(i);
        }
    }

    /**
     *
     * @return
     */
    public DomainProperty getSelectedDomainProperty() {
        return selectedDomainProperty;
    }

    /**
     *
     * @return
     */
    public boolean isDomainPropertySelected() {
        if (selectedDomainProperty != null) {
            return true;
        }

        return false;
    }
}
