package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.DealModel;
import com.bizconnectivity.gino.webservices.RetrieveDealWS;

import java.util.List;

public class RetrieveDealAsyncTask extends AsyncTask<String, Void, List<DealModel>>{

    private final AsyncResponse asyncResponse;


    public RetrieveDealAsyncTask(AsyncResponse asyncResponse) {
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected List<DealModel> doInBackground(String... params) {
        return RetrieveDealWS.invokeRetrieveDeal();
    }

    @Override
    protected void onPostExecute(List<DealModel> result) {

        if (result.size() > 0){
            asyncResponse.retrieveDeal(result);
        } else {
            asyncResponse.retrieveDeal(result);
        }
    }

    public interface AsyncResponse {
        void retrieveDeal(List<DealModel> dealModelList);
    }
}
