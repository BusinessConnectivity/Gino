package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.webservices.CheckUserWS;

public class CheckUserEmailAsyncTask extends AsyncTask<String, Void, Boolean> {

    private final AsyncResponse asyncResponse;
    private String email;

    public CheckUserEmailAsyncTask(AsyncResponse asyncResponse, String email) {
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
            asyncResponse.checkUserEmailRespond(true);
        } else {
            asyncResponse.checkUserEmailRespond(false);
        }
    }

    public interface AsyncResponse {
        void checkUserEmailRespond(boolean response);
    }
}
