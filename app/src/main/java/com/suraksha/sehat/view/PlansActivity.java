package com.suraksha.sehat.view;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.suraksha.sehat.R;
import com.suraksha.sehat.adapter.PlansAdapter;
import com.suraksha.sehat.model.Plans;
import com.suraksha.sehat.network.PlansLoader;
import com.suraksha.sehat.presenter.JsonParser;
import com.suraksha.sehat.utils.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlansActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Plans>> {
    String sizeId,sumId,age;
    JsonArrayRequest jsonArrayRequest;
    RequestQueue requestQueue;

    /** Adapter for the list of plans */
    private PlansAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int PLANS_LOADER_ID = 1;

    public static final String TAG = "SpecialActivity";

    public List<Plans> plans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);

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
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null fort
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(PLANS_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

    }

    @Override
    public Loader<List<Plans>> onCreateLoader(int i, Bundle bundle) {

        return new PlansLoader(this, sizeId, sumId, age);
    }

    @Override
    public void onLoadFinished(Loader<List<Plans>> loader, List<Plans> plans) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No plans found."
        mEmptyStateTextView.setText(R.string.no_plans);

        // Clear the adapter of previous plans data
        //mAdapter.clear();

        // If there is a valid list of {@link Plans}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (plans != null && !plans.isEmpty()) {
            mAdapter.addAll(plans);
            //updateUi(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Plans>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

//    /**
//     * Make an HTTP request to the given URL and return a String as the response.
//     */
//    public List<Plans> makeHttpRequest() {
//
//        //POST API call
//        // Instantiate the RequestQueue for SumInsured Params.
//        requestQueue = Volley.newRequestQueue(PlansActivity.this);
//
//        // Request a JSON response from the provided URL.
//        jsonArrayRequest = new JsonArrayRequest
//                (Request.Method.POST, Url.BASE_URL + Url.PLANS_LIST, null, new Response.Listener<JSONArray>() {
//
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.d("ResponseArray", response.toString());
//
//                        // Parse the response, and extract a list of plans.
//                        plans = JsonParser.extractFeatureFromJson(response.toString());
//                    }
//                }, new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                }) {
//            @Override
//            protected Map<String,String> getParams(){
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("family_size_id", sizeId);
//                params.put("sum_insured_id", sumId);
//                params.put("age", age);
//
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String,String> params = new HashMap<String, String>();
//                params.put("Content-Type","application/x-www-form-urlencoded");
//                return params;
//            }
//        };
//
//        // Set the tag on the request.
//        jsonArrayRequest.setTag(TAG);
//
//        // Instantiate the RequestQueue for FamilySize Params.
//        requestQueue = Volley.newRequestQueue(PlansActivity.this);
//
//
//
//        // Add the request to the RequestQueue.
//        requestQueue.add(jsonArrayRequest);
//
//        return plans;
//
//    }
}
