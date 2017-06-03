package com.bizconnectivity.gino.asynctasks;


import android.os.AsyncTask;

import com.bizconnectivity.gino.models.Deal;
import com.bizconnectivity.gino.webservices.RetrieveDealByDateWS;

import java.util.List;

public class RetrieveDealByDateAsyncTask extends AsyncTask<String, Void, List<Deal>> {

    private final AsyncResponse asyncResponse;

    public RetrieveDealByDateAsyncTask(AsyncResponse asyncResponse) {
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected List<Deal> doInBackground(String... params) {
        return RetrieveDealByDateWS.invokeRetrieveDealByDate();
    }

    @Override
    protected void onPostExecute(List<Deal> result) {

        if (result.size() > 0){
            asyncResponse.retrieveDealByDate(result);
        } else {
            asyncResponse.retrieveDealByDate(result);
        }
    }

    public interface AsyncResponse {
        void retrieveDealByDate(List<Deal> dealList);
    }
}
