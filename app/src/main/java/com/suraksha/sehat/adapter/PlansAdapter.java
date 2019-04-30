package com.suraksha.sehat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.suraksha.sehat.R;
import com.suraksha.sehat.model.Plans;

import java.util.List;

/**
 * An {@link PlansAdapter} knows how to create a list item layout for each plan
 * in the data source (a list of {@link Plans} objects).
 *
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class PlansAdapter extends ArrayAdapter<Plans> {

    /**
     * Current GST Tax Rate on HealthCare Insurance
     */
    private static final int GST_TAX_RATE = 18;

    /**
     * Constructs a new {@link PlansAdapter}.
     *
     * @param context of the app
     * @param plans is the list of plans, which is the data source of the adapter
     */
    public PlansAdapter(Context context, List<Plans> plans) {
        super(context, 0, plans);
    }

    /**
     * Returns a list item view that displays information about the plan at the given position
     * in the list of plans.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.plans_list_item, parent, false);
        }

        // Find the plan at the given position in the list of plans
        Plans currentPlan = getItem(position);

        // Find the views we need to update on the plans screen
        TextView companyName = (TextView) listItemView.findViewById(R.id.company_name);
        TextView sumInsured = (TextView) listItemView.findViewById(R.id.sum_insured);
        TextView adultCount = (TextView) listItemView.findViewById(R.id.adult_count);
        TextView childCount = (TextView) listItemView.findViewById(R.id.child_count);
        TextView ageGroup = (TextView) listItemView.findViewById(R.id.age_group);
        TextView premiumAmount = (TextView) listItemView.findViewById(R.id.premium_amount);

        // Set the values to their respective views
        companyName.setText(currentPlan.getCompanyName());
        sumInsured.setText(String.valueOf(currentPlan.getSumInsured()) + " /-");
        adultCount.setText(String.valueOf(currentPlan.getAdultCount()) + " Adult");
        childCount.setText(String.valueOf(currentPlan.getChildCount()) + " Child");
        ageGroup.setText(String.valueOf(currentPlan.getLowerAgeGroup()) + " - " + String.valueOf(currentPlan.getUpperAgeGroup()) + " Years");

        // Return the list item view that is now showing the appropriate data
        return listItemView;

    }
}
