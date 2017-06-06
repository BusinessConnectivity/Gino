package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.Deal;
import com.bizconnectivity.gino.webservices.RetrieveSearchDealWS;

import java.util.List;

public class SearchDealAsyncTask extends AsyncTask<String, Void, List<Deal>> {

    private AsyncResponse asyncResponse;
    private String query;

    public SearchDealAsyncTask(AsyncResponse asyncResponse, String query) {
        this.asyncResponse = asyncResponse;
        this.query = query;
    }

    @Override
    protected List<Deal> doInBackground(String... params) {
        return RetrieveSearchDealWS.invokeRetrieveSearchDeal(query);
    }

    @Override
    protected void onPostExecute(List<Deal> result) {

        if (result.size() > 0){
            asyncResponse.retrieveSearchDeal(result);
        } else {
            asyncResponse.retrieveSearchDeal(result);
        }
    }

    public interface AsyncResponse {
        void retrieveSearchDeal(List<Deal> result);
    }
}
