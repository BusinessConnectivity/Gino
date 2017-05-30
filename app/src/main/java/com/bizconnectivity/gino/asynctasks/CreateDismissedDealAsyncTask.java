package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.webservices.CreateDismissedDealWS;

public class CreateDismissedDealAsyncTask extends AsyncTask<String, Void, Boolean> {

    private int memberId;
    private int dealId;

    public CreateDismissedDealAsyncTask(int memberId, int dealId) {
        this.memberId = memberId;
        this.dealId = dealId;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return CreateDismissedDealWS.invokeCreateDismissedDeal(memberId, dealId);
    }
}
