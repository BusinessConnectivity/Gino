package com.bizconnectivity.gino.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class FavDealModel extends RealmObject{

    @PrimaryKey
    @Index
    private int userFavDealID;
    private int userID;
    private int dealID;
    private RealmList<DealModel> deals;

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

    public RealmList<DealModel> getDeals() {
        return deals;
    }

    public void setDeals(RealmList<DealModel> deals) {
        this.deals = deals;
    }
}
