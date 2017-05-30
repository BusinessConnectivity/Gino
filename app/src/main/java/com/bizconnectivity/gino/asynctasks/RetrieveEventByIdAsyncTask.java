package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.EventModel;
import com.bizconnectivity.gino.webservices.RetrieveEventByIdWS;

public class RetrieveEventByIdAsyncTask extends AsyncTask<String, Void, EventModel>{

    private final AsyncResponse asyncResponse;
    private int eventId;

    public RetrieveEventByIdAsyncTask(AsyncResponse asyncResponse, int eventId) {

        this.asyncResponse = asyncResponse;
        this.eventId = eventId;
    }

    @Override
    protected EventModel doInBackground(String... params) {
        return RetrieveEventByIdWS.invokeRetrieveEventById(eventId);
    }

    @Override
    protected void onPostExecute(EventModel result) {

        if (result != null) {
            asyncResponse.retrieveEventById(result);
        } else {
            asyncResponse.retrieveEventById(null);
        }
    }

    public interface AsyncResponse {
        void retrieveEventById(EventModel eventModel);
    }
}
