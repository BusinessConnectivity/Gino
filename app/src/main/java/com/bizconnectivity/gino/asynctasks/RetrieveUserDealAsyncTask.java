package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.UserDealModel;
import com.bizconnectivity.gino.webservices.RetrieveUserDealWS;

import java.util.List;

public class RetrieveUserDealAsyncTask extends AsyncTask<String, Void, List<UserDealModel>> {

    private final AsyncResponse asyncResponse;
    private int memberId;

    public RetrieveUserDealAsyncTask(AsyncResponse asyncResponse, int memberId){

        this.asyncResponse = asyncResponse;
        this.memberId = memberId;
    }

    @Override
    protected List<UserDealModel> doInBackground(String... params) {
        return RetrieveUserDealWS.invokeRetrieveUserDeal(memberId);
    }

    @Override
    protected void onPostExecute(List<UserDealModel> result) {

        if (result != null && result.size() > 0) {
            asyncResponse.retrieveUserDeal(result);
        } else {
            asyncResponse.retrieveUserDeal(null);
        }
    }

    public interface AsyncResponse {
        void retrieveUserDeal(List<UserDealModel> userDealModelList);
    }
}
