package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.webservices.UpdateUserDealPurchaseWS;

public class UpdateUserDealPurchaseAsyncTask extends AsyncTask<String, Void, Boolean> {

    private final AsyncResponse asyncResponse;
    private int memberDealId;
    private int quantity;

    public UpdateUserDealPurchaseAsyncTask(AsyncResponse asyncResponse, int memberDealId, int quantity){

        this.asyncResponse = asyncResponse;
        this.memberDealId = memberDealId;
        this.quantity = quantity;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return UpdateUserDealPurchaseWS.invokeUpdateUserDealPurchase(memberDealId, quantity);
    }

    @Override
    protected void onPostExecute(Boolean result) {

        if (result) {
            asyncResponse.updateUserDealPurchaseResponse(true);
        } else {
            asyncResponse.updateUserDealPurchaseResponse(false);
        }
    }

    public interface AsyncResponse {
        void updateUserDealPurchaseResponse(boolean response);
    }
}
