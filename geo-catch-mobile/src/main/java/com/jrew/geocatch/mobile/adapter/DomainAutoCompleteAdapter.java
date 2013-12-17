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

    public DomainAutoCompleteAdapter(Context context, int textViewResourceId, List<DomainProperty> domainProperties) {
        super(context, textViewResourceId);
        this.initialDomainProperties = domainProperties;
        filteredDomainProperties = new ArrayList<DomainProperty>(initialDomainProperties.size());
    }

    @Override
    public int getCount() {
        return initialDomainProperties.size();
    }

    @Override
    public DomainProperty getItem(int index) {
        return initialDomainProperties.get(index);
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
            protected void publishResults(CharSequence contraint, FilterResults results) {

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
        filteredDomainProperties.clear();
        for (DomainProperty domainProperty : initialDomainProperties) {
            if (domainProperty.getValue().startsWith(constraint)) {
                filteredDomainProperties.add(domainProperty);
            }
        }
    }
}
