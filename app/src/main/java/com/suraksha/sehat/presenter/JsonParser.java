package com.suraksha.sehat.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.suraksha.sehat.model.Plans;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    /** Tag for the log messages */
    private static final String LOG_TAG = JsonParser.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link JsonParser} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name JsonParser (and an object instance of JsonParser is not needed).
     */
    private JsonParser() {
    }

    /**
     * Return a list of {@link Plans} objects that has been built up from
     * parsing the given JSON response.
     */
    public static List<Plans> extractFeatureFromJson(String plansJSONArray) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(plansJSONArray)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding plans to
        List<Plans> plans = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONArray from the JSON response string
            JSONArray baseJsonResponse = new JSONArray(plansJSONArray);

            // For each plan in the plansArray, create an {@link Earthquake} object
            for (int i = 0; i < baseJsonResponse.length(); i++) {

                // Get a single plan at position i within the list of plans
                JSONObject currentPlan = baseJsonResponse.getJSONObject(i);

                // Extract the planId for the key called "_id"
                String planId = currentPlan.getString("_id");

                // Extract the familySizeId for the key called "family_size_id"
                String familySizeId = currentPlan.getString("family_size_id");

                // Extract the sumInsuredId for the key called "sum_insured_id"
                String sumInsuredId = currentPlan.getString("sum_insured_id");

                // Extract the companyId for the key called "company_id"
                String companyId = currentPlan.getString("company_id");

                // Extract the planName for the key called "plan_name"
                String planName = currentPlan.getString("plan_name");

                // Extract the lowerAgeGroup for the key called "lower_age_group"
                double lowerAgeGroup = currentPlan.getDouble("lower_age_group");

                // Extract the upperAgeGroup for the key called "upper_age_group"
                int upperAgeGroup = currentPlan.getInt("upper_age_group");

                // Extract the premiumAmount for the key called "premium_amount"
                int premiumAmount = currentPlan.getInt("premium_amount");

                // Extract the categoryId for the key called "category_id"
                String categoryId = currentPlan.getString("category_id");

                // Extract the categoryId for the key called "plan_type"
                String planType = currentPlan.getString("plan_type");

                // Extract the JSONArray associated with the key called "family_size",
                // which represents family size info (or plans).
                JSONArray familySizeArray = currentPlan.getJSONArray("family_size");

                // Get the family size info at position 0 within the list of plans
                JSONObject familySizeObject = familySizeArray.getJSONObject(0);

                // Extract the familySize for the key called "family_size"
                String familySize = familySizeObject.getString("family_size");

                // Extract the adult for the key called "adult"
                int adult = familySizeObject.getInt("adult");

                // Extract the child for the key called "child"
                int child = familySizeObject.getInt("child");

                // Extract the JSONArray associated with the key called "sum_insured",
                // which represents sum insured info (or plans).
                JSONArray sumInsuredArray = currentPlan.getJSONArray("sum_insured");

                // Get the sum insured info at position 0 within the list of plans
                JSONObject sumInsuredObject = sumInsuredArray.getJSONObject(0);

                // Extract the sumInsured for the key called "amount"
                int amount = sumInsuredObject.getInt("amount");

                // Extract the JSONArray associated with the key called "company",
                // which represents company info (or plans).
                JSONArray companyArray = currentPlan.getJSONArray("company");

                // Get the company info at position 0 within the list of plans
                JSONObject companyObject = companyArray.getJSONObject(0);

                // Extract the companyName for the key called "company_name"
                String companyName = companyObject.getString("company_name");

                // Create a new {@link Plan} object with the parameters
                // and url from the JSON response.
                Plans plan = new Plans(planId, familySizeId, sumInsuredId, companyId, planName,
                        lowerAgeGroup, upperAgeGroup, premiumAmount, categoryId, planType, familySize,
                        adult, child, amount, companyName);

                // Add the new {@link Plan} to the list of plans.
                plans.add(plan);

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the plans JSON results", e);
        }

        // Return the list of plans
        return plans;
    }

}
