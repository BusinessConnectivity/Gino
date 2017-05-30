package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.webservices.DeleteFavouriteDealWS;

public class DeleteFavouriteDealAsyncTask extends AsyncTask<String, Void, Boolean> {

    private int memberFavDealId;

    public DeleteFavouriteDealAsyncTask(int memberFavDealId) {
        this.memberFavDealId = memberFavDealId;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return DeleteFavouriteDealWS.invokeDeleteFavouriteDeal(memberFavDealId);
    }
}
