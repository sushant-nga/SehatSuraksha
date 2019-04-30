package com.suraksha.sehat.network;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.suraksha.sehat.model.Plans;
import com.suraksha.sehat.presenter.JsonParser;
import com.suraksha.sehat.utils.Url;
import com.suraksha.sehat.view.PlansActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper methods related to requesting and receiving plans data from Backend.
 */
public final class QueryUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    static List<Plans> plans;

    static String mSizeId, mSumId, mAge;

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the Backend dataset and return a list of {@link Plans} objects.
     */
    public static List<Plans> fetchPlansData(Context context, String sizeId, String sumId,
                                             String age, String requestUrl) {

        JsonArrayRequest jsonArrayRequest;
        RequestQueue requestQueue;
        mSizeId = sizeId;
        mSumId = sumId;
        mAge = age;
        Log.d("SizeId", mSizeId);
        Log.d("SumId", mSumId);
        Log.d("Age", mAge);

        JSONObject parameters = new JSONObject();
        try {
            parameters.put("family_size_id", mSizeId);
            parameters.put("sum_insured_id", mSumId);
            parameters.put("age", mAge);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray arrayParams = new JSONArray(parameters);

        //POST API call

        // Request a JSON response from the provided URL.
        jsonArrayRequest = new JsonArrayRequest
                (Request.Method.POST, requestUrl, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("ResponseArray", response.toString());

                        // Parse the response, and extract a list of plans.
                        plans = JsonParser.extractFeatureFromJson(response.toString());

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
                params.put("family_size_id", mSizeId);
                params.put("sum_insured_id", mSumId);
                params.put("age", mAge);

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
        jsonArrayRequest.setTag(LOG_TAG);

        // Instantiate the RequestQueue for FamilySize Params.
        requestQueue = Volley.newRequestQueue(context);



        // Add the request to the RequestQueue.
        requestQueue.add(jsonArrayRequest);

        return plans;

    }

}
