package com.suraksha.sehat.utils;

public class Url {

    // local server - Deeksha's System (WiFi - Algorithm)
    public static String BASE_URL = "http://10.0.0.246:3003/";

    // List of APIs

    /**
     * Sum Insured List
     * METHOD - GET
     * PARAMS - nil
     * RESPONSE - JSON
     */
    public static String SUM_INSURED_LIST = "plans/getsumassured";

    /**
     * Category List
     * METHOD - GET
     * PARAMS - nil
     * RESPONSE - JSON
     */
    public static String CATEGORY_LIST = "plans/getcategories";

    /**
     * Family Size List
     * METHOD - GET
     * PARAMS - nil
     * RESPONSE - JSON
     */
    public static String FAMILY_SIZE_LIST = "plans/getfamilysizes";

    /**
     * Plans List according to parameters
     * METHOD - POST
     * PARAMS AS BODY - family_size_id, sum_insured_id, age
     * RESPONSE - JSON response
     */
    public static String PLANS_LIST = "plans/getplans";

}
