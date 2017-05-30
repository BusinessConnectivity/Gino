package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.webservices.UpdateDealNoOfViewWS;

public class UpdateDealNoOfViewAsyncTask extends AsyncTask<String, Void, Boolean> {

    private final AsyncResponse asyncResponse;
    private int dealId;

    public UpdateDealNoOfViewAsyncTask(AsyncResponse asyncResponse, int dealId) {

        this.asyncResponse = asyncResponse;
        this.dealId = dealId;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return UpdateDealNoOfViewWS.invokeUpdateDealNoOfView(dealId);
    }

    @Override
    protected void onPostExecute(Boolean result) {

        if (result) {
            asyncResponse.updateDealNoOfViewResponse(true, dealId);
        } else {
            asyncResponse.updateDealNoOfViewResponse(false, dealId);
        }
    }

    public interface AsyncResponse {
        void updateDealNoOfViewResponse(boolean response, int dealId);
    }
}
