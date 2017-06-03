package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.PurchasedDeal;
import com.bizconnectivity.gino.webservices.RetrieveHistoryDealWS;

import java.util.List;

public class RetrieveHistoryDealAsyncTask extends AsyncTask<String, Void, List<PurchasedDeal>> {

    private final AsyncResponse asyncResponse;
    private int memberId;

    public RetrieveHistoryDealAsyncTask(AsyncResponse asyncResponse, int memberId){

        this.asyncResponse = asyncResponse;
        this.memberId = memberId;
    }

    @Override
    protected List<PurchasedDeal> doInBackground(String... params) {
        return RetrieveHistoryDealWS.invokeRetrieveHistoryDeal(memberId);
    }

    @Override
    protected void onPostExecute(List<PurchasedDeal> result) {

        if (result != null && result.size() > 0) {
            asyncResponse.retrieveHistoryDeal(result);
        } else {
            asyncResponse.retrieveHistoryDeal(null);
        }
    }

    public interface AsyncResponse {
        void retrieveHistoryDeal(List<PurchasedDeal> result);
    }
}
