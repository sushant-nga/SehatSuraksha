package com.suraksha.sehat.model;

public class Plans {

    /** PlanId of the plan */
    private String mPlanId;

    /** FamilySizeId of the plan */
    private String mFamilySizeId;

    /** SumInsuredId of the plan */
    private String mSumInsuredId;

    /** CompanyId of the plan */
    private String mCompanyId;

    /** PlanName of the plan */
    private String mPlanName;

    /** LowerAgeGroup of the plan */
    private double mLowerAgeGroup;

    /** UpperAgeGroup of the plan */
    private int mUpperAgeGroup;

    /** PremiumAmount of the plan */
    private int mPremiumAmount;

    /** CategoryId of the plan */
    private String mCategoryId;

    /** FamilySize of the plan */
    private String mFamilySize;

    /** Adult Quantity of the plan */
    private int mAdult;

    /** Children Quantity of the plan */
    private int mChild;

    /** SumInsured of the plan */
    private int mSumInsured;

    /** Company Name of the plan */
    private String mCompanyName;

    /**
     * Constructs a new {@link Plans} object.
     */
    public Plans(String planId, String familySizeId, String sumInsuredId, String companyId, String planName,
                 double lowerAgeGroup, int upperAgeGroup, int premiumAmount, String categoryId, String familySize,
                 int adult, int child, int amount, String companyName) {
        mPlanId = planId;
        mFamilySizeId = familySizeId;
        mSumInsuredId = sumInsuredId;
        mCompanyId = companyId;
        mPlanName = planName;
        mLowerAgeGroup = lowerAgeGroup;
        mUpperAgeGroup = upperAgeGroup;
        mPremiumAmount = premiumAmount;
        mCategoryId = categoryId;
        mFamilySize = familySize;
        mAdult = adult;
        mChild = child;
        mSumInsured = amount;
        mCompanyName = companyName;
    }

    /**
     * Returns the planId of the plan.
     */
    public String getPlanId() {
        return mPlanId;
    }

    /**
     * Returns the premiumAmount of the plan.
     */
    public int getPremiumAmount() {
        return mPremiumAmount;
    }

    /**
     * Returns the planName of the plan.
     */
    public String getPlanName() {
        return mPlanName;
    }

    /**
     * Returns the familySize of the plan.
     */
    public String getFamilySize() {
        return mFamilySize;
    }

    /**
     * Returns the sumInsured of the plan.
     */
    public int getSumInsured() {
        return mSumInsured;
    }

    /**
     * Returns the companyName of the plan.
     */
    public String getCompanyName() {
        return mCompanyName;
    }

    /**
     * Returns the adult count of the plan.
     */
    public int getAdultCount() {
        return mAdult;
    }

    /**
     * Returns the child count of the plan.
     */
    public int getChildCount() {
        return mChild;
    }

    /**
     * Returns the lower age group of the plan.
     */
    public double getLowerAgeGroup() {
        return mLowerAgeGroup;
    }

    /**
     * Returns the upper age group of the plan.
     */
    public int getUpperAgeGroup() {
        return mUpperAgeGroup;
    }
}
