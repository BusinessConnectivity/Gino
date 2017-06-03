package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.webservices.DeleteDismissedDealWS;

public class DeleteDismissedDealAsyncTask extends AsyncTask<String, Void, Boolean> {

    private int memberFavDealId;

    public DeleteDismissedDealAsyncTask(int memberFavDealId) {
        this.memberFavDealId = memberFavDealId;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return DeleteDismissedDealWS.invokeDeleteDismissedDeal(memberFavDealId);
    }
}
