package com.jrew.geocatch.mobile.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import com.jrew.geocatch.web.model.DomainProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Kazak_VV
 * Date: 17.12.13
 * Time: 18:15
 * To change this template use File | Settings | File Templates.
 */
public class DomainAutoCompleteAdapter extends ArrayAdapter<DomainProperty> implements Filterable {

    /** Initial list of domain properties **/
    private List<DomainProperty> initialDomainProperties;

    /** **/
    private List<DomainProperty> filteredDomainProperties;

    /**
     *
     * @param context
     * @param textViewResourceId
     * @param domainProperties
     */
    public DomainAutoCompleteAdapter(Context context, int textViewResourceId, List<DomainProperty> domainProperties) {
        super(context, textViewResourceId);
        this.initialDomainProperties = domainProperties;
        filteredDomainProperties = new ArrayList<DomainProperty>();
    }

    @Override
    public int getCount() {
        return filteredDomainProperties.size();
    }

    @Override
    public DomainProperty getItem(int index) {
        return filteredDomainProperties.get(index);
    }

    @Override
    public Filter getFilter() {

        Filter myFilter = new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();

                if(constraint != null) {
                    // Need to filter result here by constraint value
                    filterDomainProperties(constraint.toString());
                    // Now assign the values and count to the FilterResults object
                    filterResults.values = filteredDomainProperties;
                    filterResults.count = filteredDomainProperties.size();
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if(results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return myFilter;
    }

    /**
     *
     * @param constraint
     */
    private void filterDomainProperties(String constraint) {

        String lowerCaseConstraint = constraint.toLowerCase();

        filteredDomainProperties.clear();
        for (DomainProperty domainProperty : initialDomainProperties) {
            if (domainProperty.getValue().startsWith(lowerCaseConstraint)) {
                filteredDomainProperties.add(domainProperty);
            }
        }
    }

    /**
     *
     * @param value
     * @return
     */
    public DomainProperty getDomainPropertyByValue(String value) {
        if (initialDomainProperties != null) {
            for (DomainProperty domainProperty : initialDomainProperties) {
                if (domainProperty.getValue().equalsIgnoreCase(value)) {
                    return domainProperty;
                }
            }
        }

        return null;
    }

    /**
     *
     * @param initialDomainProperties
     */
    public void setInitialDomainProperties(List<DomainProperty> initialDomainProperties) {
        this.initialDomainProperties = initialDomainProperties;
    }

    /**
     *
     * @return
     */
    public boolean isPopulated() {

        if (initialDomainProperties != null && initialDomainProperties.size() > 0) {
            return true;
        }

        return false;
    }
}
