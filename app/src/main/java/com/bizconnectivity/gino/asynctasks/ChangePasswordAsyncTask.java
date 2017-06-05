package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.webservices.ChangePasswordWS;

public class ChangePasswordAsyncTask extends AsyncTask<String, Void, Boolean>{

    private AsyncResponse asyncResponse;
    private int memberId;
    private String password;

    public ChangePasswordAsyncTask(AsyncResponse asyncResponse, int memberId, String password) {
        this.asyncResponse = asyncResponse;
        this.memberId = memberId;
        this.password = password;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return ChangePasswordWS.invokeChangePassword(memberId, password);
    }

    @Override
    protected void onPostExecute(Boolean result) {

        if (result) {
            asyncResponse.changePasswordRespond(true);
        } else {
            asyncResponse.changePasswordRespond(false);
        }
    }

    public interface AsyncResponse {
        void changePasswordRespond(boolean response);
    }
}
