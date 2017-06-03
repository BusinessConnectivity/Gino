package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.Deal;
import com.bizconnectivity.gino.webservices.RetrieveFavouriteDealWS;

import java.util.List;

public class RetrieveFavouriteDealAsyncTask extends AsyncTask<String, Void, List<Deal>> {

    private final AsyncResponse asyncResponse;
    private int memberId;

    public RetrieveFavouriteDealAsyncTask(AsyncResponse asyncResponse, int memberId){

        this.asyncResponse = asyncResponse;
        this.memberId = memberId;
    }

    @Override
    protected List<Deal> doInBackground(String... params) {
        return RetrieveFavouriteDealWS.invokeRetrieveFavouriteDeal(memberId);
    }

    @Override
    protected void onPostExecute(List<Deal> result) {

        if (result != null && result.size() > 0) {
            asyncResponse.retrieveFavouriteDeal(result);
        } else {
            asyncResponse.retrieveFavouriteDeal(null);
        }
    }

    public interface AsyncResponse {
        void retrieveFavouriteDeal(List<Deal> result);
    }
}
