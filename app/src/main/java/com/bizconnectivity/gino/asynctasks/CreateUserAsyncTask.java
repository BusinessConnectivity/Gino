package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.webservices.CreateUserWS;

public class CreateUserAsyncTask extends AsyncTask<String, Void, Boolean> {

    private final AsyncResponse asyncResponse;
    private String name;
    private String password;
    private String email;
    private String gender;
    private String dob;
    private String facebookId;
    private String googleId;
    private String photoUrl;

    public CreateUserAsyncTask(AsyncResponse asyncResponse, String name, String password, String email, String gender,
                               String dob, String facebookId, String googleId, String photoUrl) {

        this.asyncResponse = asyncResponse;
        this.name = name;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.dob = dob;
        this.facebookId = facebookId;
        this.googleId = googleId;
        this.photoUrl = photoUrl;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return CreateUserWS.invokeCreateUser(name, password, email, gender, dob, facebookId, googleId, photoUrl);
    }

    @Override
    protected void onPostExecute(Boolean result) {

        if (result) {
            asyncResponse.createUserResponse(true);
        } else {
            asyncResponse.createUserResponse(false);
        }
    }

    public interface AsyncResponse {
        void createUserResponse(boolean response);
    }
}
