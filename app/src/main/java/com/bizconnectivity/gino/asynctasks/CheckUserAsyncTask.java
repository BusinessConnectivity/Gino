package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.webservices.CheckUserWS;

public class CheckUserAsyncTask extends AsyncTask<String, Void, Boolean>{

    private final AsyncResponse asyncResponse;
    private String email;

    public CheckUserAsyncTask(AsyncResponse asyncResponse, String email) {
        this.asyncResponse = asyncResponse;
        this.email = email;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return CheckUserWS.invokeCheckUser(email);
    }

    @Override
    protected void onPostExecute(Boolean result) {

        if (result) {
            asyncResponse.checkUserRespond(true);
        } else {
            asyncResponse.checkUserRespond(false);
        }
    }

    public interface AsyncResponse {
        void checkUserRespond(boolean response);
    }
}
