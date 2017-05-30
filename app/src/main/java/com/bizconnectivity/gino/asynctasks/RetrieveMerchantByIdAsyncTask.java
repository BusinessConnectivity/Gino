package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.MerchantModel;
import com.bizconnectivity.gino.webservices.RetrieveMerchantByIDWS;

public class RetrieveMerchantByIdAsyncTask extends AsyncTask<String, Void, MerchantModel>{

    private final AsyncResponse asyncResponse;
    private int merchantId;

    public RetrieveMerchantByIdAsyncTask(AsyncResponse asyncResponse, int merchantId) {

        this.asyncResponse = asyncResponse;
        this.merchantId = merchantId;
    }

    @Override
    protected MerchantModel doInBackground(String... params) {
        return RetrieveMerchantByIDWS.invokeRetrieveMerchantByID(merchantId);
    }

    @Override
    protected void onPostExecute(MerchantModel result) {

        if (result != null) {
            asyncResponse.retrieveMerchant(result);
        } else {
            asyncResponse.retrieveMerchant(null);
        }
    }

    public interface AsyncResponse {
        void retrieveMerchant(MerchantModel merchantModel);
    }
}
