package com.bizconnectivity.gino.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class UserDealModel extends RealmObject{

    @PrimaryKey
    @Index
    private int userDealID;
    @Index
    private int userID;
    @Index
    private int dealID;
    private int quantity;
    private String redeemedDate;
    private boolean isRedeemed;
    private boolean isExpired;
    private String createdDate;
    private RealmList<DealModel> deals;

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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public RealmList<DealModel> getDeals() {
        return deals;
    }

    public void setDeals(RealmList<DealModel> deals) {
        this.deals = deals;
    }
}
