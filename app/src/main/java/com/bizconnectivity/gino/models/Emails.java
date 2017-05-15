package com.bizconnectivity.gino.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by wendachye on 11/5/17.
 */

public class Emails {

    @SerializedName("email")
    private String email;

    @SerializedName("verified")
    private String verified;

    @SerializedName("primary")
    private  String primary;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }
}
