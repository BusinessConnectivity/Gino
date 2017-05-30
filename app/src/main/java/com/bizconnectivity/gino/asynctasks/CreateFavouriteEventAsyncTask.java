package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.webservices.CreateFavouriteEventWS;

public class CreateFavouriteEventAsyncTask extends AsyncTask<String, Void, Boolean> {

    private int memberId;
    private int eventId;

    public CreateFavouriteEventAsyncTask(int memberId, int eventId) {
        this.memberId = memberId;
        this.eventId = eventId;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return CreateFavouriteEventWS.invokeCreateFavouriteEvent(memberId, eventId);
    }
}
