package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.Deal;
import com.bizconnectivity.gino.webservices.RetrieveUserDealByDateWS;

import java.util.List;

public class RetrieveUserDealByDateAsyncTask extends AsyncTask<String, Void, List<Deal>> {

    private final AsyncResponse asyncResponse;
    private int memberId;

    public RetrieveUserDealByDateAsyncTask(AsyncResponse asyncResponse, int memberId) {
        this.asyncResponse = asyncResponse;
        this.memberId = memberId;
    }

    @Override
    protected List<Deal> doInBackground(String... params) {
        return RetrieveUserDealByDateWS.invokeRetrieveUserDealByDate(memberId);
    }

    @Override
    protected void onPostExecute(List<Deal> result) {

        if (result.size() > 0){
            asyncResponse.retrieveUserDealByDate(result);
        } else {
            asyncResponse.retrieveUserDealByDate(result);
        }
    }

    public interface AsyncResponse {
        void retrieveUserDealByDate(List<Deal> dealList);
    }
}
