package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.UserDeal;
import com.bizconnectivity.gino.webservices.RetrieveUserDealDetailWS;

public class RetrieveUserDealDetailAsyncTask extends AsyncTask<String, Void, UserDeal> {

    private final AsyncResponse asyncResponse;
    private int memberDealId;

    public RetrieveUserDealDetailAsyncTask(AsyncResponse asyncResponse, int memberDealId){

        this.asyncResponse = asyncResponse;
        this.memberDealId = memberDealId;
    }

    @Override
    protected UserDeal doInBackground(String... params) {
        return RetrieveUserDealDetailWS.invokeRetrieveUserDealDetail(memberDealId);
    }

    @Override
    protected void onPostExecute(UserDeal result) {

        if (result != null) {
            asyncResponse.retrieveAvailableDealDetail(result);
        } else {
            asyncResponse.retrieveAvailableDealDetail(null);
        }
    }

    public interface AsyncResponse {
        void retrieveAvailableDealDetail(UserDeal result);
    }
}
