package com.bizconnectivity.gino.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by wendachye on 12/5/17.
 */

public class EndDateList {

    @SerializedName("local")
    @Expose
    private Date local;

    public Date getLocal() {
        return local;
    }

    public void setLocal(Date local) {
        this.local = local;
    }
}
