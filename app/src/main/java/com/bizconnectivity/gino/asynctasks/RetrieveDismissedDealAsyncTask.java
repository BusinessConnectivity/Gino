package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.Deal;
import com.bizconnectivity.gino.webservices.RetrieveDismissedDealWS;

import java.util.List;

public class RetrieveDismissedDealAsyncTask extends AsyncTask<String, Void, List<Deal>> {

    private final AsyncResponse asyncResponse;
    private int memberId;

    public RetrieveDismissedDealAsyncTask(AsyncResponse asyncResponse, int memberId){

        this.asyncResponse = asyncResponse;
        this.memberId = memberId;
    }

    @Override
    protected List<Deal> doInBackground(String... params) {
        return RetrieveDismissedDealWS.invokeRetrieveDismissedDeal(memberId);
    }

    @Override
    protected void onPostExecute(List<Deal> result) {

        if (result != null && result.size() > 0) {
            asyncResponse.retrieveDismissedDeal(result);
        } else {
            asyncResponse.retrieveDismissedDeal(null);
        }
    }

    public interface AsyncResponse {
        void retrieveDismissedDeal(List<Deal> result);
    }
}
