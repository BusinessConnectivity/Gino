package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.webservices.UpdateDealNoOfViewWS;

public class UpdateDealNoOfViewAsyncTask extends AsyncTask<String, Void, Boolean> {

    private int dealId;

    public UpdateDealNoOfViewAsyncTask(int dealId) {
        this.dealId = dealId;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return UpdateDealNoOfViewWS.invokeUpdateDealNoOfView(dealId);
    }
}
