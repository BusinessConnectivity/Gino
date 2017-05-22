package com.bizconnectivity.gino.models;

import io.realm.RealmObject;

public class UserDealModel extends RealmObject{

    private int userDealID;
    private int userID;
    private int dealID;
    private String redeemedDate;
    private boolean isRedeemed;
    private boolean isExpired;
    private boolean isDeleted;

    public int getUserDealID() {
        return userDealID;
    }

    public void setUserDealID(int userDealID) {
        this.userDealID = userDealID;
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

    public String getRedeemedDate() {
        return redeemedDate;
    }

    public void setRedeemedDate(String redeemedDate) {
        this.redeemedDate = redeemedDate;
    }

    public boolean isRedeemed() {
        return isRedeemed;
    }

    public void setRedeemed(boolean redeemed) {
        isRedeemed = redeemed;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
