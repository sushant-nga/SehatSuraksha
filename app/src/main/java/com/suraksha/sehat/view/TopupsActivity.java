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
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.suraksha.sehat.R;
import com.suraksha.sehat.utils.Url;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TopupsActivity extends AppCompatActivity {

    /** Spinner field to enter the age unit */
    private Spinner mAgeUnit;

    /** EditText field to enter the age group */
    private EditText mAgeGroup;

    /** Spinner field to enter the family size */
    private Spinner mFamilySize;

    /** Spinner field to enter the sum insured */
    private Spinner mSumInsured;

    /** Spinner field to enter the deductable */
    private Spinner mDeductable;

    private int mDeductibleAmount;

    private Button proceedButton;

    private ProgressBar progressIndicator;

    public static final String TAG = "FloaterActivity";
    JsonArrayRequest jsonArrayRequest1, jsonArrayRequest2, jsonArrayRequest3;
    RequestQueue requestQueue;

    int REQUEST_FLAG = 0;

    private String sumInsuredId [] = new String [16];
    private int sumInsured [] = new int [16];

    private String familySizeId [] = new String [10];
    private int familyAdultSize [] = new int [10];
    private int familyChildSize [] = new int [10];

    private String categoryId [] = new String [6];
    private String categoryName [] = new String [6];

    String ageText;

    /**
     * Age Unit. The possible valid values are in the InsuranceContract.java file.
     */
    private int mUnit;

//    /**
//     * Age Group of the user. The possible valid values are in the InsuranceContract.java file.
//     */
//    private int mAge = 17;

    /**
     * Category. The possible valid values are in the InsuranceContract.java file.
     */
    private String mCategoryText = "Top ups";
    private String mCategoryId = "";
    private String mCategoryName;

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
        setContentView(R.layout.activity_topups);

        mAgeGroup = (EditText) findViewById(R.id.age);
        mAgeUnit = (Spinner) findViewById(R.id.spinner_age_unit);
        mFamilySize = (Spinner) findViewById(R.id.spinner_family_size);
        mSumInsured = (Spinner) findViewById(R.id.spinner_sum_insured);
        mDeductable = (Spinner) findViewById(R.id.spinner_deductable);

        proceedButton = (Button) findViewById(R.id.button_proceed);

        progressIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        validateViews(false);
        getInputParameters();
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
                    Toast.makeText(TopupsActivity.this, "Age cannot be more than 100", Toast.LENGTH_SHORT).show();
                } else {
                    ageText = s.toString();
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

        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mAgeGroup.getText())) {
                    Toast.makeText(TopupsActivity.this, getString(R.string.age_is_empty), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(TopupsActivity.this, PlansActivity.class);
                    intent.putExtra("mSizeId", mSizeId);
                    intent.putExtra("mSumId", mSumId);
                    if (mAgeUnit.getSelectedItem().equals("Years")) {
                        intent.putExtra("age", ageText);
                    } else if (mAgeUnit.getSelectedItem().equals("Days")) {
                        double ageInDays = (Double.parseDouble(ageText))/(double)365;
                        ageInDays = Math.round(ageInDays * 100.0) / 100.0;
                        intent.putExtra("age", String.valueOf(ageInDays));
                    }
                    intent.putExtra("categoryId", mCategoryId);
                    intent.putExtra("deductible", mDeductibleAmount);
                    intent.putExtra("fromActivity", "Topups");
                    Log.d("FloaterActivity(age)", ageText);
                    startActivity(intent);
                }
            }
        });

    }

    public void getInputParameters() {

        // Instantiate the RequestQueue for SumInsured Params.
        requestQueue = Volley.newRequestQueue(TopupsActivity.this);

        // Request a JSON response from the provided URL.
        jsonArrayRequest1 = new JsonArrayRequest
                (Request.Method.GET, Url.BASE_URL + Url.SUM_INSURED_LIST, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("ResponseArray[Sum]", response.toString());
                        REQUEST_FLAG += 1;
                        parseJsonResult(response, 1);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        // Set the tag on the request.
        jsonArrayRequest1.setTag(TAG);

        // Instantiate the RequestQueue for FamilySize Params.
        requestQueue = Volley.newRequestQueue(TopupsActivity.this);

        // Request a JSON response from the provided URL.
        jsonArrayRequest2 = new JsonArrayRequest
                (Request.Method.GET, Url.BASE_URL + Url.FAMILY_SIZE_LIST, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("ResponseArray[Family]", response.toString());
                        REQUEST_FLAG += 1;
                        //validateViews(true);
                        parseJsonResult(response, 2);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        // Set the tag on the request.
        jsonArrayRequest2.setTag(TAG);

        // Instantiate the RequestQueue for FamilySize Params.
        requestQueue = Volley.newRequestQueue(TopupsActivity.this);

        // Request a JSON response from the provided URL.
        jsonArrayRequest3 = new JsonArrayRequest
                (Request.Method.GET, Url.BASE_URL + Url.CATEGORY_LIST, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("ResponseArray[Category]", response.toString());
                        REQUEST_FLAG += 1;
                        //validateViews(true);
                        parseJsonResult(response, 3);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        // Set the tag on the request.
        jsonArrayRequest3.setTag(TAG);

        jsonArrayRequest1.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        jsonArrayRequest2.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        jsonArrayRequest3.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add the request to the RequestQueue.
        requestQueue.add(jsonArrayRequest1);
        requestQueue.add(jsonArrayRequest2);
        requestQueue.add(jsonArrayRequest3);

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
                } else if (flag == 3) {
                    //Adding the _id, category_id, category_name into array
                    categoryId[i] = json.getString("_id");
                    categoryName[i] = json.getString("category_name");
                }
                if (REQUEST_FLAG > 2) {
                    validateViews(true);
                    setupSpinner();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mSizeId = familySizeId[0];
            mSumId = sumInsuredId[0];
            mCategoryId = categoryId[1];
            mCategoryName = categoryName[1];
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

        // For deductible amount.
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter deductibleAdapter = ArrayAdapter.createFromResource(this,
                R.array.deductible_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        ageUnitAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mDeductable.setAdapter(deductibleAdapter);

        // Set the integer mSelected to the constant values
        mDeductable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("3,00,000")) {
                        mDeductibleAmount = 300000;
                    } else if (selection.equals("5,00,000")) {
                        mDeductibleAmount = 500000;
                    } else if (selection.equals("10,00,000")) {
                        mDeductibleAmount = 1000000;
                    } else {
                        mDeductibleAmount = 300000;
                    }
                }
                ageUnitValidator();
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mDeductibleAmount = 300000;
                ageUnitValidator();
            }
        });

        validateViews(true);

    }

    public void ageUnitValidator() {
        if (mUnit == 1) {
            mAgeGroup.setText("91");
            mAgeGroup.setEnabled(false);
            mAgeGroup.setFocusable(true);
        } else if (mUnit == 2) {
            mAgeGroup.setEnabled(true);
            mAgeGroup.setFocusable(true);
            mAgeGroup.getText().clear();
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
        mDeductable.setEnabled(flag);
        mDeductable.setSelection(0);
        mFamilySize.setEnabled(flag);
        mFamilySize.setSelection(0);
        mSumInsured.setEnabled(flag);
        mSumInsured.setSelection(0);
        proceedButton.setEnabled(flag);
        if (flag == true) {
            proceedButton.setBackgroundResource(R.color.colorPrimary);
            progressIndicator.setVisibility(View.GONE);
        } else {
            proceedButton.setBackgroundResource(R.color.colorPrimaryDisabled);
            progressIndicator.setVisibility(View.VISIBLE);
        }

    }

}
