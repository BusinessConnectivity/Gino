package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.Merchant;
import com.bizconnectivity.gino.webservices.RetrieveMerchantWS;

import java.util.List;

public class RetrieveMerchantAsyncTask extends AsyncTask<String, Void, List<Merchant>> {

    private final AsyncResponse asyncResponse;

    public RetrieveMerchantAsyncTask(AsyncResponse asyncResponse){
        this.asyncResponse = asyncResponse;
    }

    @Override
    protected List<Merchant> doInBackground(String... params) {
        return RetrieveMerchantWS.invokeRetrieveMerchant();
    }

    @Override
    protected void onPostExecute(List<Merchant> result) {

        if (result != null && result.size() > 0) {
            asyncResponse.retrieveMerchants(result);
        } else {
            asyncResponse.retrieveMerchants(null);
        }
    }

    public interface AsyncResponse {
        void retrieveMerchants(List<Merchant> merchantList);
    }
}
