package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.Event;
import com.bizconnectivity.gino.webservices.RetrieveFavouriteEventWS;

import java.util.List;

public class RetrieveFavouriteEventAsyncTask extends AsyncTask<String, Void, List<Event>> {

    private final AsyncResponse asyncResponse;
    private int memberId;

    public RetrieveFavouriteEventAsyncTask(AsyncResponse asyncResponse, int memberId){

        this.asyncResponse = asyncResponse;
        this.memberId = memberId;
    }

    @Override
    protected List<Event> doInBackground(String... params) {
        return RetrieveFavouriteEventWS.invokeRetrieveFavouriteEvent(memberId);
    }

    @Override
    protected void onPostExecute(List<Event> result) {

        if (result != null && result.size() > 0) {
            asyncResponse.retrieveFavouriteEvent(result);
        } else {
            asyncResponse.retrieveFavouriteEvent(null);
        }
    }

    public interface AsyncResponse {
        void retrieveFavouriteEvent(List<Event> result);
    }
}
