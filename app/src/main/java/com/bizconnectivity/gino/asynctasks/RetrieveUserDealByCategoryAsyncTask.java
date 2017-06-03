package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.Deal;
import com.bizconnectivity.gino.webservices.RetrieveUserDealByCategoryWS;

import java.util.List;

public class RetrieveUserDealByCategoryAsyncTask extends AsyncTask<String, Void, List<Deal>> {

    private final AsyncResponse asyncResponse;
    private int memberId;
    private int dealCategoryId;

    public RetrieveUserDealByCategoryAsyncTask(AsyncResponse asyncResponse, int memberId, int dealCategoryId) {
        this.asyncResponse = asyncResponse;
        this.memberId = memberId;
        this.dealCategoryId = dealCategoryId;
    }

    @Override
    protected List<Deal> doInBackground(String... params) {
        return RetrieveUserDealByCategoryWS.invokeRetrieveUserDealByCategory(memberId, dealCategoryId);
    }

    @Override
    protected void onPostExecute(List<Deal> result) {

        if (result.size() > 0){
            asyncResponse.retrieveUserDealByCategory(result);
        } else {
            asyncResponse.retrieveUserDealByCategory(result);
        }
    }

    public interface AsyncResponse {
        void retrieveUserDealByCategory(List<Deal> dealList);
    }
}
