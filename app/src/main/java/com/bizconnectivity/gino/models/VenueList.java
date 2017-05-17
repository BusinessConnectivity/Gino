package com.bizconnectivity.gino.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VenueList {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("address")
    @Expose
    private AddressList address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressList getAddress() {
        return address;
    }

    public void setAddress(AddressList address) {
        this.address = address;
    }
}
