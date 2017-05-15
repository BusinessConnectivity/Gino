package com.bizconnectivity.gino.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by wendachye on 11/5/17.
 */

public class AddressList {

    @SerializedName("address_1")
    @Expose
    private String address_1;

    @SerializedName("address_2")
    @Expose
    private String address_2;

    @SerializedName("city")
    @Expose
    private String city;

    @SerializedName("postal_code")
    @Expose
    private String postal_code;

    public String getAddress_1() {
        return address_1;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public String getAddress_2() {
        return address_2;
    }

    public void setAddress_2(String address_2) {
        this.address_2 = address_2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }
}
