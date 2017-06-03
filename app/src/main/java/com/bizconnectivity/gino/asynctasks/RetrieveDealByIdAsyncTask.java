package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.Deal;
import com.bizconnectivity.gino.webservices.RetrieveDealByIdWS;

public class RetrieveDealByIdAsyncTask extends AsyncTask<String, Void, Deal>{

    private final AsyncResponse asyncResponse;
    private int dealId;

    public RetrieveDealByIdAsyncTask(AsyncResponse asyncResponse, int dealId) {

        this.asyncResponse = asyncResponse;
        this.dealId = dealId;
    }

    @Override
    protected Deal doInBackground(String... params) {
        return RetrieveDealByIdWS.invokeRetrieveDealById(dealId);
    }

    @Override
    protected void onPostExecute(Deal result) {

        if (result != null) {
            asyncResponse.retrieveDealById(result);
        } else {
            asyncResponse.retrieveDealById(null);
        }
    }

    public interface AsyncResponse {
        void retrieveDealById(Deal deal);
    }
}
