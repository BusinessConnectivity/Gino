package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.webservices.UpdateUserWS;

public class UpdateUserAsyncTask extends AsyncTask<String, Void, Boolean>{

    private final AsyncResponse asyncResponse;
    private String email;
    private String name;
    private String gender;
    private String dob;

    public UpdateUserAsyncTask(AsyncResponse asyncResponse, String email, String name, String gender, String dob) {

        this.asyncResponse = asyncResponse;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.dob = dob;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return UpdateUserWS.invokeUpdateUser(email, name, gender, dob);
    }

    @Override
    protected void onPostExecute(Boolean result) {

        if (result) {
            asyncResponse.updateUserResponse(true);
        } else {
            asyncResponse.updateUserResponse(false);
        }
    }

    public interface AsyncResponse {
        void updateUserResponse(boolean response);
    }
}
