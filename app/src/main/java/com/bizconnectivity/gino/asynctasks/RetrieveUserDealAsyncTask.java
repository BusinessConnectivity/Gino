package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.Deal;
import com.bizconnectivity.gino.webservices.RetrieveUserDealWS;

import java.util.List;

public class RetrieveUserDealAsyncTask extends AsyncTask<String, Void, List<Deal>> {

    private final AsyncResponse asyncResponse;
    private int memberId;

    public RetrieveUserDealAsyncTask(AsyncResponse asyncResponse, int memberId) {
        this.asyncResponse = asyncResponse;
        this.memberId = memberId;
    }

    @Override
    protected List<Deal> doInBackground(String... params) {
        return RetrieveUserDealWS.invokeRetrieveUserDeal(memberId);
    }

    @Override
    protected void onPostExecute(List<Deal> result) {

        if (result.size() > 0){
            asyncResponse.retrieveUserDeal(result);
        } else {
            asyncResponse.retrieveUserDeal(result);
        }
    }

    public interface AsyncResponse {
        void retrieveUserDeal(List<Deal> dealList);
    }
}
