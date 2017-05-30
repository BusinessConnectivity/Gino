package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.webservices.CheckUserLoginWS;

public class CheckUserLoginAsyncTask extends AsyncTask<String, Void, Boolean> {

    private final AsyncResponse asyncResponse;
    private String email;
    private String password;

    public CheckUserLoginAsyncTask(AsyncResponse asyncResponse, String email, String password) {
        this.asyncResponse = asyncResponse;
        this.email = email;
        this.password = password;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return CheckUserLoginWS.invokeCheckUserLogin(email, password);
    }

    @Override
    protected void onPostExecute(Boolean result) {

        if (result) {
            asyncResponse.checkUserLoginRespond(true);
        } else {
            asyncResponse.checkUserLoginRespond(false);
        }
    }

    public interface AsyncResponse {
        void checkUserLoginRespond(boolean response);
    }
}
