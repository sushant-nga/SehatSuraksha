package com.suraksha.sehat.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.suraksha.sehat.utils.Url;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class PlansActivity extends AppCompatActivity {
    String sizeId,sumId,age;
    JsonArrayRequest jsonArrayRequest;
    RequestQueue requestQueue;
    TextView responseString;

    public static final String TAG = "SpecialActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans);

        responseString = (TextView) findViewById(R.id.response_string);
        sizeId = getIntent().getStringExtra("mSizeId");
        sumId = getIntent().getStringExtra("mSumId");
        age = getIntent().getStringExtra("age");

        //POST API call
        // Instantiate the RequestQueue for SumInsured Params.
        requestQueue = Volley.newRequestQueue(PlansActivity.this);


        // Request a JSON response from the provided URL.
        jsonArrayRequest = new JsonArrayRequest
                (Request.Method.POST, Url.BASE_URL + Url.PLANS_LIST, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("ResponseArray", response.toString());

                        responseString.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        responseString.setText("Result cannot be parsed");

                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("family_size_id", sizeId);
                params.put("sum_insured_id", sumId);
                params.put("age", age);
                return params;
            }
        };

        // Set the tag on the request.
        jsonArrayRequest.setTag(TAG);

        // Instantiate the RequestQueue for FamilySize Params.
        requestQueue = Volley.newRequestQueue(PlansActivity.this);



        // Add the request to the RequestQueue.
        requestQueue.add(jsonArrayRequest);

    }
}
