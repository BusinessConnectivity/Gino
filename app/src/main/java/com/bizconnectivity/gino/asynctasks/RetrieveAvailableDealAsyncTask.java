package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.PurchasedDeal;
import com.bizconnectivity.gino.webservices.RetrieveAvailableDealWS;

import java.util.List;

public class RetrieveAvailableDealAsyncTask extends AsyncTask<String, Void, List<PurchasedDeal>> {

    private final AsyncResponse asyncResponse;
    private int memberId;

    public RetrieveAvailableDealAsyncTask(AsyncResponse asyncResponse, int memberId){

        this.asyncResponse = asyncResponse;
        this.memberId = memberId;
    }

    @Override
    protected List<PurchasedDeal> doInBackground(String... params) {
        return RetrieveAvailableDealWS.invokeRetrieveAvailableDeal(memberId);
    }

    @Override
    protected void onPostExecute(List<PurchasedDeal> result) {

        if (result != null && result.size() > 0) {
            asyncResponse.retrieveAvailableDeal(result);
        } else {
            asyncResponse.retrieveAvailableDeal(null);
        }
    }

    public interface AsyncResponse {
        void retrieveAvailableDeal(List<PurchasedDeal> userDealModelList);
    }
}
