package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.Event;
import com.bizconnectivity.gino.webservices.RetrieveEventByIdWS;

public class RetrieveEventByIdAsyncTask extends AsyncTask<String, Void, Event>{

    private final AsyncResponse asyncResponse;
    private int eventId;

    public RetrieveEventByIdAsyncTask(AsyncResponse asyncResponse, int eventId) {

        this.asyncResponse = asyncResponse;
        this.eventId = eventId;
    }

    @Override
    protected Event doInBackground(String... params) {
        return RetrieveEventByIdWS.invokeRetrieveEventById(eventId);
    }

    @Override
    protected void onPostExecute(Event result) {

        if (result != null) {
            asyncResponse.retrieveEventById(result);
        } else {
            asyncResponse.retrieveEventById(null);
        }
    }

    public interface AsyncResponse {
        void retrieveEventById(Event eventModel);
    }
}
