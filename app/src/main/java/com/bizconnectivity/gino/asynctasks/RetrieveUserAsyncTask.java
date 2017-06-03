package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.models.User;
import com.bizconnectivity.gino.webservices.RetrieveUserWS;

public class RetrieveUserAsyncTask extends AsyncTask<String, Void, User>{

    private final AsyncResponse asyncResponse;
    private String email;

    public RetrieveUserAsyncTask(AsyncResponse asyncResponse, String email) {
        this.asyncResponse = asyncResponse;
        this.email = email;
    }

    @Override
    protected User doInBackground(String... params) {
        return RetrieveUserWS.invokeRetrieveUser(email);
    }

    @Override
    protected void onPostExecute(User user) {

        if (user != null) {
            asyncResponse.retrieveUserDetail(user);
        } else {
            asyncResponse.retrieveUserDetail(null);
        }
    }

    public interface AsyncResponse {
        void retrieveUserDetail(User user);
    }
}
