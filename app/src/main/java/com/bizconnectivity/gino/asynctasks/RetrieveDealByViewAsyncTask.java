package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.Deal;
import com.bizconnectivity.gino.webservices.RetrieveDealByViewWS;

import java.util.List;

public class RetrieveDealByViewAsyncTask extends AsyncTask<String, Void, List<Deal>> {

    private final AsyncResponse asyncResponse;

    public RetrieveDealByViewAsyncTask(AsyncResponse asyncResponse){
            this.asyncResponse=asyncResponse;
            }

    @Override
    protected List<Deal> doInBackground(String...params){
            return RetrieveDealByViewWS.invokeRetrieveDealByView();
    }

    @Override
    protected void onPostExecute(List<Deal> result){

        if(result.size()>0){
            asyncResponse.retrieveDealByView(result);
        }else{
            asyncResponse.retrieveDealByView(result);
        }
    }

    public interface AsyncResponse {
        void retrieveDealByView(List<Deal> dealList);
    }
}