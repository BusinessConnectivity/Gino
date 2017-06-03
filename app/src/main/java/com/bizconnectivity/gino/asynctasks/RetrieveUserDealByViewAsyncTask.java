package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.Deal;
import com.bizconnectivity.gino.webservices.RetrieveUserDealByViewWS;

import java.util.List;

public class RetrieveUserDealByViewAsyncTask extends AsyncTask<String, Void, List<Deal>> {

    private final AsyncResponse asyncResponse;
    private int memberId;

    public RetrieveUserDealByViewAsyncTask(AsyncResponse asyncResponse, int memberId) {
        this.asyncResponse = asyncResponse;
        this.memberId = memberId;
    }

    @Override
    protected List<Deal> doInBackground(String... params) {
        return RetrieveUserDealByViewWS.invokeRetrieveUserDealByView(memberId);
    }

    @Override
    protected void onPostExecute(List<Deal> result) {

        if (result.size() > 0){
            asyncResponse.retrieveUserDealByView(result);
        } else {
            asyncResponse.retrieveUserDealByView(result);
        }
    }

    public interface AsyncResponse {
        void retrieveUserDealByView(List<Deal> dealList);
    }
}
