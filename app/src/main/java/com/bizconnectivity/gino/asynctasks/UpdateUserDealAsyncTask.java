package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.webservices.UpdateUserDealWS;

public class UpdateUserDealAsyncTask extends AsyncTask<String, Void, Boolean> {

    private int memberDealId;

    public UpdateUserDealAsyncTask(int memberDealId) {
        this.memberDealId = memberDealId;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return UpdateUserDealWS.invokeUpdateUserDeal(memberDealId);
    }
}
