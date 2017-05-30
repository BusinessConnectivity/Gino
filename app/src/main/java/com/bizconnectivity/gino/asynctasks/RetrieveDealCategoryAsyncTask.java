package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.DealCategoryModel;
import com.bizconnectivity.gino.webservices.RetrieveDealCategoryWS;

import java.util.List;

public class RetrieveDealCategoryAsyncTask extends AsyncTask<String, Void, List<DealCategoryModel>>{

    private final AsyncResponse asyncResponse;

    public RetrieveDealCategoryAsyncTask(AsyncResponse asyncResponse) {
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected List<DealCategoryModel> doInBackground(String... params) {
        return RetrieveDealCategoryWS.invokeRetrieveDealCategory();
    }

    @Override
    protected void onPostExecute(List<DealCategoryModel> result) {

        if (result.size() > 0) {
            asyncResponse.retrieveDealCategory(result);
        } else {
            asyncResponse.retrieveDealCategory(null);
        }
    }

    public interface AsyncResponse {
        void retrieveDealCategory(List<DealCategoryModel> dealCategoryModelList);
    }
}
