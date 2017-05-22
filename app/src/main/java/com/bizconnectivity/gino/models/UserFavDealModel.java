package com.bizconnectivity.gino.models;

import io.realm.RealmObject;

public class UserFavDealModel extends RealmObject{

    private int userFavDealID;
    private int userID;
    private int dealID;

    public int getUserFavDealID() {
        return userFavDealID;
    }

    public void setUserFavDealID(int userFavDealID) {
        this.userFavDealID = userFavDealID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getDealID() {
        return dealID;
    }

    public void setDealID(int dealID) {
        this.dealID = dealID;
    }
}
