package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.webservices.CreateFavouriteDealWS;

public class CreateFavouriteDealAsyncTask extends AsyncTask<String, Void, Boolean> {

    private int memberId;
    private int dealId;

    public CreateFavouriteDealAsyncTask(int memberId, int dealId) {
        this.memberId = memberId;
        this.dealId = dealId;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return CreateFavouriteDealWS.invokeCreateFavouriteDeal(memberId, dealId);
    }
}
