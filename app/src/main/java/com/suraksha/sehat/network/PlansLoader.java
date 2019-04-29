package com.suraksha.sehat.network;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.suraksha.sehat.model.Plans;
import com.suraksha.sehat.utils.Url;

import java.util.List;

/**
 * Loads a list of plans by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class PlansLoader extends AsyncTaskLoader<List<Plans>> {

    Context mContext;

    String mSizeId, mSumId, mAge;

    /** Tag for log messages */
    private static final String LOG_TAG = PlansLoader.class.getName();

    /**
     * Constructs a new {@link PlansLoader}.
     *
     * @param context of the activity
     */
    public PlansLoader(Context context, String sizeId, String sumId, String age) {
        super(context);
        mContext = context;
        mSizeId = sizeId;
        mSumId = sumId;
        mAge = age;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Plans> loadInBackground() {

        // Perform the network request, parse the response, and extract a list of plans.
        List<Plans> plans = QueryUtils.fetchPlansData(mContext, mSizeId, mSumId, mAge, Url.BASE_URL + Url.PLANS_LIST);
        return plans;
    }
}
