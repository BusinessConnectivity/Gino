package com.bizconnectivity.gino.models;

import io.realm.RealmObject;

public class FirstTimeInstall extends RealmObject{

    private String isFirstTime;

    public String getIsFirstTime() {
        return isFirstTime;
    }

    public void setIsFirstTime(String isFirstTime) {
        this.isFirstTime = isFirstTime;
    }
}
