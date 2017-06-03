package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.Event;
import com.bizconnectivity.gino.webservices.RetrieveEventWS;

import java.util.List;

public class RetrieveEventAsyncTask extends AsyncTask<String, Void, List<Event>>{

    private final AsyncResponse asyncResponse;
    private String latitude;
    private String longitude;
    private String kilometer;

    public RetrieveEventAsyncTask(AsyncResponse asyncResponse, String latitude, String longitude, String kilometer) {

        this.asyncResponse = asyncResponse;
        this.latitude = latitude;
        this.longitude = longitude;
        this.kilometer = kilometer;
    }

    @Override
    protected List<Event> doInBackground(String... params) {
        return RetrieveEventWS.invokeRetrieveNearbyEvent(latitude, longitude, kilometer);
    }

    @Override
    protected void onPostExecute(List<Event> result) {

        if (result != null && result.size() > 0) {
            asyncResponse.retrieveNearbyEvent(result);
        } else {
            asyncResponse.retrieveNearbyEvent(null);
        }
    }

    public interface AsyncResponse {
        void retrieveNearbyEvent(List<Event> eventList);
    }
}
