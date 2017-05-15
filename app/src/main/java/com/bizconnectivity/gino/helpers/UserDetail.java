package com.bizconnectivity.gino.helpers;

import com.bizconnectivity.gino.models.Emails;
import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserDetail {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("first_name")
    private String first_name;

    @SerializedName("last_name")
    private String last_name;

    @SerializedName("emails")
    private List<Emails> emails;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public List<Emails> getEmails() {
        return emails;
    }

    public void setEmails(List<Emails> emails) {
        this.emails = emails;
    }
}
