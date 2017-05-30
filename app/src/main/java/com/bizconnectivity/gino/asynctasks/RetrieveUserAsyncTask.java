package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.UserModel;
import com.bizconnectivity.gino.webservices.RetrieveUserWS;

public class RetrieveUserAsyncTask extends AsyncTask<String, Void, UserModel>{

    private final AsyncResponse asyncResponse;
    private String email;

    public RetrieveUserAsyncTask(AsyncResponse asyncResponse, String email) {
        this.asyncResponse = asyncResponse;
        this.email = email;
    }

    @Override
    protected UserModel doInBackground(String... params) {
        return RetrieveUserWS.invokeRetrieveUser(email);
    }

    @Override
    protected void onPostExecute(UserModel userModel) {

        if (userModel != null) {
            asyncResponse.retrieveUserDetail(userModel);
        } else {
            asyncResponse.retrieveUserDetail(null);
        }
    }

    public interface AsyncResponse {
        void retrieveUserDetail(UserModel userModel);
    }
}
