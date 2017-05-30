package com.bizconnectivity.gino.models;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class DismissedDealModel extends RealmObject{

    @PrimaryKey
    @Index
    private int userDismissedDealID;
    private int userID;
    private int dealID;

    public int getUserDismissedDealID() {
        return userDismissedDealID;
    }

    public void setUserDismissedDealID(int userDismissedDealID) {
        this.userDismissedDealID = userDismissedDealID;
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
