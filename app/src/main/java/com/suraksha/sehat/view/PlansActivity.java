package com.suraksha.sehat.view;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.suraksha.sehat.R;
import com.suraksha.sehat.adapter.PlansAdapter;
import com.suraksha.sehat.model.Plans;
import com.suraksha.sehat.presenter.JsonParser;
import com.suraksha.sehat.utils.Url;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlansActivity extends AppCompatActivity {
    String sizeId,sumId,age;

    View loadingIndicator;

    /** Adapter for the list of plans */
    private PlansAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    public static final String LOG_TAG = "PlansActivity";

    public List<Plans> plans;

    //JsonArrayRequest jsonArrayRequest;
    StringRequest stringRequest;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);

        loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);

        // Find a reference to the {@link ListView} in the layout
        ListView plansListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        plansListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of plans as input
        mAdapter = new PlansAdapter(this, new ArrayList<Plans>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        plansListView.setAdapter(mAdapter);

        sizeId = getIntent().getStringExtra("mSizeId");
        sumId = getIntent().getStringExtra("mSumId");
        age = getIntent().getStringExtra("age");
        Log.d("PlansActivity(mSizeId)", sizeId);
        Log.d("PlansActivity(mSumId)", sumId);
        Log.d("PlansActivity(age)", age);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            //POST API call
            // Request a JSON response from the provided URL.
            stringRequest = new StringRequest
                    (Request.Method.POST, Url.BASE_URL + Url.PLANS_LIST,  new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            Log.d("ResponseArray", response);

                            // Parse the response, and extract a list of plans.
                            plans = JsonParser.extractFeatureFromJson(response);

                            // Hide loading indicator because the data has been loaded
                            loadingIndicator.setVisibility(View.GONE);

                            // Clear the adapter of previous plans data
                            //mAdapter.clear();

                            // If there is a valid list of {@link Plans}s, then add them to the adapter's
                            // data set. This will trigger the ListView to update.
                            if (plans != null && !plans.isEmpty()) {
                                mAdapter.addAll(plans);
                                //updateUi(earthquakes);
                            } else {
                                // Set empty state text to display "No plans found."
                                mEmptyStateTextView.setText(R.string.no_plans);
                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("API Call", "failed");
                        }
                    }) {
                // adding parameters to the request
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("family_size_id", sizeId);
                    params.put("sum_insured_id", sumId);
                    params.put("age", age);

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> header = new HashMap<String, String>();
                    header.put("Content-Type", "application/x-www-form-urlencoded");
                    return header;
                }
            };

            // Set the tag on the request.
            stringRequest.setTag(LOG_TAG);

            // Instantiate the RequestQueue for FamilySize Params.
            requestQueue = Volley.newRequestQueue(PlansActivity.this);



            // Add the request to the RequestQueue.
            requestQueue.add(stringRequest);

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

    }

}
