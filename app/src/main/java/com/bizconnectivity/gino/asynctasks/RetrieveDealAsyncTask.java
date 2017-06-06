package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.Deal;
import com.bizconnectivity.gino.webservices.RetrieveDealWS;

import java.util.List;

public class RetrieveDealAsyncTask extends AsyncTask<String, Void, List<Deal>>{

    private AsyncResponse asyncResponse;

    public RetrieveDealAsyncTask(AsyncResponse asyncResponse) {
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected List<Deal> doInBackground(String... params) {
        return RetrieveDealWS.invokeRetrieveDeal();
    }

    @Override
    protected void onPostExecute(List<Deal> result) {

        if (result.size() > 0){
            asyncResponse.retrieveDeals(result);
        } else {
            asyncResponse.retrieveDeals(result);
        }
    }

    public interface AsyncResponse {
        void retrieveDeals(List<Deal> dealList);
    }
}
