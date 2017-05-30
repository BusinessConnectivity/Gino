package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.FavDealModel;
import com.bizconnectivity.gino.webservices.RetrieveFavouriteDealWS;

import java.util.List;

public class RetrieveFavouriteDealAsyncTask extends AsyncTask<String, Void, List<FavDealModel>> {

    private final AsyncResponse asyncResponse;
    private int memberId;

    public RetrieveFavouriteDealAsyncTask(AsyncResponse asyncResponse, int memberId){

        this.asyncResponse = asyncResponse;
        this.memberId = memberId;
    }

    @Override
    protected List<FavDealModel> doInBackground(String... params) {
        return RetrieveFavouriteDealWS.invokeRetrieveFavouriteDeal(memberId);
    }

    @Override
    protected void onPostExecute(List<FavDealModel> result) {

        if (result != null && result.size() > 0) {
            asyncResponse.retrieveFavouriteDeal(result);
        } else {
            asyncResponse.retrieveFavouriteDeal(null);
        }
    }

    public interface AsyncResponse {
        void retrieveFavouriteDeal(List<FavDealModel> favDealList);
    }
}
