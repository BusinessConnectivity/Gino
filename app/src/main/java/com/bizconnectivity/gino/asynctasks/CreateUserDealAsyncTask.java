package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.webservices.CreateUserDealWS;

public class CreateUserDealAsyncTask extends AsyncTask<String, Void, Boolean>{

    private final AsyncResponse asyncResponse;
    private int userId;
    private int dealId;
    private int quantity;

    public CreateUserDealAsyncTask(AsyncResponse asyncResponse, int userId, int dealId, int quantity) {

        this.asyncResponse = asyncResponse;
        this.userId = userId;
        this.dealId = dealId;
        this.quantity = quantity;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return CreateUserDealWS.invokeCreateUserDeal(userId, dealId, quantity);
    }

    @Override
    protected void onPostExecute(Boolean result) {

        if (result) {
            asyncResponse.createUserDealResponse(true);
        } else {
            asyncResponse.createUserDealResponse(false);
        }
    }

    public interface AsyncResponse {
        void createUserDealResponse(boolean response);
    }
}
