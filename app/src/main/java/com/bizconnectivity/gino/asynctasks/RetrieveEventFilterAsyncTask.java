package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.bizconnectivity.gino.models.Event;
import com.bizconnectivity.gino.webservices.RetrieveEventFilterWS;

import java.util.List;

public class RetrieveEventFilterAsyncTask extends AsyncTask<String, Void, List<Event>>{

    private final AsyncResponse asyncResponse;
    private String latitude;
    private String longitude;
    private String kilometer;
    private String startDate;
    private String endDate;
    private String name;

    public RetrieveEventFilterAsyncTask(AsyncResponse asyncResponse, String latitude, String longitude, String kilometer,
                                        String startDate, String endDate, String name) {

        this.asyncResponse = asyncResponse;
        this.latitude = latitude;
        this.longitude = longitude;
        this.kilometer = kilometer;
        this.startDate = startDate;
        this.endDate = endDate;
        this.name = name;
    }

    @Override
    protected List<Event> doInBackground(String... params) {

        return RetrieveEventFilterWS.invokeRetrieveNearbyEventFilter(latitude, longitude, kilometer, startDate,
                endDate, name);
    }

    @Override
    protected void onPostExecute(List<Event> result) {

        if (result != null && result.size() > 0) {
            asyncResponse.retrieveNearbyEventFilter(result);
        } else {
            asyncResponse.retrieveNearbyEventFilter(null);
        }
    }

    public interface AsyncResponse {
        void retrieveNearbyEventFilter(List<Event> eventList);
    }
}
