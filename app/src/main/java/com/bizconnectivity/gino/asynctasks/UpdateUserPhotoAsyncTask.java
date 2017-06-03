package com.bizconnectivity.gino.asynctasks;

import android.os.AsyncTask;

import com.bizconnectivity.gino.webservices.UpdateUserPhotoWS;

public class UpdateUserPhotoAsyncTask extends AsyncTask<String, Void, Boolean>{

    private int memberId;
    private byte[] photoFile;
    private String photoName;
    private String photoExt;

    public UpdateUserPhotoAsyncTask(int memberId, byte[] photoFile, String photoName, String photoExt) {
        this.memberId = memberId;
        this.photoFile = photoFile;
        this.photoName = photoName;
        this.photoExt = photoExt;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        return UpdateUserPhotoWS.invokeUpdateUserPhoto(memberId, photoFile, photoName, photoExt);
    }
}
