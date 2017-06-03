package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.DealCategory;
import com.bizconnectivity.gino.webservices.RetrieveDealCategoryWS;

import java.util.List;

public class RetrieveDealCategoryAsyncTask extends AsyncTask<String, Void, List<DealCategory>>{

    private final AsyncResponse asyncResponse;

    public RetrieveDealCategoryAsyncTask(AsyncResponse asyncResponse) {
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected List<DealCategory> doInBackground(String... params) {
        return RetrieveDealCategoryWS.invokeRetrieveDealCategory();
    }

    @Override
    protected void onPostExecute(List<DealCategory> result) {

        if (result.size() > 0) {
            asyncResponse.retrieveDealCategory(result);
        } else {
            asyncResponse.retrieveDealCategory(null);
        }
    }

    public interface AsyncResponse {
        void retrieveDealCategory(List<DealCategory> dealCategoryModelList);
    }
}
