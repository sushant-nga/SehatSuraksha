package com.suraksha.sehat.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.suraksha.sehat.R;
import com.suraksha.sehat.utils.Url;

import org.json.JSONArray;
import org.json.JSONObject;

public class SpecialActivity extends AppCompatActivity {

    /** Spinner field to enter the age group */
    private Spinner mAgeGroup;

    /** Spinner field to enter the family size */
    private Spinner mFamilySize;

    /** Spinner field to enter the sum insured */
    private Spinner mSumInsured;

    private Button proceedButton;

    private ProgressBar progressIndicator;

    private TextView responseString;

    public static final String TAG = "SpecialActivity";
    JsonArrayRequest jsonArrayRequest;
    RequestQueue requestQueue;


    /**
     * Age Group of the user. The possible valid values are in the InsuranceContract.java file.
     */
    private int mAge = 2;

    /**
     * Family Size of the user. The possible valid values are in the InsuranceContract.java file.
     */
    private int mSize = 3;

    /**
     * Sum Insured of the user. The possible valid values are in the InsuranceContract.java file.
     */
    private int mSum = 3;

    //LinearLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);

        mAgeGroup = (Spinner) findViewById(R.id.spinner_age_group);
        mFamilySize = (Spinner) findViewById(R.id.spinner_family_size);
        mSumInsured = (Spinner) findViewById(R.id.spinner_sum_insured);

        proceedButton = (Button) findViewById(R.id.button_proceed);

        progressIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        responseString = (TextView) findViewById(R.id.response);

        //rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        //rootLayout.getBackground().setAlpha(120);

        // Invalidating some Views until data is loaded
        mAgeGroup.setClickable(false);
        mAgeGroup.setFocusable(false);
        mFamilySize.setClickable(false);
        mFamilySize.setFocusable(false);
        mSumInsured.setClickable(false);
        mSumInsured.setFocusable(false);
        proceedButton.setClickable(false);
        proceedButton.setFocusable(false);
        proceedButton.setBackgroundResource(R.color.colorPrimaryDisabled);
        progressIndicator.setVisibility(View.VISIBLE);

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Instantiate the RequestQueue.
                requestQueue = Volley.newRequestQueue(SpecialActivity.this);

                // Request a JSON response from the provided URL.
                jsonArrayRequest = new JsonArrayRequest
                        (Request.Method.GET, Url.BASE_URL + Url.SUM_INSURED_LIST, null, new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                validateViews();
                                Log.d("ResponseArray", response.toString());
                                responseString.setText("Response: " + response.toString());
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                responseString.setText("Result cannot be parsed");

                            }
                        });

                // Set the tag on the request.
                jsonArrayRequest.setTag(TAG);

                // Add the request to the RequestQueue.
                requestQueue.add(jsonArrayRequest);

            }
        });

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the spinner categories.
     */
    private void setupSpinner() {

        // For age group.
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter ageGroupAdapter = ArrayAdapter.createFromResource(this,
                R.array.age_group_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        ageGroupAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mAgeGroup.setAdapter(ageGroupAdapter);

        // Set the integer mSelected to the constant values
        mAgeGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    switch(selection) {
                        case "20":
                            mAge = 20;
                            break;
                        case "21":
                            mAge = 21;
                            break;
                        case "26":
                            mAge = 26;
                            break;
                        case "31":
                            mAge = 31;
                            break;
                        case "36":
                            mAge = 36;
                            break;
                        case "41":
                            mAge = 41;
                            break;
                        case "46":
                            mAge = 46;
                            break;
                        case "51":
                            mAge = 51;
                            break;
                        case "56":
                            mAge = 56;
                            break;
                        case "61":
                            mAge = 61;
                            break;
                        case "66":
                            mAge = 66;
                            break;
                        case "71":
                            mAge = 71;
                            break;
                        case "76":
                            mAge = 76;
                            break;
                        default:
                            mAge = 30;
                            break;
                    }
                }
                //spinnerValidation();
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mAge = 30;
            }
        });

        // For family size.
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter familySizeAdapter = ArrayAdapter.createFromResource(this,
                R.array.family_size_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        familySizeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mFamilySize.setAdapter(familySizeAdapter);

        // Set the integer mSelected to the constant values
        mFamilySize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    switch(selection) {
                        case "20":
                            mSize = 20;
                            break;
                        case "21":
                            mSize = 21;
                            break;
                        case "26":
                            mSize = 26;
                            break;
                        case "31":
                            mSize = 31;
                            break;
                        case "36":
                            mSize = 36;
                            break;
                        case "41":
                            mSize = 41;
                            break;
                        case "46":
                            mSize = 46;
                            break;
                        case "51":
                            mSize = 51;
                            break;
                        case "56":
                            mSize = 56;
                            break;
                        case "61":
                            mSize = 61;
                            break;
                        case "66":
                            mSize = 66;
                            break;
                        case "71":
                            mSize = 71;
                            break;
                        case "76":
                            mSize = 76;
                            break;
                        default:
                            mSize = 30;
                            break;
                    }
                }
                //spinnerValidation();
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSize = 30;
            }
        });

        // For sum insured.
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter sumInsuredAdapter = ArrayAdapter.createFromResource(this,
                R.array.sum_insured_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        sumInsuredAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mSumInsured.setAdapter(sumInsuredAdapter);

        // Set the integer mSelected to the constant values
        mSumInsured.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    switch(selection) {
                        case "20":
                            mSum = 20;
                            break;
                        case "21":
                            mSum = 21;
                            break;
                        case "26":
                            mSum = 26;
                            break;
                        case "31":
                            mSum = 31;
                            break;
                        case "36":
                            mSum = 36;
                            break;
                        case "41":
                            mSum = 41;
                            break;
                        case "46":
                            mSum = 46;
                            break;
                        case "51":
                            mSum = 51;
                            break;
                        case "56":
                            mSum = 56;
                            break;
                        case "61":
                            mSum = 61;
                            break;
                        case "66":
                            mSum = 66;
                            break;
                        case "71":
                            mSum = 71;
                            break;
                        case "76":
                            mSum = 76;
                            break;
                        default:
                            mSum = 30;
                            break;
                    }
                }
                //spinnerValidation();
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSum = 30;
            }
        });

    }

    @Override
    protected void onStop () {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }

    public void validateViews() {

        // Validating views so that User can interact with them
        mAgeGroup.setClickable(true);
        mAgeGroup.setFocusable(true);
        mFamilySize.setClickable(true);
        mFamilySize.setFocusable(true);
        mSumInsured.setClickable(true);
        mSumInsured.setFocusable(true);
        proceedButton.setClickable(true);
        proceedButton.setFocusable(true);
        proceedButton.setBackgroundResource(R.color.colorPrimary);
        progressIndicator.setVisibility(View.GONE);

    }

}
