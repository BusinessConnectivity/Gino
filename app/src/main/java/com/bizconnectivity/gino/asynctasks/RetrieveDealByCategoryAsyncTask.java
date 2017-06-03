package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.Deal;
import com.bizconnectivity.gino.webservices.RetrieveDealByCategoryWS;

import java.util.List;

public class RetrieveDealByCategoryAsyncTask extends AsyncTask<String, Void, List<Deal>> {

    private final AsyncResponse asyncResponse;
    private int memberId;

    public RetrieveDealByCategoryAsyncTask(AsyncResponse asyncResponse, int memberId) {
        this.asyncResponse = asyncResponse;
        this.memberId = memberId;
    }

    @Override
    protected List<Deal> doInBackground(String... params) {
        return RetrieveDealByCategoryWS.invokeRetrieveDealByCategory(memberId);
    }

    @Override
    protected void onPostExecute(List<Deal> result) {

        if (result.size() > 0){
            asyncResponse.retrieveDealByCategory(result);
        } else {
            asyncResponse.retrieveDealByCategory(result);
        }
    }

    public interface AsyncResponse {
        void retrieveDealByCategory(List<Deal> dealList);
    }
}
