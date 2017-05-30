package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.DismissedDealModel;
import com.bizconnectivity.gino.webservices.RetrieveDismissedDealWS;

import java.util.List;

public class RetrieveDismissedDealAsyncTask extends AsyncTask<String, Void, List<DismissedDealModel>> {

    private final AsyncResponse asyncResponse;
    private int memberId;

    public RetrieveDismissedDealAsyncTask(AsyncResponse asyncResponse, int memberId){

        this.asyncResponse = asyncResponse;
        this.memberId = memberId;
    }

    @Override
    protected List<DismissedDealModel> doInBackground(String... params) {
        return RetrieveDismissedDealWS.invokeRetrieveFavouriteDeal(memberId);
    }

    @Override
    protected void onPostExecute(List<DismissedDealModel> result) {

        if (result != null && result.size() > 0) {
            asyncResponse.retrieveDismissedDeal(result);
        } else {
            asyncResponse.retrieveDismissedDeal(null);
        }
    }

    public interface AsyncResponse {
        void retrieveDismissedDeal(List<DismissedDealModel> dismissedDealList);
    }
}
