package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.Merchant;
import com.bizconnectivity.gino.webservices.RetrieveMerchantByIDWS;

public class RetrieveMerchantByIdAsyncTask extends AsyncTask<String, Void, Merchant>{

    private final AsyncResponse asyncResponse;
    private int merchantId;

    public RetrieveMerchantByIdAsyncTask(AsyncResponse asyncResponse, int merchantId) {

        this.asyncResponse = asyncResponse;
        this.merchantId = merchantId;
    }

    @Override
    protected Merchant doInBackground(String... params) {
        return RetrieveMerchantByIDWS.invokeRetrieveMerchantByID(merchantId);
    }

    @Override
    protected void onPostExecute(Merchant result) {

        if (result != null) {
            asyncResponse.retrieveMerchant(result);
        } else {
            asyncResponse.retrieveMerchant(null);
        }
    }

    public interface AsyncResponse {
        void retrieveMerchant(Merchant merchantModel);
    }
}
