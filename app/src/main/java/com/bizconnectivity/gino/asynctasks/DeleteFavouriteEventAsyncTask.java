package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.webservices.DeleteFavouriteEventWS;

public class DeleteFavouriteEventAsyncTask extends AsyncTask<String, Void, Boolean> {

    private int memberFavEventId;

    public DeleteFavouriteEventAsyncTask(int memberFavEventId) {
        this.memberFavEventId = memberFavEventId;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return DeleteFavouriteEventWS.invokeDeleteFavouriteEvent(memberFavEventId);
    }
}
