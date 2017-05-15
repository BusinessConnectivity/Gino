package com.bizconnectivity.gino.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by wendachye on 11/5/17.
 */

public class CategoryList {

    @SerializedName("name")
    @Expose
    private String name;

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
