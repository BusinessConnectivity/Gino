package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.Event;
import com.bizconnectivity.gino.webservices.CheckFavouriteEventWS;

public class CheckFavouriteEventAsyncTask extends AsyncTask<String, Void, Event> {

    private final AsyncResponse asyncResponse;
    private int memberId;
    private int eventId;

    public CheckFavouriteEventAsyncTask(AsyncResponse asyncResponse, int memberId, int eventId) {
        this.asyncResponse = asyncResponse;
        this.memberId = memberId;
        this.eventId = eventId;
    }

    @Override
    protected Event doInBackground(String... params) {
        return CheckFavouriteEventWS.invokeCheckFavouriteEvent(memberId, eventId);
    }

    @Override
    protected void onPostExecute(Event result) {

        if (result != null) {
            asyncResponse.checkFavouriteEventRespond(result);
        } else {
            asyncResponse.checkFavouriteEventRespond(null);
        }
    }

    public interface AsyncResponse {
        void checkFavouriteEventRespond(Event response);
    }
}
