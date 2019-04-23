package com.suraksha.sehat.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import org.json.JSONException;
import org.json.JSONObject;

public class SpecialActivity extends AppCompatActivity {

    /** Spinner field to enter the age unit */
    private Spinner mAgeUnit;

    /** EditText field to enter the age group */
    private EditText mAgeGroup;

    /** Spinner field to enter the family size */
    private Spinner mFamilySize;

    /** Spinner field to enter the sum insured */
    private Spinner mSumInsured;

    private Button proceedButton;

    private ProgressBar progressIndicator;

    private TextView responseString;

    public static final String TAG = "SpecialActivity";
    JsonArrayRequest jsonArrayRequest1, jsonArrayRequest2;
    RequestQueue requestQueue;

    int REQUEST_FLAG = 0;

    private String sumInsuredId [] = new String [16];
    private int sumInsured [] = new int [16];

    private String familySizeId [] = new String [10];
    private int familyAdultSize [] = new int [10];
    private int familyChildSize [] = new int [10];

    /**
     * Age Unit. The possible valid values are in the InsuranceContract.java file.
     */
    private int mUnit;

//    /**
//     * Age Group of the user. The possible valid values are in the InsuranceContract.java file.
//     */
//    private int mAge = 17;

    /**
     * Family Size. The possible valid values are in the InsuranceContract.java file.
     */
    private double mSize = 1.0;
    private String mSizeId = "";

    /**
     * Sum Insured of the user. The possible valid values are in the InsuranceContract.java file.
     */
    private double mSum = 2.0;
    private String mSumId = "";

    //LinearLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);

        getInputParameters();

        mAgeGroup = (EditText) findViewById(R.id.age);
        mAgeUnit = (Spinner) findViewById(R.id.spinner_age_unit);
        mFamilySize = (Spinner) findViewById(R.id.spinner_family_size);
        mSumInsured = (Spinner) findViewById(R.id.spinner_sum_insured);

        proceedButton = (Button) findViewById(R.id.button_proceed);

        progressIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        responseString = (TextView) findViewById(R.id.response);

        validateViews(false);
        ageUnitValidator();

        mAgeGroup.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() > 2) {
                    mAgeGroup.setText("");
                    Toast.makeText(SpecialActivity.this, "Age cannot be more than 100", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        //rootLayout.getBackground().setAlpha(120);

        if (REQUEST_FLAG == 2) {
            // Validating the views because data is loaded
            //validateViews(true);
        } else {
            // Invalidating some views until data is loaded
            //validateViews(false);
        }

//        proceedButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                // Instantiate the RequestQueue.
//                requestQueue = Volley.newRequestQueue(SpecialActivity.this);
//
//                // Request a JSON response from the provided URL.
//                jsonArrayRequest = new JsonArrayRequest
//                        (Request.Method.GET, Url.BASE_URL + Url.SUM_INSURED_LIST, null, new Response.Listener<JSONArray>() {
//
//                            @Override
//                            public void onResponse(JSONArray response) {
//                                validateViews();
//                                Log.d("ResponseArray", response.toString());
//                                responseString.setText("Response: " + response.toString());
//                            }
//                        }, new Response.ErrorListener() {
//
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                responseString.setText("Result cannot be parsed");
//
//                            }
//                        });
//
//                // Set the tag on the request.
//                jsonArrayRequest.setTag(TAG);
//
//                // Add the request to the RequestQueue.
//                requestQueue.add(jsonArrayRequest);
//
//            }
//        });

    }

    public void getInputParameters() {

        // Instantiate the RequestQueue for SumInsured Params.
        requestQueue = Volley.newRequestQueue(SpecialActivity.this);

        // Request a JSON response from the provided URL.
        jsonArrayRequest1 = new JsonArrayRequest
                (Request.Method.GET, Url.BASE_URL + Url.SUM_INSURED_LIST, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("ResponseArray", response.toString());
                        REQUEST_FLAG += 1;
                        parseJsonResult(response, 1);
                        responseString.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        responseString.setText("Result cannot be parsed");

                    }
                });

        // Set the tag on the request.
        jsonArrayRequest1.setTag(TAG);

        // Instantiate the RequestQueue for FamilySize Params.
        requestQueue = Volley.newRequestQueue(SpecialActivity.this);

        // Request a JSON response from the provided URL.
        jsonArrayRequest2 = new JsonArrayRequest
                (Request.Method.GET, Url.BASE_URL + Url.FAMILY_SIZE_LIST, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("ResponseArray", response.toString());
                        REQUEST_FLAG += 1;
                        if (REQUEST_FLAG == 2) {
                            //validateViews(true);
                            parseJsonResult(response, 2);
                        }
                        responseString.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        responseString.setText("Result cannot be parsed");

                    }
                });

        // Set the tag on the request.
        jsonArrayRequest2.setTag(TAG);

        // Add the request to the RequestQueue.
        requestQueue.add(jsonArrayRequest1);
        requestQueue.add(jsonArrayRequest2);

    }

    public void parseJsonResult(JSONArray response, int flag) {

        //Traversing through all the items in the json array and storing it into local array
        for(int i = 0 ; i < response.length() ; i++) {
            try {
                //Getting JSON Object
                JSONObject json = response.getJSONObject(i);

                if (flag == 1) {
                    //Adding the id and amount into array
                    sumInsuredId[i] = json.getString("_id");
                    sumInsured[i] = json.getInt("amount");
                } else if (flag == 2) {
                    //Adding the id, adult and child into array
                    familySizeId[i] = json.getString("_id");
                    familyAdultSize[i] = json.getInt("adult");
                    familyChildSize[i] = json.getInt("child");
                    setupSpinner();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mSizeId = familySizeId[0];
            mSumId = sumInsuredId[0];
        }
    }

    /**
     * Setup the dropdown spinner that allows the user to select the spinner categories.
     */
    private void setupSpinner() {

        // For age unit.
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter ageUnitAdapter = ArrayAdapter.createFromResource(this,
                R.array.age_unit_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        ageUnitAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mAgeUnit.setAdapter(ageUnitAdapter);

        // Set the integer mSelected to the constant values
        mAgeUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("Days")) {
                        mUnit = 1;
                    } else if (selection.equals("Years")) {
                        mUnit = 2;
                    } else {
                        mUnit = 2;
                    }
                }
                ageUnitValidator();
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mUnit = 2;
                ageUnitValidator();
            }
        });

        // For family size.
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter familySizeAdultAdapter = ArrayAdapter.createFromResource(this,
                R.array.family_size_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        familySizeAdultAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mFamilySize.setAdapter(familySizeAdultAdapter);

        // Set the integer mSelected to the constant values
        mFamilySize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    switch(selection) {
                        case "Individual":
                            mSize = 1.0;
                            mSizeId = familySizeId[0];
                            break;
                        case "1 Adult + 1 Child":
                            mSize = 1.1;
                            mSizeId = familySizeId[1];
                            break;
                        case "1 Adult + 2 Child":
                            mSize = 1.2;
                            mSizeId = familySizeId[2];
                            break;
                        case "1 Adult + 3 Child":
                            mSize = 1.3;
                            mSizeId = familySizeId[3];
                            break;
                        case "1 Adult + 4 Child":
                            mSize = 1.4;
                            mSizeId = familySizeId[4];
                            break;
                        case "2 Adults":
                            mSize = 2.0;
                            mSizeId = familySizeId[5];
                            break;
                        case "2 Adults + 1 Child":
                            mSize = 2.1;
                            mSizeId = familySizeId[6];
                            break;
                        case "2 Adults + 2 Child":
                            mSize = 2.2;
                            mSizeId = familySizeId[7];
                            break;
                        case "2 Adults + 3 Child":
                            mSize = 2.3;
                            mSizeId = familySizeId[8];
                            break;
                        case "2 Adults + 4 Child":
                            mSize = 2.4;
                            mSizeId = familySizeId[9];
                            break;
                        default:
                            mSize = 1.0;
                            mSizeId = familySizeId[0];
                            break;
                    }
                    Log.d("mSizeId", mSizeId);
                }
                //spinnerValidation();
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSize = 1.0;
                mSizeId = familySizeId[0];
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
                        case "2,00,000":
                            mSum = 2.0;
                            mSumId = sumInsuredId[0];
                            break;
                        case "3,00,000":
                            mSum = 3.0;
                            mSumId = sumInsuredId[1];
                            break;
                        case "4,00,000":
                            mSum = 4.0;
                            mSumId = sumInsuredId[2];
                            break;
                        case "5,00,000":
                            mSum = 5.0;
                            mSumId = sumInsuredId[3];
                            break;
                        case "6,00,000":
                            mSum = 6.0;
                            mSumId = sumInsuredId[4];
                            break;
                        case "7,00,000":
                            mSum = 7.0;
                            mSumId = sumInsuredId[5];
                            break;
                        case "7,50,000":
                            mSum = 7.5;
                            mSumId = sumInsuredId[6];
                            break;
                        case "10,00,000":
                            mSum = 10.0;
                            mSumId = sumInsuredId[7];
                            break;
                        case "15,00,000":
                            mSum = 15.0;
                            mSumId = sumInsuredId[8];
                            break;
                        case "20,00,000":
                            mSum = 20.0;
                            mSumId = sumInsuredId[9];
                            break;
                        case "25,00,000":
                            mSum = 25.0;
                            mSumId = sumInsuredId[10];
                            break;
                        case "30,00,000":
                            mSum = 30.0;
                            mSumId = sumInsuredId[11];
                            break;
                        case "35,00,000":
                            mSum = 35.0;
                            mSumId = sumInsuredId[12];
                            break;
                        case "40,00,000":
                            mSum = 40.0;
                            mSumId = sumInsuredId[13];
                            break;
                        case "45,00,000":
                            mSum = 45.0;
                            mSumId = sumInsuredId[14];
                            break;
                        case "50,00,000":
                            mSum = 50.0;
                            mSumId = sumInsuredId[15];
                            break;
                        default:
                            mSum = 2.0;
                            mSumId = sumInsuredId[0];
                            break;
                    }

                    Log.d("mSumId", mSumId);
                }
                //spinnerValidation();
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSum = 2.0;
                mSumId = sumInsuredId[0];
            }
        });

        validateViews(true);

    }

    public void ageUnitValidator() {
        if (mUnit == 1) {
            mAgeGroup.setText("91");
            mAgeGroup.setClickable(false);
            mAgeGroup.setFocusable(false);
        } else if (mUnit == 2) {
            mAgeGroup.setFocusable(true);
            mAgeGroup.setClickable(true);
            mAgeGroup.setText("");
        }
    }

    @Override
    protected void onStop () {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }

    public void validateViews(boolean flag) {

        // Validating views so that User can interact with them
//        mAgeGroup.setClickable(flag);
//        mAgeGroup.setFocusable(flag);
        mFamilySize.setClickable(flag);
        mFamilySize.setFocusable(flag);
        mSumInsured.setClickable(flag);
        mSumInsured.setFocusable(flag);
        proceedButton.setClickable(flag);
        proceedButton.setFocusable(flag);
        if (flag == true) {
            proceedButton.setBackgroundResource(R.color.colorPrimary);
            progressIndicator.setVisibility(View.GONE);
        } else {
            proceedButton.setBackgroundResource(R.color.colorPrimaryDisabled);
            progressIndicator.setVisibility(View.VISIBLE);
        }

    }

}
